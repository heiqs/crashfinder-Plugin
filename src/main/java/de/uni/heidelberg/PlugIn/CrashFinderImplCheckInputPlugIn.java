package de.uni.heidelberg.PlugIn;

import hudson.model.BuildListener;

/**
 * 
 * @author Antsa Harinala Andriamboavonjy
 *
 */
public class CrashFinderImplCheckInputPlugIn {
	
	//variable containing the path to the log directory
    private final String pathToLogPathDir;
    
    //variable containing the path to the jar of the failing version
    private final String pathToJarFailingVersion;
    
    //variable containing the path the jar of the passing version
    private final String pathToJarPassingVersion;
    
    //variable containing the command used to run test on the instrumented jar corresponding to the passing version.
    private final String pathToJarTest;
    
    //variable containing the command used to run test on the failing jar corresponding to the failing version.
    private final String dependencyPaths;
    
    //private final String SCM;
    //the content of this variable indicates the options used to get the passing version of the project.
    private final String behaviour;
    
    //If this variable is not null,then it indicates that git is used to check out the passing version of the project
    private final String git;
    
    //If this variable is not null,then it indicates that svn is used to check out the passing version of the project
    private final String svn;
    
    //This variable contains the command used as in shell to check out the passing version
    private final String commandCheckOutPassing;
    
    //Variable containing the path which points to the directory in the file system containing an older passing version
    private final String pathToSrcFileSystem;
    
    //Variable stores the number of back commit to obtain the passing version of the project
    private final String gitNumberCommitBack;
    
    //variable gives the revision number of the passing version in svn
    private final String svnRevisionNumb; 
   
    //variable contains the username to authentificate in svn
    private final String usernameSvn;
    
    //variable contains password used to authentificate in svn
    private final String passwordSvn;
    
    //variables contains username used to authentifacate in svn when using shell's command
    private final String usernameSvnCommand;
    
    //variable contains password used to authentificate in svn when using shell's command
    private final String passwordSvnCommand;
    
    private final BuildListener listener;
    
    

	public CrashFinderImplCheckInputPlugIn(
			String pathToLogPathDir,
			String pathToJarFailingVersion,
	        String pathToJarPassingVersion,
	        String pathToJarTest,
	        String dependencyPaths,
	        String behaviour,
	        String git,
	        String svn,
	        String commandCheckOutPassing,
	        String pathToSrcFileSystem,
	        String gitNumberCommitBack,
	        String svnRevisionNumb,
	        String usernameSvn,
	        String passwordSvn,
	        String usernameSvnCommand,
	        String passwordSvnCommand,
	        BuildListener listener)
	   {
		this.behaviour = behaviour;
		this.pathToJarFailingVersion = pathToJarFailingVersion;
		this.pathToJarPassingVersion = pathToJarPassingVersion;
		this.pathToLogPathDir = pathToLogPathDir;
		this.pathToJarTest = pathToJarTest;
		this.dependencyPaths = dependencyPaths;
		this.git = git;
		this.svn = svn;
		this.commandCheckOutPassing = commandCheckOutPassing;
		this.pathToSrcFileSystem = pathToSrcFileSystem;
		this.usernameSvn = usernameSvn;
		this.passwordSvn = passwordSvn;
		this.usernameSvnCommand = usernameSvnCommand;
		this.passwordSvnCommand = passwordSvnCommand;
		this.gitNumberCommitBack = gitNumberCommitBack;
		this.svnRevisionNumb = svnRevisionNumb;
		this.listener = listener;
	
	   }
	
	
        
        
	public boolean existLogPath()
	{
		if(this.pathToLogPathDir.equals("") == true)
		{
			return false;
		}else
		{
			return true;
		}
	}
	
	public boolean existPathToJarPassing()
	{
		if(this.pathToJarPassingVersion.equals("") == true)
		{
			return false;
		}else
		{
			return true;
		}
	}
	
	public boolean existPathToJarFailing()
	{
		if(this.pathToJarFailingVersion.equals("") == true)
		{
			return false;
		}else
		{
			return true;
		}
	}

	
	public boolean existBehaviourPassingVersion()
	{
		
		if(this.behaviour == null)
		{
			return false;
		}else
		{
			if(this.behaviour.equals("Number") == true)
			{
				if(this.git.equals("true") == true)
				{
					if(this.gitNumberCommitBack.equals("") == true)
					{
						return false;
					}else
                                        {
                                            try
                                            {
                                                Integer.parseInt(this.gitNumberCommitBack);
                                            }catch(NumberFormatException e)
                                            {
                                                  return false;      
                                            }
                                        }
				}else if(this.svn.equals("true") == true)
				{
					if(this.svnRevisionNumb.equals("") == true)
					{
						return false;
					}else
                                        {
                                             try
                                            {
                                                Integer.parseInt(this.svnRevisionNumb);
                                            }catch(NumberFormatException e)
                                            {
                                                  return false;      
                                            }
                                        }
					
				}else if(this.git.equals("true") == false && this.svn.equals("true") == false)
				{
					return false;
				}
				
			}else if(this.behaviour.equals("CommandLine") == true)
			{
				if(this.commandCheckOutPassing.equals("")==true)
				{
					return false;
				}
				
			}else if(this.behaviour.equals("Filesystem") == true)
			{
				if(this.pathToSrcFileSystem.equals("") == true)
				{
					return false;
				}
			}
		}
		return true;
	}
	
	
	
	public boolean existDependencyPaths()
	{
		if(this.dependencyPaths.equals("") == true)
		{
			return false;
		}
		return true;
	}
	
	public boolean existPathToTestJar()
	{
		if(this.pathToJarTest.equals("") == true)
		{
			return false;
		}
		return true;
	}
	

	public boolean isMissingInformation()
	{
		if(this.existLogPath() == false)
		{
			listener.getLogger().println("Log path is missing ");
			return true;
		}else if(this.existPathToJarPassing() == false)
		{
			listener.getLogger().println("Path to jar passing version is missing");
			return true;
		}else if(this.existPathToJarFailing() == false)
		{
			listener.getLogger().println("Path to jar failing version is missing");
			return true;
		}else if(this.existBehaviourPassingVersion() == false)
		{
			listener.getLogger().println("Information for getting passing version is missing");
			return true;
		}
		
		else if(this.existDependencyPaths() == false)
		{
			listener.getLogger().println("Path to crash finder jar is missing");
			return true;
		}else if(this.existPathToTestJar() == false)
		{
			listener.getLogger().println("Path to test jar is missing ");
			return true;
		}
		
		return false;
	}
}
