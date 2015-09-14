/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.heidelberg.PlugIn;

import hudson.FilePath;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author antsaharinala
 */
public class CrashFinderLogger {
    
    private String absPathToLogDir; //not end by /
    
    private File fileLog;
    
    private FilePath filePathLog;
    
    public CrashFinderLogger(String absPathToLogDir) throws IOException, InterruptedException
    {
        this.absPathToLogDir = absPathToLogDir;
        this.fileLog = new File(absPathToLogDir);
        this.filePathLog = new FilePath(fileLog);
        if(filePathLog.isDirectory() == false)
        {
            filePathLog.mkdirs();
        }
        
    }
    
    public String getPathToLogDir()
    {
        return this.absPathToLogDir;
    }
    
    public void setPathToLogDir(String pathToLogDir)
    {
        this.absPathToLogDir = pathToLogDir;
    }
    
    public String createLogSubDir(String dirName) throws IOException, InterruptedException
    {
       String absPathSubDir = this.absPathToLogDir + "/" + dirName;
       File fileSubDir = new File(absPathSubDir);
       FilePath filePathSubDir = new FilePath(fileSubDir);
       if(filePathSubDir.isDirectory() == false)
       {
           filePathSubDir.mkdirs();
           //isDirCreated = true;
       }
       //return isDirCreated ;
       return absPathSubDir;
    }
    
    public String  createLogSubSubDir(String pathToParentDir, String dirName) throws IOException, InterruptedException
    {
        //boolean isDirCreated = false;
        
        String absPathSubSubDir = pathToParentDir + "/" + dirName;
        File fileSubSubDir = new File(absPathSubSubDir);
        FilePath filePathSubSubDir = new FilePath(fileSubSubDir);
        if(filePathSubSubDir.isDirectory() == false)
        {
            filePathSubSubDir.mkdirs();
            //gisDirCreated = true;
        }
        return absPathSubSubDir;
        
    }
    
    public String createLogFile(String filename) throws IOException, InterruptedException
    {
        //boolean isCreatedFile = false;
        String absPathLogFile = this.absPathToLogDir + "/" + filename;
        File fileLogFile = new File(absPathLogFile);
        FilePath filePathLogFile = new FilePath(fileLogFile);
        if(filePathLogFile.exists() == false)
        {
            filePathLogFile.touch(1);
            //isCreatedFile = true;
        }
        
        //return isCreatedFile;
        return absPathLogFile;
    }
    
    public String createFileSubDirLog(String pathToSubDir, String filename) throws IOException, InterruptedException
    {
        //boolean isCreatedFile = false;
        String absPathToSubDirFile = pathToSubDir + "/" + filename;
        File fileSubDirFile = new File(absPathToSubDirFile);
        FilePath filePathSubDirFile = new FilePath(fileSubDirFile);
        if(filePathSubDirFile.exists() == false)
        {
            filePathSubDirFile.touch(2);
            //isCreatedFile = true;
        }
        //return isCreatedFile;
        return absPathToSubDirFile;
    }
    
    
            
}
