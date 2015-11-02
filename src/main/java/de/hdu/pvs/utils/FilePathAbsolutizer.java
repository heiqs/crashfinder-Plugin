package de.hdu.pvs.utils;

import java.io.File;
import java.io.IOException;

/**
 * @author Dominik Fay Created in October 2015 Utility for resolving a
 *         potentially relative file path into an absolute one.
 */
public class FilePathAbsolutizer {

	private final String basePath;

	/**
	 * Create an absolutizer that will resolve relative paths based upon given
	 * basePath.
	 * 
	 * @param basePath
	 *            the absolute path to which relative paths will be appended.
	 * @throws IllegalArgumentException
	 *             if {@code basePath} is not absolute.
	 */
	public FilePathAbsolutizer(String basePath) {
		if (!new File(basePath).isAbsolute()) {
			throw new IllegalArgumentException("basePath is not absolute.");
		}
		this.basePath = basePath;
	}

	/**
	 * Absolutize the given file path.
	 * <p/>
	 * If {@code inputPath} is already absolute, return {@code inputPath}. Else,
	 * join basePath with inputPath.
	 * 
	 * @param inputPath
	 *            a file path to be absolutized
	 * @return the absolute form of given file path
	 */
	public String absolutize(String inputPath) throws IOException {
		File inputFile = new File(inputPath);
		if (inputFile.isAbsolute()) {
			return inputPath;
		} else {
			return new File(this.basePath, inputPath).getCanonicalPath();
		}
	}

	/**
	 * Get the file path upon which relative paths are resolved.
	 */
	public String getBasePath() {
		return basePath;
	}

}