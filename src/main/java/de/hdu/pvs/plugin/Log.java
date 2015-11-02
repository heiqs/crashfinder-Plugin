package de.hdu.pvs.plugin;

import hudson.FilePath;
import java.io.File;
import java.io.IOException;

/**
 * 
 * @author Antsa Harinala Andriamboavonjy Created in October 2015
 * 
 */

public class Log {

	private String absPathToLogDir;

	private File fileLog;

	private FilePath filePathLog;

	public Log(String absPathToLogDir) throws IOException, InterruptedException {
		this.absPathToLogDir = absPathToLogDir;
		this.fileLog = new File(absPathToLogDir);
		this.filePathLog = new FilePath(fileLog);
		if (filePathLog.isDirectory() == false) {
			filePathLog.mkdirs();
		}
	}

	/**
	 * 
	 * @return path to log file, where the intermediate results of the
	 *         crashFinder steps are stored.
	 */
	public String getPathToLogDir() {
		return this.absPathToLogDir;
	}

	public void setPathToLogDir(String pathToLogDir) {
		this.absPathToLogDir = pathToLogDir;
	}

	/**
	 * Create a directory insides the log directory
	 * 
	 * @param dirName
	 *            - Name of the directory
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String createSubdirectory(String dirName) throws IOException,
			InterruptedException {
		String absPathSubDir = this.absPathToLogDir + "/" + dirName;
		File fileSubDir = new File(absPathSubDir);
		FilePath filePathSubDir = new FilePath(fileSubDir);
		if (filePathSubDir.isDirectory() == false) {
			filePathSubDir.mkdirs();
		}

		return absPathSubDir;
	}

	/**
	 * 
	 * Create a directory inside a directory
	 * 
	 * @param pathToParentDir
	 *            - absolute path to the parent directory
	 * @param dirName
	 *            - name of the new directory
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String createDirectoryInsideSubDirectory(String pathToParentDir,
			String dirName) throws IOException, InterruptedException {
		String absPathSubSubDir = pathToParentDir + "/" + dirName;
		File fileSubSubDir = new File(absPathSubSubDir);
		FilePath filePathSubSubDir = new FilePath(fileSubSubDir);
		if (filePathSubSubDir.isDirectory() == false) {
			filePathSubSubDir.mkdirs();
		}
		return absPathSubSubDir;

	}

	/**
	 * Create a file in a directory
	 * 
	 * @param filename
	 *            - The name of the file
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String createFile(String filename) throws IOException,
			InterruptedException {
		// boolean isCreatedFile = false;
		String absPathLogFile = this.absPathToLogDir + "/" + filename;
		File fileLogFile = new File(absPathLogFile);
		FilePath filePathLogFile = new FilePath(fileLogFile);
		if (filePathLogFile.exists() == false) {
			filePathLogFile.touch(1);
		}
		return absPathLogFile;
	}

	/**
	 * 
	 * @param pathToSubDir
	 *            - absolute path to subdirectory
	 * @param filename
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String createFileInSubdirectory(String pathToSubDir, String filename)
			throws IOException, InterruptedException {
		String absPathToSubDirFile = pathToSubDir + "/" + filename;
		File fileSubDirFile = new File(absPathToSubDirFile);
		FilePath filePathSubDirFile = new FilePath(fileSubDirFile);
		if (filePathSubDirFile.exists() == false) {
			filePathSubDirFile.touch(2);
		}

		return absPathToSubDirFile;
	}

}
