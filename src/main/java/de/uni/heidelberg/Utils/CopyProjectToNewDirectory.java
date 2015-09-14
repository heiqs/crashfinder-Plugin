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
public class CopyProjectToNewDirectory {
    
   
    
    public static boolean copyProjectToDirectory(ArrayList<String> listPathFiles, String pathToDestinationDir) throws IOException
    {
        boolean isFinished = false;
        File fileDestinationDir = new File(pathToDestinationDir);
        for(int i = 0 ; i  < listPathFiles.size() ; i++)
    	{
            String pathToFile = listPathFiles.get(i);
            File file  = new File(pathToFile);
            if(file.isFile() && file.isHidden() == false) 
            {
                isFinished = false;
                FileUtils.copyFileToDirectory(file, fileDestinationDir);
                
            }else if (file.isDirectory()) {
                isFinished = false;
		FileUtils.copyDirectoryToDirectory(file, fileDestinationDir);
            }
	}
        return true;
    }
}
