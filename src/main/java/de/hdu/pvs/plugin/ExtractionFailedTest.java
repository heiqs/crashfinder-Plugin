package de.hdu.pvs.plugin;

import hudson.model.BuildListener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Antsa Harinala Andriamboavonjy Created in October 2015
 */

public class ExtractionFailedTest {

	/**
	 * Extract class name failed test.
	 * 
	 * @param input
	 *            - input string from log file
	 * @return - string containing name of the class
	 */
	public static ArrayList<String> extractClassNameFailedTest(String input,
			BuildListener listener) {
		ArrayList<String> listTest = new ArrayList<String>();
		ArrayList<String> listFailedTest = new ArrayList<String>();

		String regexEnd = "(?=" + "\\[INFO\\]" + ")";
		String regex = "[\\-]+\\n T E S T S\\n[\\-]+(.*?)" + regexEnd;
		Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		Matcher matcher = pattern.matcher(input);

		if (matcher.find()) {

			String content = matcher.group();
			listTest = ExtractionFailedTest.extractAllTest(content, listener);
			listener.getLogger().println("Size test: " + listTest.size());
			listFailedTest = ExtractionFailedTest.getFullNameFailedTest(
					content, listTest, listener);
		}

		return listFailedTest;
	}

	/**
	 * Extract all test names from log file
	 * 
	 * @param input
	 *            - string input log
	 * @return - list containing name executed tests
	 */
	public static ArrayList<String> extractAllTest(String input,
			BuildListener listener) {
		ArrayList<String> listTests = new ArrayList<String>();
		String regex = "Running " + "(.*?)" + "\\n";
		Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		Matcher matcher = pattern.matcher(input);
		while (matcher.find()) {
			listener.getLogger().println("Found 1");
			String fullNameTestClass = matcher.group(1);
			listTests.add(fullNameTestClass);
		}

		return listTests;
	}

	/**
	 * Retrieve full name failed test from log file
	 * 
	 * @param input
	 *            - part of the log string
	 * @param listTest
	 *            - list of all tests
	 * @return
	 */
	public static ArrayList<String> getFullNameFailedTest(String input,
			ArrayList<String> listTest, BuildListener listener) {
		listener.getLogger().println("Input 3: " + input);
		ArrayList<String> listFailedTest = new ArrayList<String>();
		String beginRegex = "Results\\s?:\\n+";
		String regex = beginRegex + "(.*?)" + ":\\s*" + "(.*?)" + "\\n";

		Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		Matcher matcher = pattern.matcher(input);
		while (matcher.find()) {

			String strFound = matcher.group(2);
			for (int k = 0; k < listTest.size(); k++) {
				String fullNameTest = listTest.get(k);
				if (strFound.contains(fullNameTest)) {
					listFailedTest.add(fullNameTest);
					return listFailedTest;
				}

			}

		}

		return listFailedTest;
	}

}
