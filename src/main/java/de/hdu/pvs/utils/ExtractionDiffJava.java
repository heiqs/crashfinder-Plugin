package de.hdu.pvs.utils;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Antsa Harinala Andriamboavonjy Created in October 2015
 */

public class ExtractionDiffJava {

	public static void extractDiffJavaFile(String strInputDiff,
			File fileOutput, String regexStartCommandDiff) throws IOException {
		String output = "";
		String regex = regexStartCommandDiff + "(.*?)" + "(?="
				+ regexStartCommandDiff + ")";
		Pattern p = Pattern.compile(regex, Pattern.DOTALL);
		Matcher m = p.matcher(strInputDiff);
		while (m.find()) {
			String strFound = m.group();

			String regexDiffJava = "diff -ENwbur " + "(.*).java (.*).java";
			Pattern pattern = Pattern.compile(regexDiffJava);
			Matcher matcher = pattern.matcher(strFound);
			if (matcher.find()) {
				output = output + strFound;
			}

		}

		DocumentWriter.writeDocument(output, fileOutput);
	}
}
