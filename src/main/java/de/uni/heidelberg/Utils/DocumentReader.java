package de.uni.heidelberg.Utils;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;

/**
 * 
 * @author Antsa Harinala Andriamboavonjy
 * Created on October 2015.
 */
public class DocumentReader {

	/**
	 * 
	 * @param file - File object 
	 * @return string containing the content of the file
	 * @throws IOException
	 */
    public static String slurpFile(File file)throws IOException
    {
		return FileUtils.readFileToString(file);
    }

    public static String slurpStream(InputStream is) throws IOException {
        StringBuilder b = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            b.append(line);
        }
        return b.toString();
    }
    
    /**
     * 
     * @param file - File object
     * @return list of lines
     * @throws IOException
     */
    public static ArrayList<String> slurpFiles(File file)throws IOException
    {
        ArrayList<String> listLines = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        while(br.ready())
        {
            String line = br.readLine();
            listLines.add(line);
        }
        
        return listLines;
    }
    
}
    

