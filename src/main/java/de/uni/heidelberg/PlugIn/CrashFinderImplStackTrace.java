package de.uni.heidelberg.PlugIn;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.uni.heidelberg.Utils.DocumentReader;
import de.uni.heidelberg.Utils.FilePathAbsolutizer;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;

public class CrashFinderImplStackTrace {
	
	//private String automateStackTrace;
	
	//private String manuallyStackTrace;
	private String stackTrace;
	
	private String pathToStackTrace;
	
	private String fullNameFailedTestClass;
	
	
	
	private ArrayList<String> listPathToStackTrace;
	
	private ArrayList<String> listFullNameFailedTest;
	
	private AbstractBuild build;
	
	private BuildListener listener;
	
	public CrashFinderImplStackTrace(String stackTrace,
									 //String automateStackTrace, 
									 //String manuallyStackTrace,
									 String pathToStackTrace,
									 String fullNameFailedTestClass,
									 AbstractBuild build,
									 BuildListener listener)
	{
		this.stackTrace = stackTrace;
		this.pathToStackTrace = pathToStackTrace;
		this.fullNameFailedTestClass = fullNameFailedTestClass;
		this.build = build;
		this.listener = listener;
		this.listPathToStackTrace = new ArrayList<String>();
		this.listFullNameFailedTest = new ArrayList<String>();
	}
	
	public void start() throws IOException
	{
		handleAutomate();
		handleManual();
	}
	
	public ArrayList<String> getListPathToStackTrace()
	{
		return this.listPathToStackTrace;
	}
	
	public ArrayList<String> getListNameFailedTest()
	{
		return this.listFullNameFailedTest;
	}
	
	/**
	public String getFinalPathToStackTrace()
	{
		return this.finalPathToStackTrace;
	}
	
	public String getFinalFullNameFailedTestClass()
	{
		return this.fullNameFailedTestClass;
	}**/
	
	public void handleAutomate() throws IOException
	{
		if(this.stackTrace.equals("automatically"))
		{
			
			String pathToWorkspace = this.build.getWorkspace().getRemote();
			String projectName = this.build.getProject().getName();
			FilePathAbsolutizer filePathAbsolutizer = new FilePathAbsolutizer(pathToWorkspace);
			
			String pathResult = filePathAbsolutizer.absolutize("../");
			//String projectName = build.getProject().getAbsoluteUrl();
			String pathToLogLastFailedBuild = pathResult + "/" + "builds/lastFailedBuild/log";
			listener.getLogger().println("Path last failed build: " + pathToLogLastFailedBuild);
			File fileLogLastFailedBuild = new File(pathToLogLastFailedBuild);
			
			this.listPathToStackTrace = CrashFinderImplSearchStackTrace.searchFileStackTrace(pathToWorkspace);
			String contentLog = DocumentReader.slurpFile(fileLogLastFailedBuild);
			this.listFullNameFailedTest = CrashFinderImplExtractionNameFailedTest.extractClassNameFailedTest(contentLog);
		}
	}
	
	public void handleManual()
	{
		if(this.stackTrace.equals("manually") == true && this.pathToStackTrace.equals("") ==false && this.fullNameFailedTestClass.equals("") == false)
		{
			this.listFullNameFailedTest.add(fullNameFailedTestClass);
			this.listPathToStackTrace.add(pathToStackTrace);
		}
	}
}
