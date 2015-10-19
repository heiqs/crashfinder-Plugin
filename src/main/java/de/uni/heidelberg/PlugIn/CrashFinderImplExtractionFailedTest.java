package de.uni.heidelberg.PlugIn;

import hudson.model.BuildListener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Antsa Harinala Andriamboavonjy
 * This class is mainly used to extract the name of the failed test from log file
 * of jenkins
 */

public class CrashFinderImplExtractionFailedTest {
	
	/**
	 * Extract class name failed test.
	 * @param input - input string from log file
	 * @return - string containing name of the class
	 */
	public static ArrayList<String> extractClassNameFailedTest(String input,
															   BuildListener
																	   listener)
	{
		ArrayList<String> listTest = new ArrayList<String>();
		ArrayList<String> listFailedTest = new ArrayList<String>();

		String regexEnd = "(?=" + "\\[INFO\\]" + ")";
		String regex = "[\\-]+\\n T E S T S\\n[\\-]+(.*?)" + regexEnd;
		Pattern pattern = Pattern.compile(regex,Pattern.DOTALL);
		Matcher matcher = pattern.matcher(input);
		
		if(matcher.find())
		{
			
			String content = matcher.group();
			listTest = CrashFinderImplExtractionFailedTest.extractAllTest
					(content,listener);
			listener.getLogger().println("Size test: "  + listTest.size());
			listFailedTest = CrashFinderImplExtractionFailedTest.getFullNameFailedTest(content,listTest,listener);
		}
		
		return listFailedTest;
	}
	
	/**
	 * Extract all test names from log file
	 * @param input - string input log
	 * @return - list containing name executed tests
	 */
	public static ArrayList<String> extractAllTest(String input,
												   BuildListener  listener)
	{
		ArrayList<String> listTests = new ArrayList<String>();
		String regex = "Running " + "(.*?)" + "\\n";
                Pattern pattern = Pattern.compile(regex,Pattern.DOTALL);
		Matcher matcher = pattern.matcher(input);
		while(matcher.find())
		{
			listener.getLogger().println("Found 1");
			String fullNameTestClass = matcher.group(1);
			listTests.add(fullNameTestClass);
		}
		
		return listTests;
	}
	
	/**
	 * Retrieve full name failed test from log file
	 * @param input - part of the log string
	 * @param listTest - list of all tests
	 * @return
	 */
	public static ArrayList<String> getFullNameFailedTest(String input, ArrayList<String> listTest, BuildListener listener)
	{
		listener.getLogger().println("Input 3: " + input);
		ArrayList<String> listFailedTest = new ArrayList<String> ();
		String beginRegex = "Results\\s?:\\n+";
		//String regex = beginRegex + "Failed tests: " + "(.*?)" + "\\n";
		String regex = beginRegex  + "(.*?)" + ":\\s*" + "(.*?)" + "\\n";
		
		Pattern pattern = Pattern.compile(regex,Pattern.DOTALL);
		Matcher matcher = pattern.matcher(input);
		while(matcher.find())
		{
			
			String strFound = matcher.group(2);
                        listener.getLogger().println("String found: " + strFound);
			for ( int k = 0 ; k < listTest.size() ; k++)
			{
				String fullNameTest = listTest.get(k);
				if(strFound.contains(fullNameTest))
			    {
					listFailedTest.add(fullNameTest);
                                        listener.getLogger().println("Test: " + fullNameTest);
					return listFailedTest;
			    }
				
			}//end for
			
		}//end while
		
		return listFailedTest;
	}
	
	
	/**
	public static void main(String[]args) throws IOException
	{
		String pathToLastFailedBuildLog = "/home/antsaharinala/BugLocator/WorkspaceNetbeans/BugLocator/work/jobs/musicfailingVersion/builds/lastFailedBuild/log";
		File fileLastFailedBuildLog = new File(pathToLastFailedBuildLog);
		String contentLog = DocumentReader.slurpFile(fileLastFailedBuildLog);
		//ArrayList<String> listTest = new ArrayList<String> ();
		ArrayList<String> listFailedTest = new ArrayList<String>();
		
		listFailedTest = CrashFinderImplExtractionNameFailedTest.extractClassNameFailedTest(contentLog);
		
		
		System.out.println("List failed test");
		for ( int j = 0 ; j < listFailedTest.size() ; j++)
		{
			System.out.println(listFailedTest.get(j));
		}

	}**/
	
}
