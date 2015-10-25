package de.uni.heidelberg.PlugIn;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;
import de.uni.heidelberg.Utils.DocumentReader;
import de.uni.heidelberg.Utils.FilePathAbsolutizer;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.print.Doc;

/**
 * 
 * @author Antsa Harinala Andriamboavonjy
 * Created in October 2015
 *
 */

public class CrashFinderImplGetStackTrace {
	
	private String stackTrace;
	
	private String pathToStackTrace;
	
	private String fullNameFailedTestClass;
	
	private ArrayList<String> listPathToStackTrace;
	
	private ArrayList<String> listFullNameFailedTest;
	
	private AbstractBuild build;
	
	private BuildListener listener;
	
	public CrashFinderImplGetStackTrace(String stackTrace,
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
	
	public void handleAutomate() throws IOException
	{
		if(this.stackTrace.equals("automatically"))
		{
                        List<String> logLines = build.getLog(Integer.MAX_VALUE);
			String contentLog = Joiner.on("\n").join(logLines);
                        String pathToDirectoryStackTrace =
					CrashFinderImplSearchStackTrace
							.searchStackTraceContent(contentLog);
			

			this.listPathToStackTrace = CrashFinderImplSearchStackTrace.searchFileStackTrace(pathToDirectoryStackTrace);
			this.listFullNameFailedTest = CrashFinderImplExtractionFailedTest
					.extractClassNameFailedTest(contentLog,listener);
		}
	}
	
	public void handleManual()
	{
		if(this.stackTrace.equals("manually") == true && this.pathToStackTrace.equals("") ==false && this.fullNameFailedTestClass.equals("") == false)
		{
			this.listFullNameFailedTest.add(fullNameFailedTestClass);
			this.listPathToStackTrace.add(pathToStackTrace);
			
		}else if(this.stackTrace.equals("manually") == true && this.pathToStackTrace.equals("") == true && this.fullNameFailedTestClass.equals("")==false)
		{
			this.listFullNameFailedTest.add(this.fullNameFailedTestClass);
                        List<String> logLines = null;
                        try {
                            logLines = build.getLog(Integer.MAX_VALUE);
                        } catch (IOException ex) {
                            listener.getLogger().println("Error by getting log file");
                        }
			String contentLog = Joiner.on("\n").join(logLines);
                        String pathToDirectoryStackTrace =
					CrashFinderImplSearchStackTrace
							.searchStackTraceContent(contentLog);
			
			
			try {
				
				this.listPathToStackTrace = CrashFinderImplSearchStackTrace.searchFileStackTrace(pathToDirectoryStackTrace);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				listener.getLogger().println("Error by searching path to stack trace");
			}
			
		}else if(this.stackTrace.equals("manually") == true  && this.pathToStackTrace.equals("")==false && this.fullNameFailedTestClass.equals("")==true)
		{
			List<String> logLines = null;
            try {
                       logLines = build.getLog(Integer.MAX_VALUE);
                 } catch (IOException ex) {
                       Logger.getLogger(CrashFinderImplGetStackTrace.class.getName()).log(Level.SEVERE, null, ex);
                       listener.getLogger().println("Error by reading log file");
                 }
			String contentLog = Joiner.on("\n").join(logLines);
            this.listFullNameFailedTest = CrashFinderImplExtractionFailedTest
					.extractClassNameFailedTest(contentLog,listener);
			this.listPathToStackTrace.add(pathToStackTrace);
		}
		
	}
}
