package de.hdu.pvs.crashfinder;

import hudson.model.BuildListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.ibm.wala.ipa.slicer.Statement;
import com.ibm.wala.util.CancelException;

import de.hdu.pvs.crashfinder.analysis.FindPassingSeed;
import de.hdu.pvs.crashfinder.analysis.Slicing;
import de.hdu.pvs.crashfinder.instrument.InstrumenterMain;
import de.hdu.pvs.crashfinder.util.WALAUtils;

/**
 * 
 * @author Antsa Harinala Andriamboavonjy, Dominik Fay Created in October 2015
 * 
 */
public class CrashFinderRunnerPassing implements CrashFinderRunner {

	private final CrashFinderImplementation crashFinderImpl;
	private Statement seedStatement;
	private BuildListener listener;
	private String seed;

	public CrashFinderRunnerPassing(
			CrashFinderImplementation crashFinderImpl,
			BuildListener listener, String seed) {
		this.crashFinderImpl = crashFinderImpl;
		this.listener = listener;
		this.seed = seed;
	}

	public String getSeed() {
		return this.seed;
	}

	public void runner() {

		try {
			String pathToJar = crashFinderImpl.getPathToJarFile();
			//String pathToStackTrace = crashFinderImpl.getPathToStackTrace();
			String pathToDiffFile = crashFinderImpl.getPathToDiffOut();
			String pathToInstrJar = crashFinderImpl
					.getPathToInstrumentedJarFile();
			String pathToLogSlicing = crashFinderImpl.getPathToLogSlicing();
			String pathToExclusionFile = crashFinderImpl.getPathToExclusionFile();
			String pathToWorkspace = crashFinderImpl.getCanonicalPathToWorkspaceDir();

			// 1. Slicing
			listener.getLogger().println("Initializing slicing for passing version....");
			Slicing slicing = crashFinderImpl.initializeSlicing(pathToJar, pathToExclusionFile);

			// 2. Finding passing seed statement
			listener.getLogger().println("Find passing seed statement ....");
			FindPassingSeed computePassingSeed = new FindPassingSeed();
			String passingSeed = computePassingSeed.computeSeed(this.seed, pathToDiffFile);
			this.seedStatement = computePassingSeed.findSeedStatement(passingSeed, slicing);
			listener.getLogger().println("Passing seed statement: " + this.seedStatement);
			this.seed = crashFinderImpl.getSeed();
			

			// 3.Backward slicing
			Collection<? extends Statement> slice = slicing.computeSlice(seedStatement);
			WALAUtils.dumpSliceToFile(new ArrayList<Statement>(slice), pathToLogSlicing);

			// 4. Intersection
			listener.getLogger().println("Executing intersection for passing version....");
            File shrikeDump = new File(pathToWorkspace, "shrikeDump_passing.txt");
			Collection<Statement> chop = crashFinderImpl.intersection(pathToDiffFile, slice);
			crashFinderImpl.shrikeWriter(pathToWorkspace, chop, shrikeDump);
			
			// 5. Instrument
			listener.getLogger().println("Executing instrumentation for passing version ...");
			InstrumenterMain instrumenter = new InstrumenterMain();
			instrumenter.instrument(pathToJar, pathToInstrJar, chop);

		} catch (CancelException e) {
			listener.getLogger().println("Error: in computing backward slice for passing version");
			e.printStackTrace();
		} catch (IOException e) {
			listener.getLogger().println("Error: in computing passing seed from the failing seed and stack trace");
			e.printStackTrace();
		} catch (InterruptedException e) {
			listener.getLogger().println("Error: in computing passing seed statement");
			e.printStackTrace();
		}
	}
}
