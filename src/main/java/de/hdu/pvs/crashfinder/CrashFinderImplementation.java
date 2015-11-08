package de.hdu.pvs.crashfinder;

import com.ibm.wala.ipa.slicer.Slicer.ControlDependenceOptions;
import com.ibm.wala.ipa.slicer.Slicer.DataDependenceOptions;

import de.hdu.pvs.crashfinder.analysis.Slicing;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;

/**
 * 
 * @author Antsa Harinala Andriamboavonjy, Dominik Fay. Created in October 2015.
 */
public class CrashFinderImplementation{

	private String canonicalPathToWorkspaceDir;

	private String pathToDiffOut;

	private String pathToLogDiff;

	private final String pathToExclusionFile = "resources/JavaAllExclusions.txt";

	private String pathToStackTrace;

	private String pathToJarFile;

	private String pathToInstrumentedJarFile;

	private String pathToLogSlicing;

	private BuildListener listener;

	private AbstractBuild<?, ?> build;

	private Launcher launcher;

	private String seed = "";

	public CrashFinderImplementation(String pathToDiffOut,
			String pathToLogDiff, String pathToStackTrace,
			String pathToJarFile, String pathToInstrumentedJarFile,
			String pathToLogSlicing, String canonicalPathToWorkspaceDir,
			BuildListener listener, AbstractBuild<?, ?> build, Launcher launcher) {
		this.pathToDiffOut = pathToDiffOut;
		this.pathToLogDiff = pathToLogDiff;
		this.pathToJarFile = pathToJarFile;
		this.pathToInstrumentedJarFile = pathToInstrumentedJarFile;
		this.pathToStackTrace = pathToStackTrace;
		this.canonicalPathToWorkspaceDir = canonicalPathToWorkspaceDir;
		this.pathToLogSlicing = pathToLogSlicing;
		this.setListener(listener);
		this.setBuild(build);
		this.setLauncher(launcher);

	}

	public String getSeed() {
		return this.seed;
	}

	public String getPathToLogSlicing() {
		return this.pathToLogSlicing;
	}

	public String getPathToDiffOut() {
		return this.pathToDiffOut;
	}

	public String getPathToLogDiff() {
		return this.pathToLogDiff;
	}

	public String getPathToJarFile() {
		return this.pathToJarFile;
	}

	public String getPathToInstrumentedJarFile() {
		return this.pathToInstrumentedJarFile;
	}

	public String getPathToStackTrace() {
		return this.pathToStackTrace;
	}

	public void setSeed(String seed) {
		this.seed = seed;
	}

	public String getCanonicalPathToWorkspaceDir() {
		return canonicalPathToWorkspaceDir;
	}

	public void setPathToDiffOut(String pathToDiffOut) {
		this.pathToDiffOut = pathToDiffOut;
	}

	public void setPathToLogDiff(String pathToLogDiff) {
		this.pathToLogDiff = pathToLogDiff;
	}

	public void setPathToJarFile(String pathToJarFile) {
		this.pathToJarFile = pathToJarFile;
	}

	public void setPathToInstrumentedJar(String pathToInstrumentedJar) {
		this.pathToInstrumentedJarFile = pathToInstrumentedJar;
	}

	public void setPathToStackTrace(String pathToStackTrace) {
		this.pathToStackTrace = pathToStackTrace;
	}

	public String getPathToExclusionFile() {
		return pathToExclusionFile;
	}

	public BuildListener getListener() {
		return listener;
	}

	public void setListener(BuildListener listener) {
		this.listener = listener;
	}

	public AbstractBuild<?, ?> getBuild() {
		return build;
	}

	public void setBuild(AbstractBuild<?, ?> build) {
		this.build = build;
	}

	public Launcher getLauncher() {
		return launcher;
	}

	public void setLauncher(Launcher launcher) {
		this.launcher = launcher;
	}
	
	public Slicing initializeSlicing(final String jar, String pathToExclusionFile) {

		Slicing slicing = null;
		slicing = new Slicing(jar, "", pathToExclusionFile);
		slicing.CallGraphBuilder();
		slicing.setDataDependenceOptions(DataDependenceOptions.NONE);
		slicing.setControlDependenceOptions(ControlDependenceOptions.NO_EXCEPTIONAL_EDGES);
		slicing.setContextSensitive(true); // context-insensitive
		return slicing;

	}
}
