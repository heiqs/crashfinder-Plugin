package de.hdu.pvs.plugin;

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Descriptor;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.CommandInterpreter;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import hudson.tasks.Shell;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import com.google.common.base.Joiner;
import com.ibm.wala.ipa.slicer.Statement;

import de.hdu.pvs.crashfinder.CrashFinderImplementation;
import de.hdu.pvs.crashfinder.CrashFinderRunnerFailing;
import de.hdu.pvs.crashfinder.CrashFinderRunnerPassing;
import de.hdu.pvs.crashfinder.analysis.Differencer;
import de.hdu.pvs.utils.DocumentReader;
import de.hdu.pvs.utils.DocumentWriter;
import de.hdu.pvs.utils.FilePathAbsolutizer;
import de.hdu.pvs.utils.Filewalker;

/**
 * 
 * @author Antsa Harinala Andriamboavonjy, Dominik Fay Created in October 2015.
 */
public final class CrashFinderPublisher extends Notifier {

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
    private final String stackTrace;
    private final String pathToStackTrace;
    private final String fullNameFailedTestClass;

    @DataBoundConstructor
    public CrashFinderPublisher(String pathToJarFailingVersion,
            String pathToJarPassingVersion, String pathToLogPathDir,
            String dependencyPathsPassing, String dependencyPathsFailing,
            String behaviour, String git, String svn,
            String commandCheckOutPassing, String pathToSrcFileSystem,
            String gitNumberCommitBack, String svnRevisionNumb,
            String usernameSvn, String passwordSvn, String usernameSvnCommand,
            String passwordSvnCommand, String stackTrace,
            String fullNameFailedTestClass, String pathToStackTrace) {
        this.pathToJarFailingVersion = pathToJarFailingVersion;
        this.pathToJarPassingVersion = pathToJarPassingVersion;
        this.pathToLogPathDir = pathToLogPathDir;
        this.dependencyPathsPassing = dependencyPathsPassing;
        this.dependencyPathsFailing = dependencyPathsFailing;
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
        this.pathToStackTrace = pathToStackTrace;
        this.fullNameFailedTestClass = fullNameFailedTestClass;
    }

    public String getDependencyPathsPassing() {
        return this.dependencyPathsPassing;
    }

    public String getDependencyPathsFailing() {
        return this.dependencyPathsFailing;
    }

    public String getPathToLogPathDir() {
        return this.pathToLogPathDir;
    }

    public String getPathToJarFailingVersion() {
        return pathToJarFailingVersion;
    }

    public String getPathToJarPassingVersion() {
        return pathToJarPassingVersion;
    }

    public String getGit() {
        return this.git;
    }

    public String getSvn() {
        return this.svn;
    }

    public String getGitNumberCommitBack() {
        return this.gitNumberCommitBack;
    }

    public String getBehaviour() {
        return this.behaviour;
    }

    public String getUsernameSvn() {
        return this.usernameSvn;
    }

    public String getPasswordSvn() {
        return this.passwordSvn;
    }

    public String getUsernameSvnCommand() {
        return this.usernameSvnCommand;
    }

    public String getPasswordSvnCommand() {
        return this.passwordSvnCommand;
    }

    public String getCommandCheckOutPassing() {
        return this.commandCheckOutPassing;
    }

    public String getPathToSrcFileSystem() {
        return this.pathToSrcFileSystem;
    }

    public String getSvnRevisionNumb() {
        return svnRevisionNumb;
    }

    public String getStackTrace() {
        return this.stackTrace;
    }

    public String getPathToStackTrace() {
        return this.pathToStackTrace;
    }

