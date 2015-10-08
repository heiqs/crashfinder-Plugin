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


/**
 * This class contains different methods for extracting all filenames and paths in a given parent directory
 * 
 * @author Antsa Harinala Andriamboavonjy
 */
public class ExtractionFilenamePath {
    
    /**
     * 
     * @param pathToParentDir - path to root directory
     * @return mapping filename - path
     */
    public static HashMap<String,String> extractFilenamePath(String pathToParentDir)
    {
        HashMap<String,String> mapFilenameAbsPath = new HashMap<String,String>();
        
        File fileParentDir = new File(pathToParentDir);
        File[] fList = fileParentDir.listFiles();
        for (File file : fList) 
        {
            String filename = file.getName();
            String absPathFile = file.getAbsolutePath();
             if (file.isFile() && file.isHidden()== false ) 
             {
                   if(mapFilenameAbsPath.containsKey(filename) == false)
                   {
                        mapFilenameAbsPath.put(filename, absPathFile);
                   }
                
             }else if (file.isDirectory()) {
                   
                	if(mapFilenameAbsPath.containsKey(filename) == false)
                	{
                        mapFilenameAbsPath.put(filename, absPathFile);
                	}
             }
            
        }
        
        return mapFilenameAbsPath;
    }
    
    /**
     * 
     * @param parentDirectory - path to parent directory
     * @param FilenameException - Filename which is not extracted.
     * @return mapping filename - path 
     */
    public static HashMap<String,String> extractFilenamePath(String parentDirectory, String filenameException)
    //public static HashMap<String,String> collectFilenameDirExc(String pathToParentDir, String dirFilenameException)
    {
        HashMap<String,String> mapFilenameAbsPath = new HashMap<String,String>();
        
        File fileParentDir = new File(parentDirectory);
        File[] fList = fileParentDir.listFiles();
        for (File file : fList) 
        {
            String filename = file.getName();
            String absPathFile = file.getAbsolutePath();
            if(filename.equals(filenameException) == false)
            {
                if (file.isFile()) 
                {
                    if(mapFilenameAbsPath.containsKey(filename) == false)
                    {
                        mapFilenameAbsPath.put(filename, absPathFile);
                    }
                    
                } else if (file.isDirectory()) {
                   
                	if(mapFilenameAbsPath.containsKey(filename) == false)
                	{
                        mapFilenameAbsPath.put(filename, absPathFile);
                	}
                }
            }
        }
        
        return mapFilenameAbsPath;
    }
    
    /**
     * 
     * @param parentDirectory - path to parent directory 
     * @return list containing path
     */
    public static ArrayList<String> extractPath(String parentDirectory)
    {
        ArrayList<String> listAbsPath = new ArrayList<String>();
        
        File fileParentDir = new File(parentDirectory);
        File[] fList = fileParentDir.listFiles();
        for (File file : fList) 
        {
            String filename = file.getName();
            String absPathFile = file.getAbsolutePath();
             if (file.isFile() && file.isHidden()== false ) 
             {
                   if(listAbsPath.contains(absPathFile)==false)
                   {
                        listAbsPath.add(absPathFile);
                   }
                    
             }else if (file.isDirectory()) {
                    
            	 	if(listAbsPath.contains(absPathFile) == false)
                    {
                        listAbsPath.add(absPathFile);
                    }
             }
        }
        return listAbsPath;
    }
    
}
