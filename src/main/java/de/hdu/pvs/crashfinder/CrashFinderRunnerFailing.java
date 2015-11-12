package de.hdu.pvs.crashfinder;

import com.ibm.wala.ipa.slicer.Statement;
import com.ibm.wala.util.CancelException;

import de.hdu.pvs.crashfinder.analysis.FindFailingSeed;
import de.hdu.pvs.crashfinder.analysis.IRStatement;
import de.hdu.pvs.crashfinder.analysis.Intersection;
import de.hdu.pvs.crashfinder.analysis.ShrikePoint;
import de.hdu.pvs.crashfinder.analysis.Slicing;
import de.hdu.pvs.crashfinder.analysis.SlicingOutput;
import de.hdu.pvs.crashfinder.instrument.InstrumenterMain;
import de.hdu.pvs.crashfinder.util.Files;
import de.hdu.pvs.crashfinder.util.Globals;
import de.hdu.pvs.crashfinder.util.WALAUtils;
import hudson.model.BuildListener;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * 
 * @author Antsa Harinala Andriamboavonjy, Dominik Fay Created in October 2015.
 * 
 */
public class CrashFinderRunnerFailing implements CrashFinderRunner {

	private final CrashFinderImplementation crashFinderImpl;

	private BuildListener listener;

	private String seed = null;

	private Statement seedStatement = null;

	public CrashFinderRunnerFailing(
			CrashFinderImplementation crashFinderImpl,
			BuildListener listener) {
		this.crashFinderImpl = crashFinderImpl;
		this.listener = listener;
	}

	public String getSeed() {
		if (this.seed == null) {
			throw new NullPointerException("Seed has not been computed.");
		}
		return this.seed;
	}

	public void runner() {

		try {

			String pathToJar = crashFinderImpl.getPathToJarFile();
			String pathToStackTrace = crashFinderImpl.getPathToStackTrace();
			String pathToDiffFile = crashFinderImpl.getPathToDiffOut();
			String pathToInstrJar = crashFinderImpl
					.getPathToInstrumentedJarFile();
			String pathToLogSlicing = crashFinderImpl.getPathToLogSlicing();
			String pathToExclusionFile = crashFinderImpl.getPathToExclusionFile();
			String pathToWorkspace = crashFinderImpl.getCanonicalPathToWorkspaceDir();

			// 1. Slicing
			listener.getLogger().println("Initializing slicing ....");
			Slicing slicing = crashFinderImpl.initializeSlicing(pathToJar, pathToExclusionFile);

			listener.getLogger().println("Find failing seed statement ....");
			FindFailingSeed computeFailingSeed = new FindFailingSeed();
			listener.getLogger().println("stack: "+pathToStackTrace);
			
			Statement failingSeed = computeFailingSeed.findSeedStatementFailing(pathToStackTrace, slicing);
			int failingSeedLineNum = computeFailingSeed.computeSeed(pathToStackTrace).getLineNumber();
			String failingSeedClass = computeFailingSeed.computeSeed(pathToStackTrace).getSeedClass();
			this.seed = failingSeedClass+":"+failingSeedLineNum;
			this.seedStatement = failingSeed;
			listener.getLogger().println("Seed runner: " + this.seed);

			// 3.Backward slicing
			Collection<? extends Statement> slice = slicing.computeSlice(seedStatement);
			WALAUtils.dumpSliceToFile(new ArrayList<Statement>(slice), pathToLogSlicing);

			// 4. Intersection
			listener.getLogger().println("Executing intersection ....");
			Intersection intersection = new Intersection();
			List<String> matchingSet = intersection.matchingSet(pathToDiffFile);
			Collection<Statement> chop = intersection.intersection(matchingSet, slice);
			Collection<IRStatement> irs = Slicing.convert(chop);
			SlicingOutput output = new SlicingOutput(irs);
			System.out.println("Workspace Path: " + pathToWorkspace);

			StringBuilder sb = new StringBuilder();
			for (ShrikePoint po: output.getAllShrikePoints()) {
				sb.append(po);
				sb.append(Globals.lineSep);
			}
			try {
				//String shrikeDump = "shrikeDump_failing.txt";
	            File shrikeDump = new File(pathToWorkspace, "shrikeDump_failing.txt");
				System.out.println("Write to file: " + shrikeDump);
				Files.writeToFile(sb.toString(), shrikeDump);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// 5.Instrument
			listener.getLogger().println("Executing instrumentation ...");
			InstrumenterMain instrumenter = new InstrumenterMain();
			instrumenter.instrument(pathToJar, pathToInstrJar, chop);

		} catch (IOException ex) {
			Logger.getLogger(CrashFinderRunnerFailing.class.getName())
					.log(Level.SEVERE, null, ex);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CancelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Statement getSeedStatement() {
		if (this.seedStatement == null) {
			throw new NullPointerException("Seed statement has not been "
					+ "computed.");
		}
		return this.seedStatement;
	}
}