    public String getFullNameFailedTestClass() {
        return this.fullNameFailedTestClass;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher,
            BuildListener listener) throws IOException, InterruptedException {

        Result buildResult = build.getResult();
        if (buildResult == null || buildResult.isBetterThan(Result
                .UNSTABLE)) {
            listener.getLogger().println(
                    "CrashFinder: Build was successful, nothing to do.");
            return true;
        } else {
        	CrashFinderInput inputUI = new CrashFinderInput();
        	inputUI.setBehaviour(behaviour);
        	inputUI.setCommandCheckOutPassing(commandCheckOutPassing);
        	inputUI.setFullNameFailedTestClass(fullNameFailedTestClass);
        	inputUI.setDependencyPathsFailing(dependencyPathsFailing);
        	inputUI.setDependencyPathsPassing(dependencyPathsPassing);
        	inputUI.setGit(git);
        	inputUI.setGitNumberCommitBack(gitNumberCommitBack);
        	inputUI.setPasswordSvn(passwordSvn);
        	inputUI.setPasswordSvnCommand(passwordSvnCommand);
        	inputUI.setPathToJarFailingVersion(pathToJarFailingVersion);
        	inputUI.setPathToJarPassingVersion(pathToJarPassingVersion);
        	inputUI.setPathToStackTrace(pathToStackTrace);
        	inputUI.setPathToLogPathDir(pathToLogPathDir);
        	inputUI.setPathToSrcFileSystem(pathToSrcFileSystem);
        	inputUI.setStackTrace(pathToStackTrace);
        	inputUI.setSvn(passwordSvn);
        	inputUI.setSvnRevisionNumb(svnRevisionNumb);
        	inputUI.setUsernameSvn(usernameSvn);
        	inputUI.setUsernameSvnCommand(usernameSvnCommand);
        	CrashFinderCheckInput crashFinderImplException = new CrashFinderCheckInput(inputUI,listener);
        	
        	if (crashFinderImplException.isMissingInformation() == true) {
                return true;
            }

        	String pathToWorkspace = build.getWorkspace().getRemote();
            File fileWorkspace = new File(pathToWorkspace);
            FilePath filePathWorkspace = new FilePath(fileWorkspace);

            // replace rel path to abs path
            FilePathAbsolutizer absolutizer = new FilePathAbsolutizer(
                    pathToWorkspace);

            String absPathLogDir = absolutizer
                    .absolutize(this.pathToLogPathDir);
            String absPathJarPassing = absolutizer
                    .absolutize(this.pathToJarPassingVersion);
            String absPathJarFailing = absolutizer
                    .absolutize(this.pathToJarFailingVersion);
            String absPathSrcFileSystem = absolutizer
                    .absolutize(this.pathToSrcFileSystem);
            String absPathToStackTrace = absolutizer
                    .absolutize(this.pathToStackTrace);

            Collection<String> absPathsToDependenciesFailing = new HashSet<String>();
            Collection<String> absPathsToDependenciesPassing = new HashSet<String>();
            
            Filewalker jarWalkerFailing = new Filewalker(".jar");

            for (String path : dependencyPathsFailing.split(":")) {
            	String absPath = absolutizer.absolutize(path);
            	File absPathFile = new File(absPath);
            	if (absPathFile.isFile()) {
            		absPathsToDependenciesFailing.add(absPath);
            	} else {
            		absPathsToDependenciesFailing.addAll(jarWalkerFailing.walk(absPath));
            	}
            }
            
            Filewalker jarWalkerPassing = new Filewalker(".jar");
            for (String path : dependencyPathsPassing.split(":")) {
            	String absPath = absolutizer.absolutize(path);
            	File absPathFile = new File(absPath);
            	if (absPathFile.isFile()) {
            		absPathsToDependenciesPassing.add(absPath);
            	} else {
            		absPathsToDependenciesPassing.addAll(jarWalkerPassing.walk(absPath));
            	}
            }

            File logPathFile = new File(absPathLogDir);
            FileUtils.deleteDirectory(logPathFile);
            logPathFile.mkdir();
            String filenamePassing = "PASSING";
            String absPathPassingDir = pathToWorkspace + "/" + filenamePassing;
            File filePassingDir = new File(absPathPassingDir);
            FilePath filePathPassingDir = new FilePath(filePassingDir);

            // check out passing version
            CrashFinderGetPassingVersion getPassing = new CrashFinderGetPassingVersion(
                    behaviour, git, svn, commandCheckOutPassing,
                    absPathSrcFileSystem, gitNumberCommitBack, svnRevisionNumb,
                    usernameSvn, passwordSvn, usernameSvnCommand,
                    passwordSvnCommand, build, listener, launcher);
            filePathWorkspace.act(getPassing);

            String pathToRootPassing = getPassing.getPathToPassing();

            // Execute diff
            listener.getLogger()
                    .println("Execute diff command between two versions ....");
            String pathToDiffJava = pathToWorkspace + "/diff.diff";
            File fileDiffJava = new File(pathToDiffJava);
            if (fileDiffJava.exists() == false) {
            	fileDiffJava.createNewFile();
            }
            Differencer computeDiff = new Differencer();
            computeDiff.extractDiffJavaFile(pathToRootPassing, pathToWorkspace,
                    pathToDiffJava);

            CrashFinderGetStackTrace crashFinderImplStackTrace = new CrashFinderGetStackTrace(
                    this.stackTrace, absPathToStackTrace,
                    this.fullNameFailedTestClass, build, listener);

            crashFinderImplStackTrace.start();
            ArrayList<String> listPathToStackTrace = crashFinderImplStackTrace
                    .getListPathToStackTrace();
            ArrayList<String> listFullnameFailedTest = crashFinderImplStackTrace
                    .getListNameFailedTest();

            if (this.stackTrace.equals("automatically") == true) {
            	if (listPathToStackTrace.size() == 0
                        || listFullnameFailedTest.size() == 0) {
                    if (listPathToStackTrace.size() == 0) {
                        listener.getLogger()
                                .println("No path to stack trace found ....");
                    } else if (listFullnameFailedTest.size() == 0) {
                        listener.getLogger()
                                .println("No name found for failed test ....");
                    }
                    return true;
                }

            }

            for (int i = 0; i < listPathToStackTrace.size(); i++) {
                listener.getLogger().println(
                        "Path to stack trace: " + listPathToStackTrace.get(i));
            }

            for (int j = 0; j < listFullnameFailedTest.size(); j++) {
                listener.getLogger().println("Full name failed test class: "
                        + listFullnameFailedTest.get(j));
            }

            // Create log directory
            CrashFinderLog logger = new CrashFinderLog(absPathLogDir);
            listener.getLogger().println("Log path: " + absPathLogDir);

            // Diff log
            String logDiff = "Diff";
            String absPathDiffLog = logger.createSubdirectory(logDiff);
            // move diff file to Log/Diff directory
            FileUtils.moveFileToDirectory(fileDiffJava,
                    new File(absPathDiffLog), false);
            pathToDiffJava = absPathDiffLog + "/diff.diff";

            // stacktrace file
            String logStackTrace = "log-stacktrace.txt";
            String pathToStackTrace = logger.createFile(logStackTrace);
            FileUtils.copyFile(new File(listPathToStackTrace.get(0)),
                    new File(pathToStackTrace));

            // path to directory containing the instrumented jar
            String logInstr = "Instrumentation";
            String pathToLogInstr = logger.createSubdirectory(logInstr);

            // output after executing diff command
            String absPathToDiffFile = "";
            
            // Result slicing
            String logSlicing = "Slicing";
            String pathToLogSlicing = logger.createSubdirectory(logSlicing);
            String filenameSlicingPassing = "sliceDump_passing.txt";
            String filenameSlicingFailing = "sliceDump_failing.txt";
            String pathToLogSlicingPassing = logger.createFileInSubdirectory(
                    pathToLogSlicing, filenameSlicingPassing);
            String pathToLogSlicingFailing = logger.createFileInSubdirectory(
                    pathToLogSlicing, filenameSlicingFailing);

            String pathToLogDiffPassing = logger.createFileInSubdirectory(
                    absPathDiffLog, "log-diff-passing.txt");
            String pathToLogDiffFailing = logger.createFileInSubdirectory(
                    absPathDiffLog, "log-diff-failing.txt");

            String nameInstrJarPassing = new File(absPathJarPassing).getName()
                    .replace(".jar", "-passing-instr.jar");
            String nameInstrJarFailing = new File(absPathJarFailing).getName()
                    .replace(".jar", "-failing-instr.jar");

            String pathToInstrJarPassing = logger.createFileInSubdirectory(
                    pathToLogInstr, nameInstrJarPassing);
            String pathToInstrJarFailing = logger.createFileInSubdirectory(
                    pathToLogInstr, nameInstrJarFailing);

            EnvVars envVarPassing = build.getEnvironment(listener);
            envVarPassing.put("INSTR_PASSING_VERSION", pathToInstrJarPassing);

            EnvVars envVarFailing = build.getEnvironment(listener);
            envVarFailing.put("INSTR_FAILING_VERSION", pathToInstrJarFailing);

            // run buglocator on failing version
            listener.getLogger()
                    .println("Runnig buglocator on failing version");
            CrashFinderImplementation JenkCrashFinderFailing = new CrashFinderImplementation(
                    pathToDiffJava, pathToLogDiffFailing, pathToStackTrace,
                    absPathJarFailing, pathToInstrJarFailing,
                    pathToLogSlicingFailing, pathToWorkspace, listener, build,
                    launcher);

            CrashFinderRunnerFailing runnerFailing = new CrashFinderRunnerFailing(
                    JenkCrashFinderFailing, listener);
            runnerFailing.runner();
            String seed = runnerFailing.getSeed();
            Statement failingSeedStatement = runnerFailing.getSeedStatement();
            listener.getLogger().println("Seed: " + seed);
            listener.getLogger()
                    .println("Seed Statement: " + failingSeedStatement);

            // run buglocator on passing version
            listener.getLogger()
                    .println("Running buglocator on passing version");
            CrashFinderImplementation JenkinsCrashFinderPassing = new CrashFinderImplementation(
                    pathToDiffJava, pathToLogDiffPassing, pathToStackTrace,
                    absPathJarPassing, pathToInstrJarPassing,
                    pathToLogSlicingPassing, pathToWorkspace, listener, build,
                    launcher);
            CrashFinderRunnerPassing runnerPassing = new CrashFinderRunnerPassing(
                    JenkinsCrashFinderPassing, listener, seed);
            runnerPassing.runner();

            // execute test on instrumented jar passing version
            listener.getLogger()
                    .println("Downloading junit and " + "hamcrest...");
            String mavenBaseUrl = "http://repo1.maven.org/maven2/";
            String junitUrl = mavenBaseUrl + "junit/junit/4"
                    + ".12/junit-4.12.jar";
            String hamcrestUrl = mavenBaseUrl
                    + "org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar";
            File junitJarFile = new File(pathToWorkspace, "target/junit.jar");
            File hamcrestJarFile = new File(pathToWorkspace,
                    "target/hamcrest-core.jar");

            FileUtils.copyURLToFile(new URL(junitUrl), junitJarFile);
            FileUtils.copyURLToFile(new URL(hamcrestUrl), hamcrestJarFile);

            Collection<String> commonJarsPassing = new ArrayList<String>(
                    Arrays.asList(junitJarFile.getCanonicalPath(),
                            hamcrestJarFile.getCanonicalPath()));

            Collection<String> commonJarsFailing = new ArrayList<String>(
                    Arrays.asList(junitJarFile.getCanonicalPath(),
                            hamcrestJarFile.getCanonicalPath()));
            commonJarsPassing.addAll(absPathsToDependenciesPassing);
            commonJarsFailing.addAll(absPathsToDependenciesFailing);

            Collection<String> passingJars = new ArrayList<String>();
            passingJars.add(pathToInstrJarPassing);
            passingJars.addAll(commonJarsPassing);
            Collection<String> failingJars = new ArrayList<String>();
            failingJars.add(pathToInstrJarFailing);
            failingJars.addAll(commonJarsFailing);

            String absPathToJunitOutputDir = logger
                    .createSubdirectory("junit-outputs");
            String absPathToJunitOutputPassing = new File(
                    absPathToJunitOutputDir, "junit-passing.txt")
                            .getCanonicalPath();
            String absPathToJunitOutputFailing = new File(
                    absPathToJunitOutputDir, "junit-failing.txt")
                            .getCanonicalPath();

            String testClass = listFullnameFailedTest.get(0);
            listener.getLogger().println("Test class: " + testClass);
            String testCommandTemplate = "java -cp \"%s\" org.junit"
                    + ".runner.JUnitCore " + testClass + " > %s";

            // execute test on passing version
            listener.getLogger()
                    .println("Executing test on passing version .... ");
            String commandTestPassingVersion = String.format(
                    testCommandTemplate, Joiner.on(':').join(passingJars),
                    absPathToJunitOutputPassing);
            CommandInterpreter runnerTestInstrPassing = new Shell(
                    commandTestPassingVersion);
            runnerTestInstrPassing.perform(build, launcher, listener);
            listener.getLogger().println(FileUtils
                    .readFileToString(new File(absPathToJunitOutputPassing)));

            // execute test on failing version
            listener.getLogger()
                    .println("Executing test on failing version .... ");
            String commandTestFailingVersion = String.format(
                    testCommandTemplate, Joiner.on(':').join(failingJars),
                    absPathToJunitOutputFailing);
            CommandInterpreter runnerTestInstrFailing = new Shell(
                    commandTestFailingVersion);
            runnerTestInstrFailing.perform(build, launcher, listener);
            listener.getLogger().println(FileUtils
                    .readFileToString(new File(absPathToJunitOutputFailing)));

            // collect the coverage profiles from workspace directory
            String[] dumpFileNames = new File(pathToWorkspace)
                    .list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            return name.startsWith("stmts_dump_");
                        }
                    });

            Arrays.sort(dumpFileNames);

            // find the names of the profiles
            String tmpPass = dumpFileNames[dumpFileNames.length - 2];
            String tmpFail = dumpFileNames[dumpFileNames.length - 1];

            // coverage for passing
            File passingFile = new File(pathToWorkspace, tmpPass);
            listener.getLogger().println(passingFile);
            File passingProfile = new File(passingFile.getParentFile(), "coverage-passing.txt");
            passingFile.renameTo(passingProfile);
            
            // coverage for failing
            File failingFile = new File(pathToWorkspace, tmpFail);
            listener.getLogger().println(failingFile);
            File failingProfile = new File(failingFile.getParentFile(), "coverage-failing.txt");
            failingFile.renameTo(failingProfile);

            File diffCoverageFile = new File(pathToWorkspace, "diffCoverage.txt");
            ArrayList<String> diffCoverage = new ArrayList<String>();

            listener.getLogger().println("find diff coverage");
            ArrayList<String> passReader = DocumentReader
                    .slurpFiles(passingProfile);
            ArrayList<String> failReader = DocumentReader
                    .slurpFiles(failingProfile);
            boolean check = true;
            for (String fr : failReader) {
            	String frRegex = fr.split("#")[0]+fr.split("#")[1].split("\\(")[0];
                for (String pr : passReader) {
                    check = false;
                	String prRegex = pr.split("#")[0]+pr.split("#")[1].split("\\(")[0];
                    if (prRegex.equals(frRegex)) {
                        check = true;
                        break;
                    }
                }
                if (!check){
                    check = true;
                    diffCoverage.add(fr);
                    System.out.println(fr);
                }
            }
            ArrayList<String> diffCoverageLoc = new ArrayList<String>();
            File shrikeFailing = new File(pathToWorkspace,
                    "shrikeDump_failing.txt");
            File shrikePassing = new File(pathToWorkspace,
                    "shrikeDump_passing.txt");
            
            ArrayList<String> failingShrikeReader = DocumentReader
                    .slurpFiles(shrikeFailing);
            ArrayList<String> passingShrikeReader = DocumentReader
                    .slurpFiles(shrikePassing);
            for (String item: diffCoverage){
            	String classInst = item.split("\\(")[0];
            	String instIndex = item.substring(item.lastIndexOf("#") + 1).trim();
            	for (String failSh: failingShrikeReader){
            		String shIndex = failSh.split("instruction index: ")[1].split(" ", 2)[0];
            		String shClass = failSh.split(" @ ")[1].split("\\(")[0];
            		String lineNum = failSh.split("line num: ")[1].split(" @ ")[0];
            		if (classInst.equals(shClass) && instIndex.equals(shIndex)){
                        System.out.println(classInst + ":" + lineNum);
            			diffCoverageLoc.add(classInst + ":" + lineNum);
            			continue;
            		}
            	}
            }

            File diffCoverageLocFile = new File(pathToWorkspace,
                    "diffCoverageLoc.txt");

            DocumentWriter.writeArrayDocument(diffCoverage, diffCoverageFile);
            DocumentWriter.writeArrayDocument(diffCoverageLoc,
                    diffCoverageLocFile);
            // move profiles to the result directory
            build.addAction(new CrashFinderShowDumpsAction(diffCoverageLoc,
                    diffCoverage, failReader, passReader));

            FileUtils.moveFileToDirectory(passingProfile,
                    new File(absPathLogDir), false);
            FileUtils.moveFileToDirectory(failingProfile,
                    new File(absPathLogDir), false);
            FileUtils.moveFileToDirectory(diffCoverageFile,
                    new File(absPathLogDir), false);
            FileUtils.moveFileToDirectory(diffCoverageLocFile,
                    new File(absPathLogDir), false);
        }
        return true;
    }

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
        // throw new UnsupportedOperationException("Not supported yet."); //To
        // change body of generated methods, choose Tools | Templates.
        return BuildStepMonitor.BUILD;
    }

    /**
     * Descriptor for {@link CrashFinderPublisher}. Used as a singleton. The
     * class is marked as public so that it can be accessed from views.
     * 
     * <p>
     * See
     * <tt>src/main/resources/hudson/plugins/hello_world/CrashFinder/*.jelly</tt>
     * for the actual HTML fragment for the configuration screen.
     */
    @Extension
    // This indicates to Jenkins that this is an implementation of an extension
    // point.
    public static final class DescriptorImpl
            extends BuildStepDescriptor<Publisher> {
        /**
         * In order to load the persisted global configuration, you have to call
         * load() in the constructor.
         */
        public DescriptorImpl() {
            load();
        }

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
            return "Run CrashFinder 2";
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