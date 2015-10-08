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

/**
 * This class enables to copy files and directories which compose a project to other directory
 * @author Antsa Harinala Andriamboavonjy
 */
public class CopyProject {
    
	/**
	 * 
	 * @param listPath - list containing path of all files and directories to be moved.
	 * @param destinationDirectory - destination's path
	 * @return
	 * @throws IOException
	 */
	public static boolean copyProject(ArrayList<String> listPath, String destinationDirectory) throws IOException
    {
        //boolean isFinished = false;
        File fileDestinationDir = new File(destinationDirectory);
        for(int i = 0 ; i  < listPath.size() ; i++)
    	{
            String pathToFile = listPath.get(i);
            File file  = new File(pathToFile);
            if(file.isFile() && file.isHidden() == false) 
            {
                //isFinished = false;
                FileUtils.copyFileToDirectory(file, fileDestinationDir);
                
            }else if (file.isDirectory()) {
                //isFinished = false;
                FileUtils.copyDirectoryToDirectory(file, fileDestinationDir);
            }
    	}
        
        return true;
    }
}
