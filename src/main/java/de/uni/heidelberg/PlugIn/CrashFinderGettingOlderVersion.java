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
package de.uni.heidelberg.PlugIn;

import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.remoting.VirtualChannel;
import hudson.tasks.CommandInterpreter;
import hudson.tasks.Shell;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.uni.heidelberg.Utils.*;
import static de.uni.heidelberg.Utils.CollectFilenameDirectory.collectAbsPathDir;
import org.jenkinsci.remoting.RoleChecker;

/**
 *
 * @author antsaharinala
 */
public class CrashFinderGettingOlderVersion implements FilePath.FileCallable {
  
    private final String behaviour;
    
    private final String git;
    
    private final String svn;
    
    private final String commandCheckOutPassing;
    
    private final String pathToSrcFileSystem;
    
    private final String gitNumberCommit;
    
    private final String svnRevisionNumb;
    
    private AbstractBuild build;
    
    private BuildListener listener;
    
    private String usernameSvn;
    
    private String passwordSvn;
    
    private String usernameSvnCommand;
    
    private String passwordSvnCommand;
    
    private Launcher launcher;
    
    public CrashFinderGettingOlderVersion(String behaviour,
                            String git, String svn,
                            String commandCheckOutPassing,String pathToSrcFileSystem,
                            String gitNumberCommit, String svnRevisionNumb,
                            String usernameSvn, String passwordSvn,
                            String usernameSvnCommand, String passwordSvnCommand,
                            AbstractBuild build, BuildListener listener,
                            Launcher launcher)
            
    {
        this.behaviour = behaviour;
        this.git = git;
        this.commandCheckOutPassing = commandCheckOutPassing;
        this.pathToSrcFileSystem = pathToSrcFileSystem;
        this.svn = svn;
        this.gitNumberCommit = gitNumberCommit;
        this.svnRevisionNumb = svnRevisionNumb;
        this.build = build;
        this.listener = listener;
        this.launcher = launcher;
        this.usernameSvn = usernameSvn;
        this.passwordSvn = passwordSvn;
        this.usernameSvnCommand = usernameSvnCommand;
        this.passwordSvnCommand = passwordSvnCommand;
        
    }
    
    public void start() throws InterruptedException, IOException
    {
       this.executeCommandCheckOutPassing();
       this.executeCopySrcFromFileSystem();
       this.executeGitCheckOutWithNumberCommitBack();
       this.executeSVNCheckOutWithRevisionNumber();
        
    }
    
    public void executeCopySrcFromFileSystem() throws IOException
    {
        if(this.behaviour.equals("Filesystem") == true && this.pathToSrcFileSystem != null)
        {
            String pathToWorkspace = build.getWorkspace().getRemote();
            ArrayList<String> listAbsPath = collectAbsPathDir(this.pathToSrcFileSystem);
            CopyProjectToNewDirectory.copyProjectToDirectory(listAbsPath, pathToWorkspace);
        }
    }
    
    public void executeCommandCheckOutPassing() throws InterruptedException
    {
        
        if(this.behaviour.equals("CommandLine") == true && this.commandCheckOutPassing.equals("")==false)
        {
            String command = "";
            if(this.passwordSvnCommand == null || this.usernameSvnCommand == null)
            {
                 command = this.commandCheckOutPassing;
            }else
            {
                command = this.commandCheckOutPassing + " --username " + this.usernameSvnCommand + " --password " + this.passwordSvnCommand;
            }
            
            CommandInterpreter runner = new Shell(command);
            runner.perform(build, launcher, listener);
        }
    }
    
    public void executeSVNCheckOutWithRevisionNumber() throws InterruptedException
    {
       
        if(this.behaviour.equals("Number") == true && this.svn.equals("true")==true && this.svnRevisionNumb.equals("")==false)
        {
            
            String commandSvnUpgrade =  "svn upgrade";
            CommandInterpreter runnerSvnUpgrade = new Shell(commandSvnUpgrade);
            runnerSvnUpgrade.perform(build, launcher, listener);
            String command = "";
            
            if(this.usernameSvn != null && this.passwordSvn != null )
            {
               
                command = "svn up -r " + this.svnRevisionNumb + " --username " + this.usernameSvn + " --password " + this.passwordSvn;
            }else
            {
              command = "svn up -r " + this.svnRevisionNumb ;
            } 
            CommandInterpreter runner = new Shell(command);
            runner.perform(build, launcher, listener);
            runnerSvnUpgrade.perform(build, launcher, listener);
            
        }
    }
    
    public void executeGitCheckOutWithNumberCommitBack() throws InterruptedException
    {
        
        if(this.behaviour.equals("Number") == true && this.git.equals("true") == true && this.gitNumberCommit.equals("")== false)
        {
                listener.getLogger().println("Git miditra ato");
                String command = "git checkout HEAD~1";
                CommandInterpreter runner = new Shell(command);
                int repetitions = Integer.parseInt(this.gitNumberCommit);
                for (int i = 0; i < repetitions; i++) 
                {
                    runner.perform(build, launcher, listener);
                }
        }
    }                                                                                                       
    
   
    @Override
    public Object invoke(File file, VirtualChannel vc) throws IOException, InterruptedException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        start();
        return null;
    }

    @Override
    public void checkRoles(RoleChecker rc) throws SecurityException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

