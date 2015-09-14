/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.heidelberg.Utils;

import java.io.*;
import java.util.*;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author antsaharinala
 */
public class CleanWorkspace {
    
    
    public static void cleanDirectoryInWorkspace(String pathToWorkspace) throws IOException
    {
        File fileWorkspace = new File(pathToWorkspace);
        if(fileWorkspace.isDirectory() == true)
        {
            FileUtils.cleanDirectory(fileWorkspace); 
        }
    }
    
    public static void cleanWorkspace(String pathToWorkspace) throws IOException
    {
        File fileWorkspace = new File(pathToWorkspace);
        if(fileWorkspace.isDirectory()==true)
        {
            FileUtils.cleanDirectory(fileWorkspace);
        }
    }
    
    
    
}
