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

import java.io.*;
import java.util.*;
import org.apache.commons.io.FileUtils;

import hudson.model.BuildListener;

/**
 * This class provides method to extract list of filename and their path which belong to 
 * a passing version. This is mainly useful when one of the software control version as Git or 
 * Subversion is used to get the old project.
 * @author Antsa Harinala Andriamboavonjy
 */
public class ExtractionFilenamePassing {
    
	/**
	 * 
	 * @param parentDirectory - path to the parent directory where the project is placed.
	 * @param fileReference - file reference to differentiate files or directories which belong to the project
	 * to other files which are already there before checkout of the passing version.
	 * @return mapping filename - path
	 */
    public static HashMap<String,String> extractFilenamePath(String parentDirectory, File fileReference, BuildListener listener)
	{
		//result 
		HashMap<String,String> mapFilenamePath = new HashMap<String,String>();
		ArrayList<File> listFilesParentDir = ExtractionFilenamePassing.listfiles(parentDirectory);
		
		for ( int i = 0 ; i < listFilesParentDir.size() ; i++ )
		{
			File file = listFilesParentDir.get(i);
			listener.getLogger().println("File: " + file.getAbsolutePath());
			if(FileUtils.isFileNewer(file, fileReference))
			{
				String filename = file.getName();
				String absPathFile = file.getAbsolutePath();
				mapFilenamePath.put(filename, absPathFile);
				listener.getLogger().println(filename + "is newer than reference");
			}
			
		}
		
		return mapFilenamePath;
	}
	
    /**
     * This method retrieves list of files containing in a parent directory
     * @param parentDirectory - path to the parent directory
     * @return list containing files
     */
	public static ArrayList<File> listfiles(String parentDirectory) 
	{
		ArrayList<File> listPath = new ArrayList<File>();
		File directory = new File(parentDirectory);

		// get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList) 
		{
			listPath.add(file);
			
		}//end for
		return listPath;
	}
	
	public static HashMap<String,String> collectFilenamePathPassing(String pathToWorkspace, ArrayList<String> listPathException)
	{
		HashMap<String,String> mapFilenamePathPassing = new HashMap<String,String>();
		ArrayList<File> listFiles = listfiles(pathToWorkspace);
		for(int i = 0 ; i < listFiles.size() ; i++)
		{
			File file = listFiles.get(i);
			String absPathFile = file.getAbsolutePath();
			if(listPathException.contains(absPathFile) == false)
			{
				String filename = file.getName();
				String absPathFilePassing = file.getAbsolutePath();
				
				mapFilenamePathPassing.put(filename, absPathFilePassing);
			}
		}
		
		return mapFilenamePathPassing;
	}
	
}
