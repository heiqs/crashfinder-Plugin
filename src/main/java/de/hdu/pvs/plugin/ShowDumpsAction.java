package de.hdu.pvs.plugin;

import hudson.model.Action;

import java.util.ArrayList;

/**
 * 
 * @author Dominik Fay created in October 2015.
 */

public class ShowDumpsAction implements Action {

	private final ArrayList<String> diffCoverageLoc;
	private final ArrayList<String> diffCoverage;
	private final ArrayList<String> passingDumpFile;
	private final ArrayList<String> failingDumpFile;

	public ShowDumpsAction(ArrayList<String> diffCoverageLoc2, ArrayList<String> diffCoverage2, ArrayList<String> failReader, ArrayList<String> passReader) {
		this.diffCoverageLoc = diffCoverageLoc2;
		this.diffCoverage = diffCoverage2;
		this.passingDumpFile = failReader;
		this.failingDumpFile = passReader;
	}

	public ArrayList<String> getDiffCoverageLocContent() {
		return diffCoverageLoc;
	}

	public ArrayList<String> getDiffCoverageContent() {
		return diffCoverage;
	}
	
	public ArrayList<String> getPassingDumpContent() {
		return passingDumpFile;
	}

	public ArrayList<String> getFailingDumpContent() {
		return failingDumpFile;
	}
	
	@Override
	public String getIconFileName() {
		return "/images/jenkins.png";
	}

	@Override
	public String getDisplayName() {
		return "CrashFinder Results";
	}

	@Override
	public String getUrlName() {
		return "cf-results";
	}
}
