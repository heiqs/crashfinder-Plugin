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
import static de.uni.heidelberg.Utils.ExtractionFilenamePath.*;
import org.jenkinsci.remoting.RoleChecker;

/**
 * The plugIn is able to get an old passing version of the project.
 * There are three different ways to achieve this goal:
 * 
 * 1.  By using standard software version control like Git or Subversion. By using Git for example, the user of the plugIn
 * is able to get the passing version by defining the number of back commit. In case of subVersion, the revision number 
 * the passing version is given to be checked out.

 * 2. The second option consists of simply giving a command checking out the passing version from SCM.
 * The command is the same as used in shell in Unix-operating system.
 * 
 * 3. The third option simply captures the source code of the passing version from file system. 
 * The entry is the path to the project. 
 * 
 * @author Antsa Harinala Andriamboavonjy
 */
public class CrashFinderImplPassing implements FilePath.FileCallable {
  
	//Variable specifies 
    private final String behaviour;
    
    //Variable is not null, if Git is selected by the user
    private final String git;
    
    //Variable is not null, if SubVersion is selected by the user
    private final String svn;
    
    //variable contains the command used to check out the passing version
    private final String commandCheckOutPassing;
    
    //variable contains the path to passing version
    private final String pathToSrcFileSystem;
    
    //variable contains the number commit back when using Git
    private final String gitNumberCommitBack;
    
    //variable contains the revision number in the case of subVersion
    private final String svnRevisionNumb;
    
    private AbstractBuild build;
    
    private BuildListener listener;
    
    //variable contains authentification data when using subVersion
    private String usernameSvn;
    
    //variable contains authentification data when using subVersion
    private String passwordSvn;
    
    //variable contains authentification data when doing check out of the project using shell command.
    private String usernameSvnCommand;
    
    //variable contains authentification data when doing check out of the project using shell command.
    private String passwordSvnCommand;
    
    private Launcher launcher;
    
    public CrashFinderImplPassing(String behaviour,
                            String git, String svn,
                            String commandCheckOutPassing,String pathToSrcFileSystem,
                            String gitNumberCommitBack, String svnRevisionNumb,
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
        this.gitNumberCommitBack = gitNumberCommitBack;
        this.svnRevisionNumb = svnRevisionNumb;
        this.build = build;
        this.listener = listener;
        this.launcher = launcher;
        this.usernameSvn = usernameSvn;
        this.passwordSvn = passwordSvn;
        this.usernameSvnCommand = usernameSvnCommand;
        this.passwordSvnCommand = passwordSvnCommand;
        
    }
    
    /**
     * This method 
     * @throws InterruptedException
     * @throws IOException
     */
    public void start() throws InterruptedException, IOException
    {
       this.executeCommandCheckOutPassing();
       this.executeCopySrcFromFileSystem();
       this.executeGitCheckOutWithNumberCommitBack();
       this.executeSVNCheckOutWithRevisionNumber();
    }
    
    /**
     * This method retrieves the old passing version of the project from file system.
     * @throws IOException
     */
    public void executeCopySrcFromFileSystem() throws IOException
    {
        if(this.behaviour.equals("Filesystem") == true && this.pathToSrcFileSystem != null)
        {
            String pathToWorkspace = build.getWorkspace().getRemote();
            ArrayList<String> listAbsPath = extractPath(this.pathToSrcFileSystem);
            CopyProject.copyProject(listAbsPath, pathToWorkspace);
        }
    }
    
    /**
     * This method executes the command given by the user to check out the old passing version from a scm
     * @throws InterruptedException
     */
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
    
    /**
     * This method executes check out of a given revision number of the project.
     * @throws InterruptedException
     */
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
    
    /**
     * This method executes the check out of the project which is committed before the current one's
     * @throws InterruptedException
     */
    public void executeGitCheckOutWithNumberCommitBack() throws InterruptedException
    {
        
        if(this.behaviour.equals("Number") == true && this.git.equals("true") == true && this.gitNumberCommitBack.equals("")== false)
        {
               	String command = "git checkout HEAD~1";
                CommandInterpreter runner = new Shell(command);
                int repetitions = Integer.parseInt(this.gitNumberCommitBack);
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


