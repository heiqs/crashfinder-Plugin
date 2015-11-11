package de.hdu.pvs.utils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

/**
 * Utilities for FileWalker.
 */
public class Filewalkers {

    /**
     * First, absolutize the path using given asbolutizer. Then, if the path
     * points to a file, return a Collection containing just this path. If the
     * path denotes a directory, return a Collection containing the paths
     * returned by the fileWalker.
     * 
     * @param fileWalker
     *            the walker to use if the path is a directory
     * @param absolutizer
     *            the absolutizer to resolve the potentially relative path
     * @param path
     *            the path to walk if it is a directory
     * @return A Collection containing the path(s) as described above
     * @throws IOException
     *             if the path could not be successfully absolutized
     * @throws NullPointerException
     *             if any of the arguments is null
     */
    public static Collection<String> walkIfDirectory(Filewalker fileWalker,
            FilePathAbsolutizer absolutizer, String path) throws IOException {
        if (fileWalker == null || absolutizer == null || path == null)
            throw new NullPointerException("arguments must not be null");
        String absPath = absolutizer.absolutize(path);
        File absPathFile = new File(absPath);
        if (absPathFile.isFile()) {
            return Collections.singleton(absPath);
        } else {
            return fileWalker.walk(absPath);
        }
    }
}
