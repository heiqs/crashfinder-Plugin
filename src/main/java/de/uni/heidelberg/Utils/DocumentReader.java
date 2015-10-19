/*
 * The MIT License
 *
 * Copyright 2015 Antsa Harinala Andriamboavonjy.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package de.uni.heidelberg.Utils;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;

/**
 * This class contains methods that read the content of a file in order to process it later.
 * @author Antsa Harinala Andriamboavonjy
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
     * @return list containing line string
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
    

