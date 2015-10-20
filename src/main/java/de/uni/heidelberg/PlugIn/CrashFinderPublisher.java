/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.heidelberg.PlugIn;

import com.google.common.base.Joiner;
import com.ibm.wala.ipa.slicer.Statement;
import de.uni.heidelberg.CrashFinder.JenkinsCrashFinderImplementation;
//import de.uni.heidelberg.CrashFinder.JenkinsCrashFinderRunner;
import de.uni.heidelberg.CrashFinder.JenkinsCrashFinderRunnerFailing;
import de.uni.heidelberg.CrashFinder.JenkinsCrashFinderRunnerPassing;
import de.uni.heidelberg.Utils.*;
import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.*;
import hudson.tasks.*;
import hudson.util.FormValidation;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 *
 * @author antsaharinala
 */
public final class CrashFinderPublisher extends Notifier {
    
        
        private final String pathToLogPathDir;
        
        private final String pathToJarFailingVersion;
        
        private final String pathToJarPassingVersion;
        
        private final String pathToTestsJar;
        
        private final String pathToCrashFinderJar;
        
        //private final String SCM;
         
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
        
        private final String stackTrace; 
        
        private final String pathToStackTrace;
        
        private final String fullNameFailedTestClass; //fullNameFailedTestClass
        
        
        
       

        //private final String numOfCommitsToGoBack;


    //Fields in config.jelly must match the parameter names in the
    // "DataBoundConstructor"


    /**
     *
     * @param pathToJarFailingVersion
     * 
     * @param pathToJarPassingVersion
     * @param pathToLogPathDir
     */
        
         
    	@DataBoundConstructor
	public CrashFinderPublisher(
			
                    String pathToJarFailingVersion,
                    String pathToJarPassingVersion,
                    String pathToLogPathDir,
                    String pathToTestsJar,
                    String pathToCrashFinderJar,
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
                    String stackTrace,
                    String fullNameFailedTestClass,
                    String pathToStackTrace)
                  
        {

    			this.pathToJarFailingVersion = pathToJarFailingVersion;
                
                this.pathToJarPassingVersion = pathToJarPassingVersion;
                
                this.pathToLogPathDir = pathToLogPathDir;

                this.pathToTestsJar = pathToTestsJar;
                
                this.pathToCrashFinderJar = pathToCrashFinderJar;
                
                this.svn = svn;
                
                this.git = git;
                
                this.gitNumberCommitBack = gitNumberCommitBack;
                
                this.behaviour = behaviour;
                
                this.svnRevisionNumb = svnRevisionNumb;
                
                this.usernameSvn = usernameSvn;
                
                this.passwordSvn = passwordSvn;
                
                this.usernameSvnCommand = usernameSvnCommand;
                
                this.passwordSvnCommand = passwordSvnCommand;
                
                this.commandCheckOutPassing = commandCheckOutPassing;
                
                this.pathToSrcFileSystem = pathToSrcFileSystem;
                
                this.stackTrace = stackTrace;
                
                this .pathToStackTrace = pathToStackTrace;
                
                this.fullNameFailedTestClass = fullNameFailedTestClass;
                		
        }

	/**
	 * We'll use this from the <tt>config.jelly</tt>.
	 */
        
        /**
         * We'll use this from the <tt>config.jelly</tt>.
        * @return
        */
        
        public String getPathToTestsJar()
        {
            return this.pathToTestsJar;
        }
        
        public String getPathToCrashFinderJar()
        {
            return this.pathToCrashFinderJar;
        }
        
        public String getPathToLogPathDir()
        {
            return this.pathToLogPathDir;
        }
        
        public String getPathToJarFailingVersion() 
        {
        	return pathToJarFailingVersion;
        }

        public String getPathToJarPassingVersion() {
        	return pathToJarPassingVersion;
        }
        
        //public String getPathToStackTrace()
        //{
        //	return pathToStackTrace;
        //}
        
        //public String getSCM()
        //{
        //    return this.SCM;
        //}
        
        public String getGit()
        {
            return this.git;
        }
        
        public String getSvn()
        {
            return this.svn;
        }
        
        public String getGitNumberCommitBack()
        {
            return this.gitNumberCommitBack;
        }
        
