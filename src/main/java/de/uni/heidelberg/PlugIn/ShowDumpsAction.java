package de.uni.heidelberg.PlugIn;

import hudson.model.Action;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class ShowDumpsAction implements Action {

    private final File passingDumpFile;
    private final File failingDumpFile;

    public ShowDumpsAction(File passingDumpFile, File failingDumpFile) {
        this.passingDumpFile = passingDumpFile;
        this.failingDumpFile = failingDumpFile;
    }

    public String getPassingDumpContent() {
        try {
            return FileUtils.readFileToString(passingDumpFile);
        } catch (IOException e) {
            return "Could not read passing dump file:\n" + e.getStackTrace();
        }
    }

    public String getFailingDumpContent() {
        try {
            return FileUtils.readFileToString(failingDumpFile);
        } catch (IOException e) {
            return "Could not read failing dump file:\n" + e.getStackTrace();
        }
    }

    public File getPassingDumpFile() {
        return passingDumpFile;
    }

    public File getFailingDumpFile() {
        return failingDumpFile;
    }

    @Override
    public String getIconFileName() {
        return "/images/jenkins.png";
    }

    @Override
    public String getDisplayName() {
        return "CrashFinder Results";
    }

    @Override
    public String getUrlName() {
        return "cf-results";
    }
}