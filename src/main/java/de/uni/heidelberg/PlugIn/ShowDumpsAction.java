package de.uni.heidelberg.PlugIn;

import hudson.model.Action;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * 
 * @author Dominik Fay
 * created in October 2015.
 */

public class ShowDumpsAction implements Action {

    private final String passingDumpFile;
    private final String failingDumpFile;

    public ShowDumpsAction(String passingDumpFile, String failingDumpFile) {
        this.passingDumpFile = passingDumpFile;
        this.failingDumpFile = failingDumpFile;
    }

    public String getPassingDumpContent() {
        return passingDumpFile;
    }

    public String getFailingDumpContent() {
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