        public String getBehaviour()
        {
            return this.behaviour;
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
        
        public String getCommandCheckOutPassing()
        {
            return this.commandCheckOutPassing;
        }
        
        public String getPathToSrcFileSystem()
        {
            return this.pathToSrcFileSystem;
        }

        public String getSvnRevisionNumb() {
            return svnRevisionNumb;
        }

    /**
        public String getAutomateStackTrace()
        {
        	return this.automateStackTrace;
        }
        
        public String getManuallyStackTrace()
        {
        	return this.manuallyStackTrace;
        }
        **/
        
        public String getStackTrace()
        {
        	return this.stackTrace;
        }
                
        public String getPathToStackTrace()
        {
        	return this.pathToStackTrace;
        }
        
        public String getFullNameFailedTestClass()
        {
        	return this.fullNameFailedTestClass;
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
             
            	CrashFinderImplCheckInputPlugIn crashFinderImplException = new CrashFinderImplCheckInputPlugIn(
                  		pathToLogPathDir,
              			pathToJarFailingVersion,
              	        pathToJarPassingVersion,
              	        this.pathToTestsJar,
              	        this.pathToCrashFinderJar,
              	        behaviour,
              	        git,
              	        svn,
              	        commandCheckOutPassing,
              	        pathToSrcFileSystem,
              	        gitNumberCommitBack,
              	        svnRevisionNumb,
              	        usernameSvn,
              	        passwordSvn,
              	        usernameSvnCommand,
              	        passwordSvnCommand,
              	        listener);
                  if(crashFinderImplException.isMissingInformation() == true)
                  {
                  	return true;
                  }
                
                String pathToWorkspace = build.getWorkspace().getRemote();
                File fileWorkspace = new File(pathToWorkspace);
                FilePath filePathWorkspace = new FilePath(fileWorkspace);
                
                
                //replace rel path to abs path
                FilePathAbsolutizer absolutizer = new FilePathAbsolutizer
                        (pathToWorkspace);
                
                String absPathLogDir = absolutizer.absolutize(this.pathToLogPathDir);
                String absPathJarPassing = absolutizer.absolutize(this.pathToJarPassingVersion);
                String absPathJarFailing = absolutizer.absolutize(this.pathToJarFailingVersion);
                String absPathSrcFileSystem = absolutizer.absolutize(this.pathToSrcFileSystem);
                String absPathToCrashFinderJar = absolutizer.absolutize(this.pathToCrashFinderJar);
                String absPathToTestsJar = absolutizer.absolutize(this.pathToTestsJar);

            
                File logPathFile = new File(absPathLogDir);
                FileUtils.deleteDirectory(logPathFile);
                logPathFile.mkdir();

                String filenamePassing = "PASSING";
                String absPathPassingDir = pathToWorkspace + "/" + filenamePassing;
                File filePassingDir = new File(absPathPassingDir);
                FilePath filePathPassingDir = new FilePath(filePassingDir); 
                
                //check out passing version
                CrashFinderImplGetPassing getPassing = new CrashFinderImplGetPassing
                (
                				behaviour,
                				git, svn,
                				commandCheckOutPassing, absPathSrcFileSystem,
                				gitNumberCommitBack, svnRevisionNumb,
                				usernameSvn, passwordSvn,
                				usernameSvnCommand, passwordSvnCommand,
                				build, listener,
                				launcher);
                filePathWorkspace.act(getPassing);
                
                String pathToRootPassing = getPassing.getPathToPassing();
                
                //Execute diff
                listener.getLogger().println("Execute diff");
                
                String pathToDiffOutput = pathToWorkspace + "/tmp-diff.diff";
                File fileDiffOutput = new File(pathToDiffOutput);
                
                String regexStartCommandDiff = "diff -ENwbur"; ;
                String commandDiff = regexStartCommandDiff + " --exclude=\"" + "PASSING\" " + pathToRootPassing + " " + pathToWorkspace + " > " + pathToWorkspace + "/tmp-diff.diff";
                //String commandDiff = "diff -ENwbur --exclude=\"" + "PASSING\" " +  pathToRootPassing + " " + pathToWorkspace + " > " + pathToWorkspace + "/diff.diff";
                
                CommandInterpreter runner = new Shell(commandDiff); 
                runner.perform(build, launcher, listener);
                
                String pathToDiffJava = pathToWorkspace + "/diff.diff";
                File fileDiffJava = new File(pathToDiffJava);
                if(fileDiffJava.exists() == false)
                {
                	fileDiffJava.createNewFile();
                }
                
                //Only extract diff java
                String strDiffOutput = DocumentReader.slurpFile(fileDiffOutput);
                ExtractionDiffJava.extractDiffJavaFile(strDiffOutput, fileDiffJava, regexStartCommandDiff);
                
                //delete tmp-diff
                fileDiffOutput.delete();
                //delete passing version, not needed anymore
                FileUtils.deleteDirectory(new File(absPathPassingDir));
                
                CrashFinderImplGetStackTrace crashFinderImplStackTrace = 
                		new CrashFinderImplGetStackTrace
                		(
                			this.stackTrace,
                			this.pathToStackTrace,
                			this.fullNameFailedTestClass,
							build,
							listener);
                
                crashFinderImplStackTrace.start();
                ArrayList<String> listPathToStackTrace = crashFinderImplStackTrace.getListPathToStackTrace();
                ArrayList<String> listFullnameFailedTest = crashFinderImplStackTrace.getListNameFailedTest();
                
                for(int i = 0 ; i < listPathToStackTrace.size(); i++)
                {
                	listener.getLogger().println("Path to stack trace: " + listPathToStackTrace.get(i));
                }
                
                
                for (int j = 0 ; j < listFullnameFailedTest.size() ; j++)
                {
                	listener.getLogger().println("Full name failed test class: " + listFullnameFailedTest.get(j));
                }
                
                
                //Create log directory
                CrashFinderImplLog logger = new CrashFinderImplLog(absPathLogDir);
                listener.getLogger().println("Log path: " + absPathLogDir);
                
                //Diff log
                String logDiff = "Diff";
                String absPathDiffLog = logger.createSubdirectory(logDiff);
                //move diff file to Log/Diff directory
                FileUtils.moveFileToDirectory(fileDiffJava, new File(absPathDiffLog),false);
                pathToDiffJava = absPathDiffLog + "/diff.diff";

                //stacktrace file
                String logStackTrace = "log-stacktrace.txt";
                String pathToStackTrace = logger.createFile(logStackTrace);
                FileUtils.copyFile(new File(listPathToStackTrace.get(0)), new
                        File(pathToStackTrace));

                //path to directory containing the instrumented jar
                String logInstr = "Instrumentation";
                String pathToLogInstr = logger.createSubdirectory(logInstr);

                //output after executing diff command
                String absPathToDiffFile = "";
                 
                //Result slicing
                String logSlicing = "Slicing";
                String pathToLogSlicing = logger.createSubdirectory(logSlicing);
                String filenameSlicingPassing = "dumpslice_passing.txt";
                String filenameSlicingFailing = "dumpslice_failing.txt";
                String pathToLogSlicingPassing = logger.createFileInSubdirectory(pathToLogSlicing,filenameSlicingPassing );
                String pathToLogSlicingFailing = logger.createFileInSubdirectory(pathToLogSlicing, filenameSlicingFailing);

                String pathToLogDiffPassing = logger.createFileInSubdirectory(absPathDiffLog,"log-diff-passing.txt" );
                String pathToLogDiffFailing = logger.createFileInSubdirectory(absPathDiffLog, "log-diff-failing.txt");

                //String nameInstrJarPassing =  new File(pathToJarPassingVersion).getName().replace(".jar", "-passing-instr.jar");
                //String nameInstrJarFailing =  new File(pathToJarFailingVersion).getName().replace(".jar", "-failing-instr.jar");
                String nameInstrJarPassing = new File(absPathJarPassing).getName().replace(".jar", "-passing-instr.jar");
                String nameInstrJarFailing = new File(absPathJarFailing).getName().replace(".jar", "-failing-instr.jar");

                String pathToInstrJarPassing = logger.createFileInSubdirectory(pathToLogInstr,nameInstrJarPassing);
                String pathToInstrJarFailing = logger.createFileInSubdirectory(pathToLogInstr,nameInstrJarFailing);

               EnvVars envVarPassing = build.getEnvironment(listener);
               envVarPassing.put("INSTR_PASSING_VERSION", pathToInstrJarPassing);

               EnvVars envVarFailing = build.getEnvironment(listener);
               envVarFailing.put("INSTR_FAILING_VERSION", pathToInstrJarFailing);
                
         
                //run buglocator on failing version
               listener.getLogger().println("Runnig buglocator on failing version");
               JenkinsCrashFinderImplementation JenkCrashFinderFailing = new
                       JenkinsCrashFinderImplementation(pathToDiffJava,
                    		   							pathToLogDiffFailing, 
                    		   							pathToStackTrace,
                    		   							absPathJarFailing,
                    		   							pathToInstrJarFailing,
                    		   							pathToLogSlicingFailing,
                    		   							pathToWorkspace, 
                    		   							listener,
                    		   							build, 
                    		   							launcher);
               
               JenkinsCrashFinderRunnerFailing runnerFailing = new JenkinsCrashFinderRunnerFailing(JenkCrashFinderFailing,listener);
               runnerFailing.runner();
               String seed = runnerFailing.getSeed();
                Statement failingSeedStatement = runnerFailing
                        .getSeedStatement();
               listener.getLogger().println("Seed: " + seed);
               
               
               //run buglocator on passing version
               listener.getLogger().println("Running buglocator on passing version");
               JenkinsCrashFinderImplementation JenkinsCrashFinderPassing = new JenkinsCrashFinderImplementation(pathToDiffJava,
            		   																							 pathToLogDiffPassing, 
            		   																							 pathToStackTrace,
            		   																							 absPathJarPassing,
            		   																							 pathToInstrJarPassing,
            		   																							 pathToLogSlicingPassing,
            		   																							 pathToWorkspace,
            		   																							 listener,
            		   																							 build,
            		   																							 launcher);
               	JenkinsCrashFinderRunnerPassing runnerPassing = new
                        JenkinsCrashFinderRunnerPassing
                        (JenkinsCrashFinderPassing,listener,seed);
                runnerPassing.runner();
                
                //execute test on instrumented jar passing version
                listener.getLogger().println("Downloading junit and " +
                        "hamcrest...");
                String mavenBaseUrl = "http://repo1.maven.org/maven2/";
                String junitUrl = mavenBaseUrl + "junit/junit/4" +
                				  ".12/junit-4.12.jar";
                String hamcrestUrl = mavenBaseUrl +
                					"org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar";
                File junitJarFile = new File(pathToWorkspace, "target/junit.jar");
                File hamcrestJarFile = new File(pathToWorkspace,
                								"target/hamcrest-core.jar");
                listener.getLogger().println("Downloading junit and hamcrest");
                FileUtils.copyURLToFile(new URL(junitUrl), junitJarFile);
                FileUtils.copyURLToFile(new URL(hamcrestUrl), hamcrestJarFile);

                Collection<String> commonJars = Arrays.asList(
                		absPathToCrashFinderJar,
                		absPathToTestsJar,
                		junitJarFile.getCanonicalPath(),
                		hamcrestJarFile.getCanonicalPath()
                		);
                Collection<String> passingJars = new ArrayList<String>(commonJars);
                passingJars.add(pathToInstrJarPassing);
                Collection<String> failingJars = new ArrayList<String>(commonJars);
                failingJars.add(pathToInstrJarFailing);

                //change
                //String testClass = CrashFinderImplSearchStackTrace.extractTestClass(new File(pathToStackTrace));
                String testClass = listFullnameFailedTest.get(0);
                listener.getLogger().println("Test class: " + testClass);
                String testCommandTemplate = "java -cp \"%s\" org.junit" +
                ".runner.JUnitCore " + testClass;
                
                //execute test on passing version
                listener.getLogger().println("Executing test on passing " +
                        "version");
                String commandTestPassingVersion = String.format
                        (testCommandTemplate, Joiner.on(':').join
                                (passingJars));
                listener.getLogger().println("Command re-run test passing " +
                        "version: " + commandTestPassingVersion);
                CommandInterpreter runnerTestInstrPassing = new Shell
                        (commandTestPassingVersion);
                runnerTestInstrPassing.perform(build, launcher, listener);

                //execute test on failing version
                listener.getLogger().println("Executing test on failing version");
                String commandTestFailingVersion = String.format
                        (testCommandTemplate, Joiner.on(':').join
                                (failingJars));
                listener.getLogger().println("Command re-run test failing version: " + commandTestFailingVersion);
                CommandInterpreter runnerTestInstrFailing = new Shell(commandTestFailingVersion);
                runnerTestInstrFailing.perform(build, launcher, listener);

                String[] dumpFileNames = new File(pathToWorkspace).list
                        (new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.startsWith("stmts_dump_");
                    }
                });
                Arrays.sort(dumpFileNames);
                File passingDumpFile = new File(pathToWorkspace,
                        dumpFileNames[0]);
                File failingDumpFile = new File(pathToWorkspace,
                        dumpFileNames[1]);
                build.addAction(new ShowDumpsAction(
                        FileUtils.readFileToString(passingDumpFile),
                        FileUtils.readFileToString(failingDumpFile)
                ));
                FileUtils.moveFileToDirectory(passingDumpFile,
                        new File(absPathLogDir), false);
                FileUtils.moveFileToDirectory(failingDumpFile,
                        new File(absPathLogDir), false);

                //} catch(Exception e) {
                    //FileUtils.deleteDirectory(new File(absPathFailingDir));
                    //throw new RuntimeException(e);
                //}
               
               
            }


            return true;
             
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

    
    
