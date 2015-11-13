package de.hdu.pvs.crashfinder;

import hudson.model.BuildListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.ibm.wala.ipa.slicer.Statement;
import com.ibm.wala.util.CancelException;

import de.hdu.pvs.crashfinder.analysis.FindFailingSeed;
import de.hdu.pvs.crashfinder.analysis.Slicing;
import de.hdu.pvs.crashfinder.instrument.InstrumenterMain;
import de.hdu.pvs.crashfinder.util.WALAUtils;


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
			listener.getLogger().println("Initializing slicing for failing version ....");
			Slicing slicing = crashFinderImpl.initializeSlicing(pathToJar, pathToExclusionFile);

			// 2. Finding failing seed statement
			listener.getLogger().println("Find failing seed statement ....");
			FindFailingSeed computeFailingSeed = new FindFailingSeed();
			listener.getLogger().println("StackTrace path: "+pathToStackTrace);
			Statement failingSeed = computeFailingSeed.findSeedStatementFailing(pathToStackTrace, slicing);
			int failingSeedLineNum = computeFailingSeed.computeSeed(pathToStackTrace).getLineNumber();
			String failingSeedClass = computeFailingSeed.computeSeed(pathToStackTrace).getSeedClass();
			this.seed = failingSeedClass+":"+failingSeedLineNum;
			this.seedStatement = failingSeed;

			// 3.Backward slicing
			Collection<? extends Statement> slice = slicing.computeSlice(seedStatement);
			WALAUtils.dumpSliceToFile(new ArrayList<Statement>(slice), pathToLogSlicing);

			// 4. Intersection
			listener.getLogger().println("Executing intersection for failing version ....");
            File shrikeDump = new File(pathToWorkspace, "shrikeDump_failing.txt");
			Collection<Statement> chop = crashFinderImpl.intersection(pathToDiffFile, slice);
			crashFinderImpl.shrikeWriter(pathToWorkspace, chop, shrikeDump);
			
			// 5.Instrument
			listener.getLogger().println("Executing instrumentation for failing version ...");
			InstrumenterMain instrumenter = new InstrumenterMain();
			instrumenter.instrument(pathToJar, pathToInstrJar, chop);
			
		} catch (CancelException e) {
			listener.getLogger().println("Error: in computing backward slice for failing version");
			e.printStackTrace();
		} catch (IOException e) {
			listener.getLogger().println("Error: in computing failing seed statement");
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
