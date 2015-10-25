package de.uni.heidelberg.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 * @author Antsa Harinala Andriamboavonjy
 * Created in October 2015.
 */
public class DocumentWriter {
    
	/**
	 * 
	 * @param output - the string to be written in the file
	 * @param fileOutput 
	 * @throws IOException
	 */
    public static void  writeDocument(String output, File fileOutput) throws IOException{
		FileWriter fileWriter = new FileWriter(fileOutput);
		fileWriter.write(output);
		fileWriter.flush();
		fileWriter.close();
    }
    
}
