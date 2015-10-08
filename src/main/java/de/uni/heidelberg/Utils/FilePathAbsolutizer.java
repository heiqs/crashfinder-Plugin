/*
 * The MIT License
 *
 * Copyright 2015 antsaharinala.
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

import java.io.File;
import java.io.IOException;

/**
 * Utility for resolving a potentially relative file path into an absolute one.
 */
public class FilePathAbsolutizer {

    private final String basePath;

    /**
     * Create an absolutizer that will resolve relative paths based upon
     * given basePath.
     *
     * @param basePath the absolute path to which relative paths will be
     *                 appended.
     * @throws IllegalArgumentException if {@code basePath} is not absolute.
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
     * If {@code inputPath} is already absolute, return {@code inputPath}.
     * Else, join basePath with inputPath.
     *
     * @param inputPath a file path to be absolutized
     * @return the absolute form of given file path
     */
    public String absolutize(String inputPath) throws IOException {
        File inputFile = new File(inputPath);
        if (inputFile.isAbsolute()) {
            return inputPath;
        } else {
            return new File(this.basePath, inputPath)
                    .getCanonicalPath();
        }
    }

    /**
     * Get the file path upon which relative paths are resolved.
     */
    public String getBasePath() {
        return basePath;
    }

}