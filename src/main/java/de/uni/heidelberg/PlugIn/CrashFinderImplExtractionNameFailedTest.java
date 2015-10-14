package de.uni.heidelberg.PlugIn;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.uni.heidelberg.Utils.DocumentReader;

public class CrashFinderImplExtractionNameFailedTest {
	
	public static ArrayList<String> extractClassNameFailedTest(String input)
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
			listTest = CrashFinderImplExtractionNameFailedTest.extractAllTest(content);
			listFailedTest = CrashFinderImplExtractionNameFailedTest.getFullNameFailedTest(content,listTest);
		}
		
		return listFailedTest;
	}
	
	public static ArrayList<String> extractAllTest(String input)
	{
		ArrayList<String> listTests = new ArrayList<String>();
		String regex = "Running " + "(.*?)" + "\\n";
		Pattern pattern = Pattern.compile(regex,Pattern.DOTALL);
		Matcher matcher = pattern.matcher(input);
		while(matcher.find())
		{
			String fullNameTestClass = matcher.group(1);
			listTests.add(fullNameTestClass);
		}
		
		return listTests;
	}
	
	public static ArrayList<String> getFullNameFailedTest(String input, ArrayList<String> listTest)
	{
		ArrayList<String> listFailedTest = new ArrayList<String> ();
		String beginRegex = "Results\\s?:\\n+";
		String regex = beginRegex + "Failed tests: " + "(.*?)" + "\\n";
		Pattern pattern = Pattern.compile(regex,Pattern.DOTALL);
		Matcher matcher = pattern.matcher(input);
		while(matcher.find())
		{
			String strFound = matcher.group(1);
			for ( int k = 0 ; k < listTest.size() ; k++)
			{
				String fullNameTest = listTest.get(k);
				if(strFound.contains(fullNameTest))
			    {
					listFailedTest.add(fullNameTest);
			    }//end if 
				
			}//end for
			
		}//end while
		
		return listFailedTest;
		
	}//end method
	
	
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
