/*
 * The MIT License
 *
 * Copyright 2015 antsaharinala.
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

import java.io.*;
import java.util.*;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author antsaharinala
 */
public class RetrieveFilesPassing {
    
    public static HashMap<String,String> extractFilenamePassingProject(String pathParentDir, File fileReference)
	{
		//result 
		HashMap<String,String> mapFilenamePath = new HashMap<String,String>();
		//extract current file and directory containing in the parent directory
		ArrayList<File> listFilesParentDir = RetrieveFilesPassing.listf(pathParentDir);
		
		for ( int i = 0 ; i < listFilesParentDir.size() ; i++ )
		{
			File file = listFilesParentDir.get(i);
			
			if(FileUtils.isFileNewer(file, fileReference))
			{
				String filename = file.getName();
				String absPathFile = file.getAbsolutePath();
				mapFilenamePath.put(filename, absPathFile);
			}
			
		}//end for 
		
		return mapFilenamePath;
	}
	
    
	public static ArrayList<File> listf(String pathToParentDir) 
	{
		ArrayList<File> listPath = new ArrayList<File>();
		File directory = new File(pathToParentDir);

		// get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList) 
		{
			listPath.add(file);
			
		}//end for
		return listPath;
	}
}
