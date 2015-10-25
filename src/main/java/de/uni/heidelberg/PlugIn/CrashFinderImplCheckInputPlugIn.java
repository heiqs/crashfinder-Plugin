package de.uni.heidelberg.PlugIn;

import hudson.model.BuildListener;

/**
 * 
 * @author Antsa Harinala Andriamboavonjy
 * Created on October 2015
 *
 */
public class CrashFinderImplCheckInputPlugIn {
	
	
    private final String pathToLogPathDir;
    
    private final String pathToJarFailingVersion;
    
    private final String pathToJarPassingVersion;
    
    private final String dependencyPathsPassing;
    
    private final String dependencyPathsFailing;
    
    private final String behaviour;
    
    private final String git;
    
    private final String svn;
    
    private final String commandCheckOutPassing;
    
    private final String pathToSrcFileSystem;
    
    private final String gitNumberCommitBack;
    
    private final String svnRevisionNumb; 
   
    private final String usernameSvn;
    
    private final String passwordSvn;
    
    private final String usernameSvnCommand;
    
    private final String passwordSvnCommand;
    
    private final BuildListener listener;
    
    

	public CrashFinderImplCheckInputPlugIn(
			String pathToLogPathDir,
			String pathToJarFailingVersion,
	        String pathToJarPassingVersion,
	        //String pathToJarTest,
	        String dependencyPathsFailing,
	        String dependencyPathsPassing,
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
		//this.pathToJarTest = pathToJarTest;
		this.dependencyPathsFailing = dependencyPathsFailing;
		this.dependencyPathsPassing = dependencyPathsPassing;
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
	
	
	
	public boolean existDependencyPathsFailing()
	{
		if(this.dependencyPathsFailing.equals("") == true)
		{
			return false;
		}
		return true;
	}
	
	public boolean existDependencyPathsPassing()
	{
		if(this.dependencyPathsPassing.equals("") == true)
		{
			return false;
		}
		return true;
	}
	
	//public boolean existPathToTestJar()
	//{
	//if(this.pathToJarTest.equals("") == true)
	//	{
	//		return false;
	//	}
	//	return true;
	//}
	

	public boolean isMissingInformation()
	{
		if(this.existLogPath() == false)
		{
			listener.getLogger().println("Path log is missing ");
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
		
		else if(this.existDependencyPathsPassing() == false)
		{
			listener.getLogger().println("Path to dependencies for passing version is missing");
			return true;
		
		}else if(this.existDependencyPathsFailing() == false)
		{
			listener.getLogger().println("Path to dependencies for failing version is missing");
			return true;
		}
		
		return false;
	}
}
