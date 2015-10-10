package de.uni.heidelberg.Utils;

import java.io.*;
import java.util.*;

import org.apache.commons.io.FileUtils;

public class CopyHiddenFile {
	
	public static ArrayList<String> extractHiddenFile(String parentDirectory) 
	{
		ArrayList<String> listPath = new ArrayList<String>();
		File directory = new File(parentDirectory);

		// get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList) 
		{
			if(file.isHidden() == true)
			{
				listPath.add(file.getAbsolutePath());
			}
		}
		
		return listPath;
	}
	
	public static void copyHiddenFile(String sourceDirectory, String destinationDirectory) throws IOException
	{
		File fileDestination = new File(destinationDirectory);
		ArrayList<String> listHiddenFile = extractHiddenFile(sourceDirectory);
		for ( int i =  0 ; i < listHiddenFile.size() ; i++)
		{
			String pathHidden = listHiddenFile.get(i);
			File fileHidden = new File(pathHidden);
			if(fileHidden.isDirectory() == true)
			{
				
				FileUtils.copyDirectoryToDirectory(fileHidden, fileDestination);
			}else if(fileHidden.isFile() == true)
			{
				FileUtils.copyFileToDirectory(fileHidden, fileDestination);
			}
			
		}
	}

}
