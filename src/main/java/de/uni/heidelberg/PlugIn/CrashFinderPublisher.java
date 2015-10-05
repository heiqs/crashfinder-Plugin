/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.heidelberg.PlugIn;

import com.google.common.base.Joiner;
import de.uni.heidelberg.CrashFinder.JenkinsCrashFinderImplementation;
import de.uni.heidelberg.CrashFinder.JenkinsCrashFinderRunner;
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
                    //String SCM,
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
                    String passwordSvnCommand ) 
        {

		this.pathToJarFailingVersion = pathToJarFailingVersion;
                
                this.pathToJarPassingVersion = pathToJarPassingVersion;
                
                this.pathToLogPathDir = pathToLogPathDir;

                this.pathToTestsJar = pathToTestsJar;
                
                this.pathToCrashFinderJar = pathToCrashFinderJar;
                
                //this.SCM = SCM;
                
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
        
        //public String getSCM()
        //{
        //    return this.SCM;
        //}
        
        public String getGit()
        {
            return this.git;
        }
        
        public String getSVN()
        {
            return this.svn;
        }
        
        public String getGitNumberCommit()
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

                
                listener.getLogger().println("Abs path dir: " + absPathLogDir);
                listener.getLogger().println("Abs path jar passing: " + absPathJarPassing);
                listener.getLogger().println("Abs path jar failing: " + absPathJarFailing);
                
                //1. collect existing filename of file and directory in workspace
                HashMap<String,String> mapFilenamePathFailing = CollectFilenameDirectory.collectFilenameDir(pathToWorkspace);
                Set<String> setFilenameFailing = mapFilenamePathFailing.keySet();
                        
                //2. copy copy existing file and directory in a new directory
                String failingPath = "FAILING";
                String absPathFailingDir = pathToWorkspace + "/" +  failingPath;
                Collection<String> collectionPath = mapFilenamePathFailing.values();
                ArrayList<String> listPath = new ArrayList<String>(collectionPath);
                CopyProjectToNewDirectory.copyProjectToDirectory(listPath,absPathFailingDir); // file only copied
                
                try {

                    //3. checkout older version project
                    CrashFinderGettingOlderVersion gettingOldProject = new CrashFinderGettingOlderVersion(
                                                                        behaviour,
                                                                        git, svn,
                                                                        commandCheckOutPassing, absPathSrcFileSystem,
                                                                        gitNumberCommitBack, svnRevisionNumb,
                                                                        usernameSvn, passwordSvn,
                                                                        usernameSvnCommand, passwordSvnCommand,
                                                                        build, listener,
                                                                        launcher);

                    filePathWorkspace.act(gettingOldProject);


                    /**
                    CommandInterpreter runner = new Shell(finalCheckoutCmd);
                    int repetitions = Integer.parseInt(numOfCommitsToGoBack);
                    for (int i = 0; i < repetitions; i++) {
                        runner.perform(build, launcher, listener);
                    }**/


                    //4. Collect filename and abs path new file and directory except failingVersion directory
                    //HashMap<String,String> mapNewFilenamePath = CollectFilenameDirectory.collectFilenameDirExc(pathToWorkspace,newDirectoryNameFailingVersion);
                    HashMap<String,String> mapFilenamePathPassing = new HashMap<String,String>();
                    File fileFailing = new File(absPathFailingDir);
                    mapFilenamePathPassing = CollectFilenameDirectory
                            .collectFilenameDir(pathToWorkspace);

                    //Create log directory
                    //String absPathToLogDir = absolutizer.absolutize(this.pathToLogPathDir);
                    CrashFinderLogger logger = new CrashFinderLogger(absPathLogDir);
                    listener.getLogger().println("Path to log directory: " + absPathLogDir);

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

                    //String nameInstrJarPassing =  new File(pathToJarPassingVersion).getName().replace(".jar", "-passing-instr.jar");
                    //String nameInstrJarFailing =  new File(pathToJarFailingVersion).getName().replace(".jar", "-failing-instr.jar");
                    String nameInstrJarPassing = new File(absPathJarPassing).getName().replace(".jar", "-passsing-instr.jar");
                    String nameInstrJarFailing = new File(absPathJarFailing).getName().replace(".jar", "-failing-instr.jar");

                    String pathToInstrJarPassing = logger.createFileSubDirLog(pathToLogInstr,nameInstrJarPassing);
                    String pathToInstrJarFailing = logger.createFileSubDirLog(pathToLogInstr,nameInstrJarFailing);

                    EnvVars envVarPassing = build.getEnvironment(listener);
                    envVarPassing.put("INSTR_PASSING_VERSION", pathToInstrJarPassing);

                    EnvVars envVarFailing = build.getEnvironment(listener);
                    envVarFailing.put("INSTR_FAILING_VERSION", pathToInstrJarFailing);




                    /**
                    if(mapFilenamePathPassing.size() == 1 ) // case svn
                    {
                        listener.getLogger().println("SCM SVN");
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
                                   listener.getLogger().println("Size stack trace: " + strStackTrace.split("\n").length);
                                   listener.getLogger().println("Stack trace:\n" + strStackTrace);
                                   DocumentWriter.writeDocument(strStackTrace, new File(pathToStackTrace));
                                   break;
                               }

                           }

                        }

                    }else if(mapFilenamePathPassing >  1 ) //Git
                    {

                        listener.getLogger().println("SCM Git");
                    **/
                        //file and directory are overwritten after checkout
                        //mapFilenamePathPassing =  mapFilenamePathFailing;

                    //Passing

                    // Iterator<String> iter1 = mapFilenamePathPassing.keySet().iterator();
                    // while(iter1.hasNext())
                    // {
                    //    String filename =  iter1.next();
                    //    String absPath = mapFilenamePathPassing.get(filename);
                    //      listener.getLogger().println("Filename passing: " + filename);
                    //        listener.getLogger().println("Absolute path passing: " + absPath);
                    //}

                    //Failing
                    mapFilenamePathFailing = CollectFilenameDirectory.collectFilenameDir(absPathFailingDir);
                    listener.getLogger().println("mapFilenamePathFailing: " +
                            mapFilenamePathFailing.entrySet());
                    setFilenameFailing = mapFilenamePathFailing.keySet();

                        //Iterator<String> iter = mapFilenamePathFailing.keySet().iterator();
                        //while(iter.hasNext())
                        //{
                        //    String filename = iter.next();
                        //    String absPath = mapFilenamePathFailing.get(filename);
                        //    listener.getLogger().println("Failing filename: " + filename);
                        //    listener.getLogger().println("Failing absolut path: " + absPath);
                        //}




                    Iterator<String> iterFilename = mapFilenamePathPassing.keySet().iterator();
                    listener.getLogger().println("mapFilenamePathPassing: " +
                            mapFilenamePathPassing.entrySet());

                    String absPathToFinalDiffFile = logger.createFileSubDirLog
                            (absPathDiffLog, "diff.diff");
                    Collection<String> diffFiles = new ArrayList<String>();

                    while(iterFilename.hasNext())
                    {
                        String filenamePassing = iterFilename.next();
                        String absPathPassing = mapFilenamePathPassing.get(filenamePassing);

                        if(setFilenameFailing.contains(filenamePassing)== true)
                        {
                               String absPathFailing = mapFilenamePathFailing.get(filenamePassing);
                               //execute diff
                               absPathToDiffFile = logger.createFileSubDirLog(absPathDiffLog, filenamePassing);
                               String command = "diff -ENwbur " + absPathPassing + " " + absPathFailing + " > " + absPathToDiffFile ;
                               listener.getLogger().println("Diff command:\n" + command);
                               CommandInterpreter runnerDiff = new Shell(command);
                               runnerDiff.perform(build, launcher, listener);

                            diffFiles.add(absPathToDiffFile);

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

                    if (diffFiles.isEmpty()) {
                        listener.getLogger().println("No Diff between old and new" +
                                " version. Nothing to compare! Exiting...");
                        FileUtils.deleteDirectory(new File(absPathFailingDir));
                        return true;
                    } else {
                        for (String pathToDiffFile : diffFiles) {
                            new Shell("cat " + pathToDiffFile + " >> " +
                                    absPathToFinalDiffFile).perform(build,
                                    launcher, listener);
                        }
                    }

                    //1. execute bugLocator on passing version
                    listener.getLogger().println("Executing passing version");
                    //JenkinsCrashFinderImplementation JenkCrashFinderPassing = new JenkinsCrashFinderImplementation(absPathToDiffFile,pathToLogDiffPassing,pathToStackTrace,pathToJarPassingVersion,pathToInstrJarPassing,listener);
                    JenkinsCrashFinderImplementation JenkCrashFinderPassing = new
                            JenkinsCrashFinderImplementation(absPathToFinalDiffFile,
                            pathToLogDiffPassing,pathToStackTrace,
                            absPathJarPassing,pathToInstrJarPassing,
                            pathToWorkspace, listener);
                    JenkinsCrashFinderRunner runnerPassing = new JenkinsCrashFinderRunner(JenkCrashFinderPassing,listener);
                    runnerPassing.runner();

                    listener.getLogger().println("Downloading junit and " +
                                    "hamcrest...");
                    String mavenBaseUrl = "http://repo1.maven.org/maven2/";
                    String junitUrl = mavenBaseUrl + "junit/junit/4" +
                            ".12/junit-4.12.jar";
                    String hamcrestUrl = mavenBaseUrl +
                            "org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar";
                    File junitJarFile = new File(pathToWorkspace,
                            "target/junit.jar");
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
                    Collection<String> passingJars = new ArrayList<String>
                            (commonJars);
                    passingJars.add(pathToInstrJarPassing);
                    Collection<String> failingJars = new ArrayList<String>
                            (commonJars);
                    failingJars.add(pathToInstrJarFailing);

                    String testClass = SearchStackTrace.extractTestClass
                            (new File(pathToStackTrace));
                    String testCommandTemplate = "java -cp \"%s\" org.junit" +
                            ".runner.JUnitCore " + testClass;

                    //execute test on instrumented jar
                    listener.getLogger().println("Executing test on passing version");
                    String commandTestPassingVersion = String.format
                            (testCommandTemplate, Joiner.on(':').join(passingJars));
                    String commandRunTestPassing = commandTestPassingVersion;
                    listener.getLogger().println("Command re-run test passing version: " + commandRunTestPassing);
                    CommandInterpreter runnerTestInstrPassing = new Shell(commandRunTestPassing);
                    runnerTestInstrPassing.perform(build, launcher, listener);

                    //2.execute bugLocator on failing version
                    listener.getLogger().println("Executing failing version");
                    JenkinsCrashFinderImplementation JenkCrashFinderFailing = new
                            JenkinsCrashFinderImplementation
                            (absPathToFinalDiffFile,pathToLogDiffFailing,
                                    pathToStackTrace,absPathJarFailing,
                                    pathToInstrJarFailing, absPathFailingDir,
                                    listener);
                    JenkinsCrashFinderRunner runnerFailing = new JenkinsCrashFinderRunner(JenkCrashFinderFailing,listener);
                    runnerFailing.runner();
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
                    build.addAction(new ShowDumpsAction(passingDumpFile,
                            failingDumpFile));

                    FileUtils.deleteDirectory(new File(absPathFailingDir));
                } catch (Exception e) {
                    FileUtils.deleteDirectory(new File(absPathFailingDir));
                    throw new RuntimeException(e);
                }
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

    
    

