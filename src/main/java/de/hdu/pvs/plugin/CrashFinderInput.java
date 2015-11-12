package de.hdu.pvs.plugin;

public class CrashFinderInput {
	
	private String pathToLogPathDir = "";
	private String pathToJarFailingVersion = "";
	private String pathToJarPassingVersion = "";
	private String dependencyPathsPassing = "";
	private String dependencyPathsFailing = "";
	private String behaviour = "";
	private String git = "";
	private String svn = "";
	private String commandCheckOutPassing = "";
	private String pathToSrcFileSystem  = "";
	private String gitNumberCommitBack = "";
	private String svnRevisionNumb = "";
	private String usernameSvn = "";
	private String passwordSvn = "";
	private String usernameSvnCommand = "";
	private String passwordSvnCommand = "";
	private String stackTrace ="";
	private String pathToStackTrace ="";
	private String fullNameFailedTestClass ="";
	
	public void setPathToLogPathDir(String pathToLogPathDir)
	{
		this.pathToLogPathDir = pathToLogPathDir;
	}

	public void setPathToJarFailingVersion(String pathToJarFailingVersion)
	{
		this.pathToJarFailingVersion = pathToJarFailingVersion;
	}

	public void setPathToJarPassingVersion(String pathToJarPassingVersion)
	{
		this.pathToJarPassingVersion = pathToJarPassingVersion;
	}

	public void setDependencyPathsPassing(String dependencyPathsPassing)
	{	
		this.dependencyPathsPassing = dependencyPathsPassing;
	}

	public void setDependencyPathsFailing(String dependencyPathsFailing)
	{
		this.dependencyPathsFailing = dependencyPathsFailing;
	}

	public void setBehaviour(String behaviour)
	{
		this.behaviour = behaviour;
	}

	public void setGit(String git)
	{
		this.git = git;
	}

	public void setSvn(String svn)
	{
		this.svn = svn;
	}

	public void setCommandCheckOutPassing(String commandCheckOutPassing)
	{
		this.commandCheckOutPassing = commandCheckOutPassing;
	}

	public void setPathToSrcFileSystem(String pathToSrcFileSystem)
	{
		this.pathToSrcFileSystem = pathToSrcFileSystem;
	}

	public void setGitNumberCommitBack(String gitNumberCommitBack)
	{
		this.gitNumberCommitBack = gitNumberCommitBack;
	}

	public void setSvnRevisionNumb(String svnRevisionNumb)
	{
		this.svnRevisionNumb = svnRevisionNumb;
	}

	public void setUsernameSvn(String usernameSvn)
	{
		this.usernameSvn = usernameSvn;
	}

	public void setPasswordSvn(String passwordSvn)
	{
		this.passwordSvn = passwordSvn;
	}

	public void setUsernameSvnCommand(String usernameSvnCommand)
	{
		this.usernameSvnCommand = usernameSvnCommand;
	}

	public void setPasswordSvnCommand(String passwordSvnCommand)
	{
		this.passwordSvnCommand = passwordSvnCommand;
	}

	public void setStackTrace(String stackTrace)
	{
		this.stackTrace = stackTrace;
	}

	public void setPathToStackTrace(String pathToStackTrace)
	{
		this.pathToStackTrace = pathToStackTrace;
	}
	
	public void setFullNameFailedTestClass(String fullNameFailedTestClass)
	{
		this.fullNameFailedTestClass = fullNameFailedTestClass;
	}
		
	public String getPathToLogPathDir()
	{
		return this.pathToLogPathDir;
	}

	public String getPathToJarFailingVersion()
	{
		return this.pathToJarFailingVersion;
	}

	public String getPathToJarPassingVersion()
	{
		return this.pathToJarPassingVersion ;
	}

	public String getDependencyPathsPassing()
	{	
		return this.dependencyPathsPassing;
	}

	public String getDependencyPathsFailing()
	{
		return this.dependencyPathsFailing;
	}

	public String getBehaviour()
	{
		return this.behaviour ;
	}

	public String getGit()
	{
		return this.git;
	}

	public String getSvn()
	{
		return this.svn;
	}

	public String getCommandCheckOutPassing()
	{
		return this.commandCheckOutPassing;
	}

	public String getPathToSrcFileSystem()
	{
		return this.pathToSrcFileSystem;
	}

	public String getGitNumberCommitBack()
	{
		return this.gitNumberCommitBack ;
	}

	public String getSvnRevisionNumb()
	{
		return this.svnRevisionNumb ;
	}

	public String getUsernameSvn()
	{
		return this.usernameSvn;
	}

	public String getPasswordSvn()
	{
		return this.passwordSvn;
	}

	public String getUsernameSvnCommand()
	{
		return this.usernameSvnCommand;
	}

	public String getPasswordSvnCommand()
	{
		return this.passwordSvnCommand;
	}

	public String getStackTrace()
	{
		return this.stackTrace;
	}

	public String getPathToStackTrace()
	{
		return pathToStackTrace ;
	}
	
	public String getFullNameFailedTestClass()
	{
		return fullNameFailedTestClass;
	}
}
