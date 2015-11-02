package de.hdu.pvs.utils;

import java.io.*;
import java.util.*;

import org.apache.commons.io.FileUtils;

/**
 * 
 * @author Antsa Harinala Andriamboavonjy Created in October 2015.
 */

public class CopyHiddenFile {

	/**
	 * Extract all hidden files containing in a given directory
	 * 
	 * @param parentDirectory
	 *            - the directory where the search takes place
	 * @return - list containing all hidden files
	 */
	public static ArrayList<String> extractHiddenFile(String parentDirectory) {
		ArrayList<String> listPath = new ArrayList<String>();
		File directory = new File(parentDirectory);

		// get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isHidden() == true) {
				listPath.add(file.getAbsolutePath());
			}
		}

		return listPath;
	}

	/**
	 * Copy all hidden files found in a source directory to a given directory
	 * 
	 * @param sourceDirectory
	 *            - path to the source directory
	 * @param destinationDirectory
	 *            - path to the destination directory
	 * @throws IOException
	 */
	public static void copyHiddenFile(String sourceDirectory,
			String destinationDirectory) throws IOException {
		File fileDestination = new File(destinationDirectory);
		ArrayList<String> listHiddenFile = extractHiddenFile(sourceDirectory);
		for (int i = 0; i < listHiddenFile.size(); i++) {
			String pathHidden = listHiddenFile.get(i);
			File fileHidden = new File(pathHidden);
			if (fileHidden.isDirectory() == true) {

				FileUtils.copyDirectoryToDirectory(fileHidden, fileDestination);
			} else if (fileHidden.isFile() == true) {
				FileUtils.copyFileToDirectory(fileHidden, fileDestination);
			}

		}
	}

}
