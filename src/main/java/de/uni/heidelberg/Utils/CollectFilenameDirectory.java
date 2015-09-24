/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.heidelberg.Utils;

import java.io.*;
import java.util.*;


/**
 *
 * @author antsaharinala
 */
public class CollectFilenameDirectory {
    
    /**
     * 
     * @param pathToParentDir
     * @return filename - absPathToFile
     */
    public static HashMap<String,String> collectFilenameDir(String pathToParentDir)
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
		} else if (file.isDirectory()) {
                   if(mapFilenameAbsPath.containsKey(filename) == false)
                   {
                        mapFilenameAbsPath.put(filename, absPathFile);
                   }
		}//end if
            
        }
        return mapFilenameAbsPath;
    }
    
    public static HashMap<String,String> collectFilenameDirExc(String pathToParentDir, String dirFilenameException)
    {
        HashMap<String,String> mapFilenameAbsPath = new HashMap<String,String>();
        
        File fileParentDir = new File(pathToParentDir);
        File[] fList = fileParentDir.listFiles();
        for (File file : fList) 
        {
            String filename = file.getName();
            String absPathFile = file.getAbsolutePath();
            if(filename.equals(dirFilenameException) == false)
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
		}//end if
            }
             
            
        }
        return mapFilenameAbsPath;
    }
    
    public static ArrayList<String> collectAbsPathDir(String pathToParentDir)
    {
        ArrayList<String> listAbsPath = new ArrayList<String>();
        
        File fileParentDir = new File(pathToParentDir);
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
		} else if (file.isDirectory()) {
                    if(listAbsPath.contains(absPathFile) == false)
                    {
                        listAbsPath.add(absPathFile);
                    }
		}
            
        }
        return listAbsPath;
    }
    
}
