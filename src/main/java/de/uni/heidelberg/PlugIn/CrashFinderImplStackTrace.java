package de.uni.heidelberg.PlugIn;

import java.io.File;
import java.io.IOException;

import hudson.model.AbstractBuild;
import hudson.model.BuildListener;

public class CrashFinderImplStackTrace {
	
	//private String automateStackTrace;
	
	//private String manuallyStackTrace;
	private String stackTrace;
	
	private String pathToStackTrace;
	
	private String fullNameFailedTestClass;
	
	private String finalPathToStackTrace = "";
	
	private String finalFullNameFailedTestClass = "";
	
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
		//this.automateStackTrace = automateStackTrace;
		//this.manuallyStackTrace = manuallyStackTrace;
		this.stackTrace = stackTrace;
		this.pathToStackTrace = pathToStackTrace;
		this.fullNameFailedTestClass = fullNameFailedTestClass;
		this.build = build;
		this.listener = listener;
	}
	
	public void start() throws IOException
	{
		handleAutomate();
		handleManual();
		
	}
	
	public String getFinalPathToStackTrace()
	{
		return this.finalPathToStackTrace;
	}
	
	public String getFinalFullNameFailedTestClass()
	{
		return this.fullNameFailedTestClass;
	}
	
	public void handleAutomate() throws IOException
	{
		if(this.stackTrace.equals("automatically"))
		{
			//listener.getLogger().println("Enter automate");
			String pathToWorkspace = this.build.getWorkspace().getRemote();
			this.finalPathToStackTrace = CrashFinderImplSearchStackTrace.searchFileStackTrace(pathToWorkspace,this.listener);
			this.finalFullNameFailedTestClass = CrashFinderImplSearchStackTrace.extractTestClass(new File(this.finalPathToStackTrace));
		}
	}
	
	public void handleManual()
	{
		if(this.stackTrace.equals("manually") == true && this.pathToStackTrace.equals("") ==false && this.fullNameFailedTestClass.equals("") == false)
		{
			//listener.getLogger().println("Enter manual");
			this.finalFullNameFailedTestClass = this.fullNameFailedTestClass;
			this.finalPathToStackTrace = this.pathToStackTrace;
		}
	}
}
