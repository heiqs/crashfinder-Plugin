/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.heidelberg.PlugIn;

import de.uni.heidelberg.CrashFinder.JenkinsCrashFinderImplementation;
import de.uni.heidelberg.CrashFinder.JenkinsCrashFinderRunner;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Builder;
import hudson.tasks.CommandInterpreter;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import hudson.util.FormValidation;

import de.uni.heidelberg.Utils.*;
import static de.uni.heidelberg.Utils.SearchStackTrace.listf;
import hudson.model.Descriptor;
import hudson.tasks.Shell;
import java.util.Collection;

/**
 *
 * @author antsaharinala
 */
public final class CrashFinderPublisher extends Notifier {
    
        
        private final String pathToJarFailingVersion;
        
        private final String shellCheckoutPassing;
        
        private final String pathToJarPassingVersion;
        
        private final String shellRunTestPassingVersion;
        
        private final String shellRunTestFailingVersion;
        
        private final String pathToLogPathDir;
        
        
    //Fields in config.jelly must match the parameter names in the
    // "DataBoundConstructor"

    /**
     *
     * @param pathToJarFailingVersion
     * @param shellCheckoutPassing
     * @param pathToJarPassingVersion
     * @param pathToLogPathDir
     * @param shellRunTestPassingVersion
     * @param shellRunTestFailingVersion
     */
    	@DataBoundConstructor
	public CrashFinderPublisher(
                    String pathToJarFailingVersion,
                    String shellCheckoutPassing, 
                    String pathToJarPassingVersion,
                    String pathToLogPathDir,
                    String shellRunTestPassingVersion, 
                    String shellRunTestFailingVersion) 
        {
            
		this.pathToJarFailingVersion = pathToJarFailingVersion;
                
                this.pathToJarPassingVersion = pathToJarPassingVersion;
                
                this.shellCheckoutPassing = shellCheckoutPassing;
                
                this.pathToLogPathDir = pathToLogPathDir;
                
                this.shellRunTestFailingVersion = shellRunTestFailingVersion;
                
                this.shellRunTestPassingVersion = shellRunTestPassingVersion;
        }

	/**
	 * We'll use this from the <tt>config.jelly</tt>.
	 */
        
        /**
         * We'll use this from the <tt>config.jelly</tt>.
        * @return
        */
        
        public String getShellRunTestFailingVersion()
        {
            return this.shellRunTestFailingVersion;
        }
        
        public String getShellRunTestPassingVersion()
        {
            return this.shellRunTestPassingVersion;
        }
        
        public String getPathToLogPathDir()
        {
            return this.pathToLogPathDir;
        }
        
        public String getPathToJarFailingVersion() 
        {
		return pathToJarFailingVersion;
	}

        public String getShellCheckoutPassing() {
		return shellCheckoutPassing;
	}

	public String getPathToJarPassingVersion() {
		return pathToJarPassingVersion;
	}

	//public String getShellFindSeed() {
	//	return shellFindSeed;
	//}

