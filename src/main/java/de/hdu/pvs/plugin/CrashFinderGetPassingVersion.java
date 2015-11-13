package de.hdu.pvs.plugin;

import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.remoting.VirtualChannel;
import hudson.tasks.CommandInterpreter;
import hudson.tasks.Shell;

import java.io.File;
import java.io.IOException;

import de.hdu.pvs.utils.*;

import org.jenkinsci.remoting.RoleChecker;

/**
 * 
 * 
 * 
 * @author Antsa Harinala Andriamboavonjy Created on October 2015
 */
public class CrashFinderGetPassingVersion implements FilePath.FileCallable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3871547375863960372L;

	// Variable specifies
	private final String behaviour;

	// Variable is not null, if Git is selected by the user
	private final String git;

	// Variable is not null, if SubVersion is selected by the user
	private final String svn;

	// variable contains the command used to check out the passing version
	private final String commandCheckOutPassing;

	// variable contains the path to passing version
	private final String pathToSrcFileSystem;

	// variable contains the number commit back when using Git
	private final String gitNumberCommitBack;

	// variable contains the revision number in the case of subVersion
	private final String svnRevisionNumb;

	private AbstractBuild<?, ?> build;

	private BuildListener listener;

	// variable contains authentication data when using subVersion
	private String usernameSvn;

	// variable contains authentication data when using subVersion
	private String passwordSvn;

	// variable contains authentication data when doing check out of the
	// project using shell command.
	private String usernameSvnCommand;

	// variable contains authentication data when doing check out of the
	// project using shell command.
	private String passwordSvnCommand;

	private Launcher launcher;

	// directory containing the passing project
	private final String filenamePassing = "PASSING";

	// path to the directory containing the passing project
	private String pathToPassing = "";

	public CrashFinderGetPassingVersion(String behaviour, String git, String svn,
			String commandCheckOutPassing, String pathToSrcFileSystem,
			String gitNumberCommitBack, String svnRevisionNumb,
			String usernameSvn, String passwordSvn, String usernameSvnCommand,
			String passwordSvnCommand, AbstractBuild<?, ?> build,
			BuildListener listener, Launcher launcher)

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

	public String getPathToPassing() {
		return this.pathToPassing;
	}

	/**
	 * This method
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void start() throws InterruptedException, IOException {
		this.executeCommandCheckOutPassing();
		this.executeCopySrcFromFileSystem();
		this.executeGitCheckOutWithNumberCommitBack();
		this.executeSVNCheckOutWithRevisionNumber();
	}

	/**
	 * This method retrieves the old passing version of the project from file
	 * system.
	 * 
	 * @throws IOException
	 */
	public void executeCopySrcFromFileSystem() throws IOException {
		if (this.behaviour.equals("Filesystem") == true
				&& this.pathToSrcFileSystem != null) {
			/**
			 * String pathToWorkspace = build.getWorkspace().getRemote();
			 * ArrayList<String> listAbsPath =
			 * extractPath(this.pathToSrcFileSystem);
			 * CopyProject.copyProject(listAbsPath, pathToWorkspace);
			 **/
			this.pathToPassing = this.pathToSrcFileSystem;
		}
	}

	/**
	 * This method executes the command given by the user to check out an
	 * earlier version of the project.
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void executeCommandCheckOutPassing() throws InterruptedException,
			IOException {

		String pathToPassingDir = build.getWorkspace().getRemote() + "/"
				+ this.filenamePassing;
		File dirPassing = new File(pathToPassingDir);
		if (dirPassing.exists() == false) {
			dirPassing.mkdir();
		}
		FilePath filePathPassing = new FilePath(dirPassing);

		if (this.behaviour.equals("CommandLine") == true
				&& this.commandCheckOutPassing.equals("") == false) {
			String command = "";
			if (this.passwordSvnCommand == null
					|| this.usernameSvnCommand == null) {
				command = this.commandCheckOutPassing;
			} else {
				command = this.commandCheckOutPassing + " --username "
						+ this.usernameSvnCommand + " --password "
						+ this.passwordSvnCommand;
			}

			String commandCheckOut = "cd " + pathToPassingDir + "  && "
					+ command;
			filePathPassing.act(new CrashFinderCheckOutExecution(build, listener,
					launcher, commandCheckOut));
			this.pathToPassing = pathToPassingDir;
		}

	}

	/**
	 * This method checks out an earlier version of the project from subVersion.
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void executeSVNCheckOutWithRevisionNumber()
			throws InterruptedException, IOException {
		String pathToPassingDir = build.getWorkspace().getRemote() + "/"
				+ this.filenamePassing;
		File dirPassing = new File(pathToPassingDir);
		if (dirPassing.exists() == false) {
			dirPassing.mkdir();
		}
		FilePath filePathPassing = new FilePath(dirPassing);

		CopyHiddenFile.copyHiddenFile(this.build.getWorkspace().getRemote(),
				pathToPassingDir);

		if (this.behaviour.equals("Number") == true
				&& this.svn.equals("true") == true
				&& this.svnRevisionNumb.equals("") == false) {
			/**
			 * String commandSvnUpgrade = "svn upgrade"; CommandInterpreter
			 * runnerSvnUpgrade = new Shell(commandSvnUpgrade);
			 * runnerSvnUpgrade.perform(build, launcher, listener); String
			 * command = "";
			 * 
			 * if(this.usernameSvn != null && this.passwordSvn != null) {
			 * command = "svn up -r " + this.svnRevisionNumb + " --username " +
			 * this.usernameSvn + " --password " + this.passwordSvn; }else {
			 * command = "svn up -r " + this.svnRevisionNumb ; }
			 * CommandInterpreter runner = new Shell(command);
			 * runner.perform(build, launcher, listener);
			 * runnerSvnUpgrade.perform(build, launcher, listener);
			 **/

			String commandSvnUpgrade = "svn upgrade";
			String command = "";

			if (this.usernameSvn != null && this.passwordSvn != null) {
				command = "svn up -r " + this.svnRevisionNumb + " --username "
						+ this.usernameSvn + " --password " + this.passwordSvn;
			} else {
				command = "svn up -r " + this.svnRevisionNumb;
			}
			String commandSVN = "cd " + pathToPassingDir + " && "
					+ commandSvnUpgrade + " && " + command;// + " && " +
			// commandSvnUpgrade;

			filePathPassing.act(new CrashFinderCheckOutExecution(build, listener,
					launcher, commandSVN));
			this.pathToPassing = pathToPassingDir;
		}
	}

	/**
	 * Method check out an older version of the project with git.
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void executeGitCheckOutWithNumberCommitBack()
			throws InterruptedException, IOException {
		String pathToPassingDir = build.getWorkspace().getRemote() + "/"
				+ this.filenamePassing;
		File dirPassing = new File(pathToPassingDir);
		if (dirPassing.exists() == false) {
			dirPassing.mkdir();
		}
		CopyHiddenFile.copyHiddenFile(this.build.getWorkspace().getRemote(),
				pathToPassingDir);

		if (this.behaviour.equals("Number") == true
				&& this.git.equals("true") == true
				&& this.gitNumberCommitBack.equals("") == false) {
			String command = "git stash && git checkout HEAD~1";
			String commandGit = "cd " + pathToPassingDir + " && " + command;
			CommandInterpreter runner = new Shell(commandGit);
			int repetitions = Integer.parseInt(this.gitNumberCommitBack);
			for (int i = 0; i < repetitions; i++) {
				runner.perform(build, launcher, listener);
			}

			this.pathToPassing = pathToPassingDir;
		}

	}

	@Override
	public Object invoke(File file, VirtualChannel vc) throws IOException,
			InterruptedException {
		// throw new UnsupportedOperationException("Not supported yet."); //To
		// change body of generated methods, choose Tools | Templates.
		start();
		return null;
	}

	@Override
	public void checkRoles(RoleChecker rc) throws SecurityException {
		throw new UnsupportedOperationException("Not supported yet."); // To
																		// change
																		// body
																		// of
																		// generated
																		// methods,
																		// choose
																		// Tools
																		// |
																		// Templates.
	}

}
