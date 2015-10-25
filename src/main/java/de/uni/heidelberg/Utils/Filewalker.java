package de.uni.heidelberg.Utils;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author Dominik Fay
 * Created in October 2015
 */

public class Filewalker {

    private final String suffix;

    public Filewalker(String suffix) {
        this.suffix = suffix;
    }

    public Set<File> walk(File dir) {
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException(dir + " is not a directory");
        }

        Set<File> files = new HashSet<File>();
        File[] list = dir.listFiles();
        for (File f : list) {
            if (f.isDirectory()) {
                files.addAll(walk(f));
            } else if (f.getName().endsWith(suffix)) {
                files.add(f);
            }
        }
        return files;
    }

    public Set<String> walk(String dir) {
        Set<String> files = new HashSet<String>();
        for (File f : walk(new File(dir))) {
            files.add(f.getAbsolutePath());
        }
        return files;
    }
}