	//public String getShellRunTest() {
	//	return shellRunTest;
	//}

	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher,
			BuildListener listener) throws IOException, InterruptedException
        {
                
            if(build.getResult().isBetterThan(Result.UNSTABLE)) {
			listener.getLogger().println(
					"CrashFinder: Build was successful, nothing to do.");
			return true;
            }else
            {
               
                String pathToWorkspace = build.getWorkspace().getRemote();
                //Firstly, clean workspace directory
                //CleanWorkspace.cleanWorkspace(pathToWorkspace);
                //String nameProject = build.getProject().getName();
                
                //1. collect existing filename of file and directory in workspace
                HashMap<String,String> mapFilenamePathFailing = CollectFilenameDirectory.collectFilenameDir(pathToWorkspace);
                Set<String> setFilenameFailing = mapFilenamePathFailing.keySet();
                        
                //2. copy copy existing file and directory in a new directory
                String failingPath = "FAILING";
                String absPathFailingDir = pathToWorkspace + "/" +  failingPath;
                Collection<String> collectionPath = mapFilenamePathFailing.values();
                ArrayList<String> listPath = new ArrayList<String>(collectionPath);
                CopyProjectToNewDirectory.copyProjectToDirectory(listPath,absPathFailingDir);
                
                //remove directory
                //RemoveFileDirectory.deleteFileDirectory(listPath);
                
                
                //3. Execute checkout new version
                CommandInterpreter runner = new Shell(this.shellCheckoutPassing);
                runner.perform(build, launcher, listener);
                 
                //4. Collect filename and abs path new file and directory except failingVersion directory
                //HashMap<String,String> mapNewFilenamePath = CollectFilenameDirectory.collectFilenameDirExc(pathToWorkspace,newDirectoryNameFailingVersion);
                HashMap<String,String> mapFilenamePathPassing = new HashMap<String,String>();
                File fileFailing = new File(absPathFailingDir);
                mapFilenamePathPassing = RetrieveFilesPassing.extractFilenamePassingProject(pathToWorkspace,fileFailing);
                
                //Create log directory
                String absPathToLogDir = GettingAbsPathFile.getAbsPath(this.pathToLogPathDir, pathToWorkspace);
                CrashFinderLogger logger = new CrashFinderLogger(absPathToLogDir);
                listener.getLogger().println("Path to log directory: " + absPathToLogDir);
                
                //Diff log
                String logDiff = "Diff";
                String absPathDiffLog = logger.createLogSubDir(logDiff);
                
                //stacktrace file
                String logStackTrace = "log-stacktrace.txt";
                String pathToStackTrace = logger.createLogFile(logStackTrace);
                
                //path to directory containing the instrumented jar 
                String logInstr = "Instrumentation";
                String pathToLogInstr = logger.createLogSubDir(logInstr);
                
                //output after executing diff command
                String absPathToDiffFile = "";
                
                String pathToLogDiffPassing = logger.createFileSubDirLog(absPathDiffLog,"log-diff-passing.txt" );
                String pathToLogDiffFailing = logger.createFileSubDirLog(absPathDiffLog, "log-diff-failing.txt");
                       
                String nameInstrJarPassing =  new File(pathToJarPassingVersion).getName().replace(".jar", "-passing-instr.jar");
                String nameInstrJarFailing =  new File(pathToJarFailingVersion).getName().replace(".jar", "-failing-instr.jar");
                       
                String pathToInstrJarPassing = logger.createFileSubDirLog(pathToLogInstr,nameInstrJarPassing);
                String pathToInstrJarFailing = logger.createFileSubDirLog(pathToLogInstr,nameInstrJarFailing);
                
                EnvVars envVarPassing = build.getEnvironment(listener);
                envVarPassing.put("INSTR_PASSING_VERSION", pathToInstrJarPassing);
                
                EnvVars envVarFailing = build.getEnvironment(listener);
                envVarFailing.put("INSTR_FAILING_VERSION", pathToInstrJarFailing);
                
                if(mapFilenamePathPassing.size() == 1 ) // case svn
                {
                    Iterator<String> iter = mapFilenamePathPassing.keySet().iterator();
                    String fileName = "";
                    while(iter.hasNext())
                    {
                        fileName = iter.next();
                    }
                    String pathToParentDirPassing = mapFilenamePathPassing.get(fileName);
                    File fileParentDirPassing = new File(pathToParentDirPassing);
                    
                    if(fileParentDirPassing.isDirectory() == true || fileParentDirPassing.exists() == true)
                    {
                       HashMap<String,String> passingFilenamePath = CollectFilenameDirectory.collectFilenameDir(pathToParentDirPassing);
                       Set<String> setFilenamePassing = passingFilenamePath.keySet();
                       
                       //build intersection
                       setFilenameFailing.retainAll(setFilenamePassing);
                       Iterator<String> iterFilename = setFilenameFailing.iterator();
                       while(iterFilename.hasNext())
                       {
                           String filename = iterFilename.next();
                           String absPathFailing = mapFilenamePathFailing.get(filename);
                           String absPathPassing = passingFilenamePath.get(filename);
                           
                           //execute diff
                           absPathToDiffFile = logger.createFileSubDirLog(absPathDiffLog, filename);
                           String command = "diff -ENwbur " + absPathPassing + " " + absPathFailing + " > " + absPathToDiffFile ;
                           listener.getLogger().println("Diff command:\n" + command);
                           CommandInterpreter runnerDiff = new Shell(command);
                           runnerDiff.perform(build, launcher, listener);
                           
                           //read content result
                           String contentResult = DocumentReader.slurpFile(new File(absPathToDiffFile));
                           if(CheckDiffSrc.isDiffSrc(contentResult)== true)
                           {
                               String strStackTrace = SearchStackTrace.searchFileStackTrace(absPathFailingDir,listener);
                               listener.getLogger().println("Stack trace:\n" + strStackTrace);
                               DocumentWriter.writeDocument(strStackTrace, new File(pathToStackTrace));
                               break;
                           }
                            
                       }
                      
                    }
                    
                }else if(mapFilenamePathPassing.isEmpty() == true) //Git
                {
                    //file and directory are overwritten after checkout
                    mapFilenamePathPassing =  mapFilenamePathFailing; 
                    mapFilenamePathFailing = CollectFilenameDirectory.collectFilenameDir(absPathFailingDir);
                    setFilenameFailing = mapFilenamePathFailing.keySet();
                   
                   Iterator<String> iterFilename = mapFilenamePathPassing.keySet().iterator();
                   while(iterFilename.hasNext())
                   {
                       String filenamePassing = iterFilename.next();
                       String absPathPassing = mapFilenamePathPassing.get(filenamePassing);
                       listener.getLogger().println("Filename Passing: " + filenamePassing);
                       if(setFilenameFailing.contains(filenamePassing)== true)
                       {
                           String absPathFailing = mapFilenamePathFailing.get(filenamePassing);
                           //execute diff
                           absPathToDiffFile = logger.createFileSubDirLog(absPathDiffLog, filenamePassing);
                           String command = "diff -ENwbur " + absPathPassing + " " + absPathFailing + " > " + absPathToDiffFile ;
                           listener.getLogger().println("Diff command:\n" + command);
                           CommandInterpreter runnerDiff = new Shell(command);
                           runnerDiff.perform(build, launcher, listener);
                           
                           //read content result
                           String contentResult = DocumentReader.slurpFile(new File(absPathToDiffFile));
                           if(CheckDiffSrc.isDiffSrc(contentResult)== true)
                           {
                               String strStackTrace = SearchStackTrace.searchFileStackTrace(absPathFailingDir,listener);
                               listener.getLogger().println("Stacktrace:\n" + strStackTrace);
                               DocumentWriter.writeDocument(strStackTrace, new File(pathToStackTrace));
                               break;
                           }
                       }
                       
                   }
                    
                }
                
                //1. execute bugLocator on passing version
                listener.getLogger().println("Executing passing version");
                JenkinsCrashFinderImplementation JenkCrashFinderPassing = new JenkinsCrashFinderImplementation(absPathToDiffFile,pathToLogDiffPassing,pathToStackTrace,pathToJarPassingVersion,pathToInstrJarPassing,listener);
                JenkinsCrashFinderRunner runnerPassing = new JenkinsCrashFinderRunner(JenkCrashFinderPassing,listener);
                runnerPassing.runner();
                       
                //execute test on instrumented jar 
                listener.getLogger().println("Executing test on passing version");
                String commandTestPassingVersion = this.shellRunTestPassingVersion.replace("$INSTR_PASSING_VERSION", envVarPassing.get("INSTR_PASSING_VERSION"));
                String commandRunTestPassing = commandTestPassingVersion;
                listener.getLogger().println("Command re-run test passing version: " + commandRunTestPassing);
                CommandInterpreter runnerTestInstrPassing = new Shell(commandRunTestPassing); 
                runnerTestInstrPassing.perform(build, launcher, listener);
                       
                //2.execute bugLocator on failing version
                listener.getLogger().println("Executing failing version");
                JenkinsCrashFinderImplementation JenkCrashFinderFailing = new JenkinsCrashFinderImplementation(absPathToDiffFile,pathToLogDiffFailing, pathToStackTrace,pathToJarFailingVersion,pathToInstrJarFailing, listener);
                JenkinsCrashFinderRunner runnerFailing = new JenkinsCrashFinderRunner(JenkCrashFinderFailing,listener);
                runnerFailing.runner();
                listener.getLogger().println("Executing test on failing version");
                String commandTestFailingVersion = this.shellRunTestFailingVersion.replace("$INSTR_FAILING_VERSION", envVarFailing.get("INSTR_FAILING_VERSION"));
                listener.getLogger().println("Command re-run test failing version: " + commandTestFailingVersion);
                CommandInterpreter runnerTestInstrFailing = new Shell(commandTestFailingVersion);
                runnerTestInstrFailing.perform(build, launcher, listener);
                
                
                return true;
            }
            
        
                
        }

        //@Override
        //public BuildStepMonitor getRequiredMonitorService() {
        //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        //}
       
           
	// Overridden for better type safety.
	// If your plugin doesn't really define any property on Descriptor,
	// you don't have to do this.
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BuildStepDescriptor getDescriptor() {
		return (BuildStepDescriptor<?>) super.getDescriptor();
	}

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return BuildStepMonitor.BUILD;
    }

	

	/**
	 * Descriptor for {@link CrashFinder}. Used as a singleton. The class is
	 * marked as public so that it can be accessed from views.
	 * 
	 * <p>
	 * See
	 * <tt>src/main/resources/hudson/plugins/hello_world/CrashFinder/*.jelly</tt>
	 * for the actual HTML fragment for the configuration screen.
	 */
	@Extension
	// This indicates to Jenkins that this is an implementation of an extension
	// point.
	public static final class DescriptorImpl extends
			BuildStepDescriptor<Publisher> {
		/**
		 * To persist global configuration information, simply store it in a
		 * field and call save().
		 * 
		 * <p>
		 * If you don't want fields to be persisted, use <tt>transient</tt>.
		 */

		/**
		 * In order to load the persisted global configuration, you have to call
		 * load() in the constructor.
		 */
		public DescriptorImpl() {
			load();
		}
                
         

		/**
		 * Performs on-the-fly validation of the form field 'name'.
		 * 
		 * @param value
		 *            This parameter receives the value that the user has typed.
		 * @return Indicates the outcome of the validation. This is sent to the
		 *         browser.
		 *         <p>
		 *         Note that returning {@link FormValidation#error(String)} does
		 *         not prevent the form from being saved. It just means that a
		 *         message will be displayed to the user.
		 */
		// public FormValidation doCheckName(@QueryParameter String value)
		// throws IOException, ServletException {
		// if (value.length() == 0)
		// return FormValidation.error("Please set a name");
		// if (value.length() < 4)
		// return FormValidation.warning("Isn't the name too short?");
		// return FormValidation.ok();
		// }

		@SuppressWarnings("rawtypes")
		public boolean isApplicable(Class<? extends AbstractProject> aClass) {
			// Indicates that this builder can be used with all kinds of project
			// types
			return true;
		}

		/**
		 * This human readable name is used in the configuration screen.
		 */
                @Override
		public String getDisplayName() {
			return "Run CrashFinder";
		}

		@Override
		public boolean configure(StaplerRequest req, JSONObject formData)
				throws Descriptor.FormException {
			// To persist global configuration information,
			// set that to properties and call save().

			// ^Can also use req.bindJSON(this, formData);
			// (easier when there are many fields; need set* methods for this,
			// like setUseFrench)
			save();
			return super.configure(req, formData);
		}

	}

}

    

