package de.hdu.pvs.plugin;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hudson.model.BuildListener;
import com.google.common.io.Files;

import de.hdu.pvs.utils.DetectionXMLHTML;
import de.hdu.pvs.utils.DocumentReader;

/**
 * 
 * @author Antsa Harinala Andriamboavonjy Created in October 2015
 */
public class CrashFinderSearchStackTrace {

	/**
	 * List all files containing in a directory
	 * 
	 * @param parentDirectory
	 * @param fileResult
	 */
	public static void listf(String parentDirectory, ArrayList<File> fileResult) {
		File directory = new File(parentDirectory);
		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isFile() == true && file.isHidden() == false) {
				String extensionFile = Files.getFileExtension(file
						.getAbsolutePath());
				if (extensionFile.trim().equals("java") == false
						&& extensionFile.trim().equals("html") == false
						&& extensionFile.trim().equals("xml") == false
						&& extensionFile.trim().equals("sh") == false
						&& extensionFile.trim().equals("png") == false
						&& extensionFile.trim().equals("jpg") == false) {
					fileResult.add(file);
				}

			} else if (file.isDirectory() == true && file.isHidden() == false) {
				listf(file.getAbsolutePath(), fileResult);
			}
		}
	}

	/**
	 * Check whether a file is stack trace.
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static boolean isFileStackTrace(File file) throws IOException {
		boolean isStackTrace = false;
		String fileContent = DocumentReader.slurpFile(file);
		boolean isHTMLXML = DetectionXMLHTML.isHtml(fileContent);
		if (isHTMLXML == false) {
			String regexText = "([a-zA-Z\\p{Punct}]+)Exception(:.*)?(\\n\\t?at.*)";
			Pattern pattern = Pattern.compile(regexText);
			Matcher matcher = pattern.matcher(fileContent);
			while (matcher.find()) {
				isStackTrace = true;
			}
		}

		return isStackTrace;

	}

	/**
	 * Search stack trace file
	 * 
	 * @param parentDirectory
	 *            - path to the parent directory
	 * @param listener
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<String> searchFileStackTrace(String parentDirectory)
			throws IOException {
		ArrayList<String> listPathToStackTrace = new ArrayList<String>();
		ArrayList<File> listFiles = new ArrayList<File>();
		CrashFinderSearchStackTrace.listf(parentDirectory, listFiles);
		File file = null;
		// String strStackTrace = "";
		for (int i = 0; i < listFiles.size(); i++) {
			File f = listFiles.get(i);
			boolean value = isFileStackTrace(f);
			if (value == true) {
				file = f;
				String absPathFile = file.getAbsolutePath();
				listPathToStackTrace.add(absPathFile);
				return listPathToStackTrace;
			}

		}

		return listPathToStackTrace;

	}

	/**
	 * Retrieve parent directory where stack trace file is placed.
	 * 
	 * @param pathToLog
	 *            - path to log of the last failed build
	 * @return path to the parent directory containing the stack trace
	 * @throws IOException
	 */
	public static String searchPathToDirectoryStackTrace(String pathToLog)
			throws IOException {
		String pathToStackTraceDir = "";
		File fileLog = new File(pathToLog);
		String strLog = DocumentReader.slurpFile(fileLog);

		String regex = "\\[INFO\\]\\s+Surefire report directory:" + "(.*)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(strLog);
		while (matcher.find()) {
			pathToStackTraceDir = matcher.group(1).trim();
		}
		if (pathToStackTraceDir.equals("")) {
			throw new RuntimeException("Regex not matched.");
		}
		return pathToStackTraceDir;
	}

	/**
	 * Search path to directory containing stack trace
	 * 
	 * @param logInputStream
	 * @param listener
	 * @return
	 * @throws IOException
	 */
	public static String searchPathToDirectoryStackTrace(
			InputStream logInputStream, BuildListener listener)
			throws IOException {
		String pathToStackTraceDir = "";
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				logInputStream));
		for (String line = reader.readLine(); line != null; line = reader
				.readLine()) {
			System.out.println("Line: " + line);
			String regex = "\\[INFO\\]\\s+Surefire report directory:" + "(.*)";
			System.out.println("Regex: " + regex);
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
				listener.getLogger().println("Regex found");
				pathToStackTraceDir = matcher.group(1).trim();
				break;
			}
		}
		if (pathToStackTraceDir.equals("")) {
			throw new RuntimeException("Regex not matched.");
		}
		return pathToStackTraceDir;
	}

	/**
	 * Search for the path to the directory where the stack trace is placed from
	 * content log file.
	 * 
	 * @param contentLog
	 *            - content of log file.
	 * @return
	 */
	public static String searchStackTraceContent(String contentLog) {

		String pathToStackTraceDir = null;
		String regex = "\\[INFO\\]\\s+Surefire report directory:" + "(.*)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(contentLog);
		while (matcher.find()) {
			pathToStackTraceDir = matcher.group(1).trim();
		}

		if (pathToStackTraceDir == null) {
			throw new RuntimeException("Regex not matched.");
		}

		return pathToStackTraceDir;
	}
}
