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

package de.uni.heidelberg.PlugIn;

import hudson.FilePath;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Antsa Harinala Andriamboavonjy
 * This class is mainly used to automatically create file and directory inside the directory given by the user,
 * where all intermediate results should be stored.
 */
public class CrashFinderImplLog {
    
	/**
	 * Variable containing path to the directory where the intermediate result are stored. 
	 * This is the root of the log folder.
	 * The path should not end with /
	 */
	private String absPathToLogDir;
    
    //Variable containing File object corresponding to the variable absPathToLogDir
    private File fileLog;
    
    //Variable containing FilePath object corresponding to the variable absPathToLogDir
    private FilePath filePathLog;
    
    /**
     * Constructor for the CrashFinderLogger
     * @param absPathToLogDir - path to the log directory containing the intermediate result
     * @throws IOException
     * @throws InterruptedException
     */
    public CrashFinderImplLog(String absPathToLogDir) throws IOException, InterruptedException
    {
        this.absPathToLogDir = absPathToLogDir;
        this.fileLog = new File(absPathToLogDir);
        this.filePathLog = new FilePath(fileLog);
        if(filePathLog.isDirectory() == false)
        {
            filePathLog.mkdirs();
        }
     }
    
    /**
     * 
     * @return Path to the root of the log directory containing the intermediate results
     */
    public String getPathToLogDir()
    {
        return this.absPathToLogDir;
    }
    
    /**
     * Set a new value to the path pointing to the log directory
     * @param pathToLogDir
     * 
     */
    public void setPathToLogDir(String pathToLogDir)
    {
        this.absPathToLogDir = pathToLogDir;
    }
    
    /**
     * Create a subdirectory inside log directory
     * @param dirName - the name of the to generated directory
     * @return path to the new created subdirectory
     * @throws IOException
     * @throws InterruptedException
     */
    public String createSubdirectory(String dirName) throws IOException, InterruptedException
    {
       String absPathSubDir = this.absPathToLogDir + "/" + dirName;
       File fileSubDir = new File(absPathSubDir);
       FilePath filePathSubDir = new FilePath(fileSubDir);
       if(filePathSubDir.isDirectory() == false)
       {
           filePathSubDir.mkdirs();
       }
      
       return absPathSubDir;
    }
    
    /**
     * This method creates a directory inside a subdirectory if it doesn't already exist. 
     * @param pathToParentDir - absolute path to a subdirectory.
     * @return absolute path to the subdirectory.
     * @throws IOException
     * @throws InterruptedException
     */
    public String  createDirectoryInsideSubDirectory(String pathToParentDir, String dirName) throws IOException, InterruptedException
    {
    	String absPathSubSubDir = pathToParentDir + "/" + dirName;
        File fileSubSubDir = new File(absPathSubSubDir);
        FilePath filePathSubSubDir = new FilePath(fileSubSubDir);
        if(filePathSubSubDir.isDirectory() == false)
        {
            filePathSubSubDir.mkdirs();
        }
        return absPathSubSubDir;
        
    }
    
    /**
     * This method creates a file inside the root directory if it doesn't already exist
     * @param filename - the name of the file to be created
     * @return absolute path to the file
     * @throws IOException
     * @throws InterruptedException
     */
    public String createFile(String filename) throws IOException, InterruptedException
    {
        //boolean isCreatedFile = false;
        String absPathLogFile = this.absPathToLogDir + "/" + filename;
        File fileLogFile = new File(absPathLogFile);
        FilePath filePathLogFile = new FilePath(fileLogFile);
        if(filePathLogFile.exists() == false)
        {
            filePathLogFile.touch(1);
        }
        return absPathLogFile;
    }
    
    /**
     * This method creates a file inside a subdirectory, if it doesn't already exist.
     * @param pathToSubDir - path to the subdirectory
     * @param filename - the name of the file to be created
     * @return - absolute path to file
     * @throws IOException
     * @throws InterruptedException
     */
    public String createFileInSubdirectory(String pathToSubDir, String filename) throws IOException, InterruptedException
    {
    	String absPathToSubDirFile = pathToSubDir + "/" + filename;
        File fileSubDirFile = new File(absPathToSubDirFile);
        FilePath filePathSubDirFile = new FilePath(fileSubDirFile);
        if(filePathSubDirFile.exists() == false)
        {
            filePathSubDirFile.touch(2);
        }
        
        return absPathToSubDirFile;
    }
    
    
            
}
