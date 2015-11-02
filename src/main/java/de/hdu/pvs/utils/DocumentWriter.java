package de.hdu.pvs.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * 
 * @author Antsa Harinala Andriamboavonjy Created in October 2015.
 */
public class DocumentWriter {

	/**
	 * 
	 * @param output
	 *            - the string to be written in the file
	 * @param fileOutput
	 * @throws IOException
	 */
	public static void writeDocument(String output, File fileOutput)
			throws IOException {
		FileWriter fileWriter = new FileWriter(fileOutput);
		fileWriter.write(output);
		fileWriter.flush();
		fileWriter.close();
	}
	
	public static void writeArrayDocument(ArrayList<String> output, File fileOutput)
			throws IOException {
	PrintWriter fileWriter = new PrintWriter(fileOutput); 
	for(String str: output) {
		fileWriter.println(str);
	}
	fileWriter.flush();
	fileWriter.close();
	}

}
