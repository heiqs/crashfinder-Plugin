package de.hdu.pvs.plugin;

import hudson.model.BuildListener;

import de.hdu.pvs.plugin.CrashFinderInput;

/**
 * 
 * @author Antsa Harinala Andriamboavonjy Created on October 2015
 * 
 */
public class CrashFinderCheckInput {

	protected CrashFinderInput inputs = new CrashFinderInput();
	protected BuildListener listener;

	public CrashFinderCheckInput(CrashFinderInput inputs, BuildListener listener) {
		this.inputs = inputs;
		this.listener = listener;
	}

	public boolean existLogPath() {
		if (this.inputs.getPathToLogPathDir().equals("") == true) {
			return false;
		} else {
			return true;
		}
	}

	public boolean existPathToJarPassing() {
		if (this.inputs.getPathToJarPassingVersion().equals("") == true) {
			return false;
		} else {
			return true;
		}
	}

	public boolean existPathToJarFailing() {
		if (this.inputs.getPathToJarFailingVersion().equals("") == true) {
			return false;
		} else {
			return true;
		}
	}

	public boolean existBehaviourPassingVersion() {

		if (this.inputs.getBehaviour() == null) {
			return false;
		} else {
			if (this.inputs.getBehaviour().equals("Number") == true) {
				if (this.inputs.getGit().equals("true") == true) {
					if (this.inputs.getGitNumberCommitBack().equals("") == true) {
						return false;
					} else {
						try {
							Integer.parseInt(this.inputs.getGitNumberCommitBack());
						} catch (NumberFormatException e) {
							return false;
						}
					}
				} else if (this.inputs.getSvn().equals("true") == true) {
					if (this.inputs.getSvnRevisionNumb().equals("") == true) {
						return false;
					} else {
						try {
							Integer.parseInt(this.inputs.getSvnRevisionNumb());
						} catch (NumberFormatException e) {
							return false;
						}
					}

				} else if (this.inputs.getGit().equals("true") == false
						&& this.inputs.getSvn().equals("true") == false) {
					return false;
				}

			} else if (this.inputs.getBehaviour().equals("CommandLine") == true) {
				if (this.inputs.getCommandCheckOutPassing().equals("") == true) {
					return false;
				}

			} else if (this.inputs.getBehaviour().equals("Filesystem") == true) {
				if (this.inputs.getPathToSrcFileSystem().equals("") == true) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean existDependencyPathsFailing() {
		if (this.inputs.getDependencyPathsFailing().equals("") == true) {
			return false;
		}
		return true;
	}

	public boolean existDependencyPathsPassing() {
		if (this.inputs.getDependencyPathsPassing().equals("") == true) {
			return false;
		}
		return true;
	}

	public boolean isMissingInformation() {
		if (this.existLogPath() == false) {
			listener.getLogger().println("Path log is missing ");
			return true;
		} else if (this.existPathToJarPassing() == false) {
			listener.getLogger().println(
					"Path to jar passing version is missing");
			return true;
		} else if (this.existPathToJarFailing() == false) {
			listener.getLogger().println(
					"Path to jar failing version is missing");
			return true;
		} else if (this.existBehaviourPassingVersion() == false) {
			listener.getLogger().println(
					"Information for getting passing version is missing");
			return true;
		}

		else if (this.existDependencyPathsPassing() == false) {
			listener.getLogger().println(
					"Path to dependencies for passing version is missing");
			return true;

		} else if (this.existDependencyPathsFailing() == false) {
			listener.getLogger().println(
					"Path to dependencies for failing version is missing");
			return true;
		}

		return false;
	}
}
