package de.hdu.pvs.plugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;

import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * 
 * @author Antsa Harinala Andriamboavonjy Created in October 2015
 * 
 */

public class CrashFinderGetStackTrace {

	private String stackTrace;

	private String pathToStackTrace;

	private String fullNameFailedTestClass;

	private ArrayList<String> listPathToStackTrace;

	private ArrayList<String> listFullNameFailedTest;

	private AbstractBuild<?, ?> build;

	private BuildListener listener;

	public CrashFinderGetStackTrace(String stackTrace, String pathToStackTrace,
			String fullNameFailedTestClass, AbstractBuild<?, ?> build,
			BuildListener listener) {
		this.stackTrace = stackTrace;
		this.pathToStackTrace = pathToStackTrace;
		this.fullNameFailedTestClass = fullNameFailedTestClass;
		this.build = build;
		this.listener = listener;
		this.listPathToStackTrace = new ArrayList<String>();
		this.listFullNameFailedTest = new ArrayList<String>();
	}

	public void start() throws IOException {
		handleAutomate();
		handleManual();
	}

	public ArrayList<String> getListPathToStackTrace() {
		return this.listPathToStackTrace;
	}

	public ArrayList<String> getListNameFailedTest() {
		return this.listFullNameFailedTest;
	}

	public void handleAutomate() throws IOException {
		if (this.stackTrace.equals("automatically")) {
			List<String> logLines = build.getLog(Integer.MAX_VALUE);
			String contentLog = Joiner.on("\n").join(logLines);
			String pathToDirectoryStackTrace = CrashFinderSearchStackTrace
					.searchStackTraceContent(contentLog);

			this.listPathToStackTrace = CrashFinderSearchStackTrace
					.searchFileStackTrace(pathToDirectoryStackTrace);
			this.listFullNameFailedTest = CrashFinderFailedTestExtraction
					.extractClassNameFailedTest(contentLog, listener);
		}
	}

	public void handleManual() {
		if (this.stackTrace.equals("manually") == true
				&& this.pathToStackTrace.equals("") == false
				&& this.fullNameFailedTestClass.equals("") == false) {
			this.listFullNameFailedTest.add(fullNameFailedTestClass);
			this.listPathToStackTrace.add(pathToStackTrace);

		} else if (this.stackTrace.equals("manually") == true
				&& this.pathToStackTrace.equals("") == true
				&& this.fullNameFailedTestClass.equals("") == false) {
			this.listFullNameFailedTest.add(this.fullNameFailedTestClass);
			List<String> logLines = null;
			try {
				logLines = build.getLog(Integer.MAX_VALUE);
			} catch (IOException ex) {
				listener.getLogger().println("Error by getting log file");
			}
			String contentLog = Joiner.on("\n").join(logLines);
			String pathToDirectoryStackTrace = CrashFinderSearchStackTrace
					.searchStackTraceContent(contentLog);

			try {

				this.listPathToStackTrace = CrashFinderSearchStackTrace
						.searchFileStackTrace(pathToDirectoryStackTrace);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				listener.getLogger().println(
						"Error by searching path to stack trace");
			}

		} else if (this.stackTrace.equals("manually") == true
				&& this.pathToStackTrace.equals("") == false
				&& this.fullNameFailedTestClass.equals("") == true) {
			List<String> logLines = null;
			try {
				logLines = build.getLog(Integer.MAX_VALUE);
			} catch (IOException ex) {
				Logger.getLogger(CrashFinderGetStackTrace.class.getName()).log(
						Level.SEVERE, null, ex);
				listener.getLogger().println("Error by reading log file");
			}
			String contentLog = Joiner.on("\n").join(logLines);
			this.listFullNameFailedTest = CrashFinderFailedTestExtraction
					.extractClassNameFailedTest(contentLog, listener);
			this.listPathToStackTrace.add(pathToStackTrace);
		}

	}
}
