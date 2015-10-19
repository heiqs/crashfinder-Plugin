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
	
	public void handleAutomate() throws IOException
	{
		if(this.stackTrace.equals("automatically"))
		{
			//String pathToWorkspace = this.build.getWorkspace().getRemote();
			//FilePathAbsolutizer filePathAbsolutizer = new FilePathAbsolutizer(pathToWorkspace);
			
//			String pathResult = filePathAbsolutizer.absolutize("../");
//			String pathToLogLastFailedBuild = pathResult + "/" + "builds/lastFailedBuild/log";
//			listener.getLogger().println("Path to log " +
//							"last failed: " + pathToLogLastFailedBuild);

			List<String> logLines = build.getLog(Integer.MAX_VALUE);
			String contentLog = Joiner.on("\n").join(logLines);
                        listener.getLogger().println("String contentLog: " + contentLog);
//			String contentLog = DocumentReader.slurpStream(logStream);
//			listener.getLogger().println("Log stream: " + logStream);

			String pathToDirectoryStackTrace =
					CrashFinderImplSearchStackTrace
							.searchStackTraceContent(contentLog);
			listener.getLogger().println("path to directory stack trace: " +
					pathToDirectoryStackTrace);

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
			
                        /**
			String pathToWorkspace = this.build.getWorkspace().getRemote();
			FilePathAbsolutizer filePathAbsolutizer = new FilePathAbsolutizer(pathToWorkspace);
			
			String pathResult = null;
			try {
				pathResult = filePathAbsolutizer.absolutize("../");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			String pathToLogLastFailedBuild = pathResult + "/" + "builds/lastFailedBuild/log";
			//File fileLogLastFailedBuild = new File(pathToLogLastFailedBuild);
			String pathToDirectoryStackTrace = null;
			try {
				pathToDirectoryStackTrace = CrashFinderImplSearchStackTrace.searchPathToDirectoryStackTrace(pathToLogLastFailedBuild);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				listener.getLogger().println("Cannot found path to directory stack trace");
			}**/
			this.listFullNameFailedTest.add(this.fullNameFailedTestClass);
                        List<String> logLines = null;
                        try {
                            logLines = build.getLog(Integer.MAX_VALUE);
                        } catch (IOException ex) {
                            //Logger.getLogger(CrashFinderImplGetStackTrace.class.getName()).log(Level.SEVERE, null, ex);
                            listener.getLogger().println("Error get log lines");
                        }
			String contentLog = Joiner.on("\n").join(logLines);
                        listener.getLogger().println("String contentLog: " + contentLog);
//			String contentLog = DocumentReader.slurpStream(logStream);
//			listener.getLogger().println("Log stream: " + logStream);

			String pathToDirectoryStackTrace =
					CrashFinderImplSearchStackTrace
							.searchStackTraceContent(contentLog);
			listener.getLogger().println("path to directory stack trace: " +
					pathToDirectoryStackTrace);
			
			try {
				//this.listPathToStackTrace = CrashFinderImplSearchStackTrace.searchFileStackTrace(pathToWorkspace);
				this.listPathToStackTrace = CrashFinderImplSearchStackTrace.searchFileStackTrace(pathToDirectoryStackTrace);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				listener.getLogger().println("Error search path to stack trace");
			}
			
		}else if(this.stackTrace.equals("manually") == true  && this.pathToStackTrace.equals("")==false && this.fullNameFailedTestClass.equals("")==true)
		{
			
                        listener.getLogger().println("Miditra ato");
			//String pathToWorkspace = this.build.getWorkspace().getRemote();
			//FilePathAbsolutizer filePathAbsolutizer = new FilePathAbsolutizer(pathToWorkspace);
			//String pathResult = null;
			//try {
			//	pathResult = filePathAbsolutizer.absolutize("../");
			//} catch (IOException e) {
				// TODO Auto-generated catch block
			//	e.printStackTrace();
			//}
			
			//String pathToLogLastFailedBuild = pathResult + "/" + "builds/lastFailedBuild/log";
			//File fileLogLastFailedBuild = new File(pathToLogLastFailedBuild);
                    	List<String> logLines = null;
                        try {
                            logLines = build.getLog(Integer.MAX_VALUE);
                        } catch (IOException ex) {
                            Logger.getLogger(CrashFinderImplGetStackTrace.class.getName()).log(Level.SEVERE, null, ex);
                            listener.getLogger().println("Error by read log");
                        }
			String contentLog = Joiner.on("\n").join(logLines);
                        //listener.getLogger().println("String contentLog: " + contentLog);
//			String contentLog = DocumentReader.slurpStream(logStream);
//			listener.getLogger().println("Log stream: " + logStream);

			//String pathToDirectoryStackTrace =
			//		CrashFinderImplSearchStackTrace
			//				.searchStackTraceContent(contentLog);
			//listener.getLogger().println("path to directory stack trace: " +
			//		pathToDirectoryStackTrace);
			//String contentLog = null;
			//try {
			//	contentLog = DocumentReader.slurpFile(fileLogLastFailedBuild);
			//}catch (IOException e) {
				// TODO Auto-generated catch block
			//	e.printStackTrace();
			//}
			this.listFullNameFailedTest = CrashFinderImplExtractionFailedTest
					.extractClassNameFailedTest(contentLog,listener);
			this.listPathToStackTrace.add(pathToStackTrace);
		}
		
	}
}
