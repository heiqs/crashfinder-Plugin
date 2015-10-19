package de.uni.heidelberg.CrashFinder;

import com.ibm.wala.ipa.slicer.Statement;
import de.hdu.pvs.crashfinder.analysis.Slicing;
import de.hdu.pvs.crashfinder.util.WALAUtils;
import de.uni.heidelberg.Utils.PackageExtractor;
import hudson.model.BuildListener;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JenkinsCrashFinderRunnerFailing implements CrashFinderRunner{
	
		private final JenkinsCrashFinderImplementation crashFinderImpl;
	    
	    private BuildListener listener;
	    
	    private String seed = null;

	    private Statement seedStatement = null;
	    
	    public JenkinsCrashFinderRunnerFailing(JenkinsCrashFinderImplementation crashFinderImpl,BuildListener listener)
	    {
	        this.crashFinderImpl = crashFinderImpl;
	        this.listener = listener;
	    }
	    
	    public String getSeed()
	    {
			if (this.seed == null) {
				throw new NullPointerException("Seed has not been computed.");
			}
	    	return this.seed;
	    }
	    
	    public void runner() {
	        
	    	try {
	            
	    		String pathToJar = crashFinderImpl.getPathToJarFile();
	            String pathToStackTrace = crashFinderImpl.getPathToStackTrace();
	            String pathToDiffFile = crashFinderImpl.getPathToDiffOut();
	            String pathToLogDiff = crashFinderImpl.getPathToLogDiff();
	            String pathToInstrJar = crashFinderImpl.getPathToInstrumentedJarFile();
	            String pathToLogSlicing = crashFinderImpl.getPathToLogSlicing();
	            
	            //1. Slicing
	            Slicing slicing = crashFinderImpl.initializeSlicing(pathToJar);
				if (slicing == null) {
					throw new NullPointerException("Slicing is null");
				}
	            
	            //Statement seedStatement = null;
	            //seedStatement = crashFinderImpl.findSeedStatement(pathToStackTrace, slicing);
	            Statement seedStatement = crashFinderImpl.findSeedStatementFailing(pathToStackTrace, slicing);
	            this.seed = crashFinderImpl.getSeed();
				this.seedStatement = seedStatement;
				listener.getLogger().println("Seed runner: " + seed);
	            listener.getLogger().println("Statement: " + seedStatement.toString());
				//this.seed = seedStatement.toString();
	            //3.Backward slicing
	            Collection<? extends Statement> slice = crashFinderImpl.backWardSlicing(seedStatement, slicing, pathToLogSlicing);
	            listener.getLogger().println("---START DUMP SLICE---");
	            WALAUtils.dumpSlice(new ArrayList<Statement>(slice), new
	                    PrintWriter(listener.getLogger()));
	            listener.getLogger().println("---END DUMP SLICE---");
	            
	            //4. Intersection
	            Collection<Statement> intersection = null;
	            BufferedReader br = null;
	            String sCurrentLine;
	            PrintWriter output = null;
	            List<String> diffClass = new ArrayList<String>();
	            List<String> matching = new ArrayList<String>();
	            
	            try {
	                output = new PrintWriter(
	                        new BufferedWriter(new FileWriter(pathToLogDiff)));
	                output.write("");
	                br = new BufferedReader(new FileReader(pathToDiffFile));
	                String prefix = crashFinderImpl
	                        .getCanonicalPathToWorkspaceDir();
	                while (prefix.endsWith("/")) {
	                    prefix = prefix.substring(0, prefix.length() - 1);
	                }
	                // Escape special characters for regex use
	                prefix = Pattern.quote(prefix);
	                listener.getLogger().println("Prefix: " + prefix);
	                while ((sCurrentLine = br.readLine()) != null) 
	                {
	                    Pattern p = Pattern.compile("\\+++ " + prefix + "/(.*?)" +
	                            "\\.java");
	                    Matcher m = p.matcher(sCurrentLine);
	                    if (m.find())
	                    {
	                        String strFound = m.group();
	                        String absPath = strFound.replace("+", "").trim();
	                        File javaFile = new File(absPath);
	                        String packageName = new PackageExtractor(javaFile)
	                                .extractPackageName();
	                        String fileName = javaFile.getName();
	                        String fullClassName = packageName + "." + fileName
	                                .substring(0, fileName.length() - 5);
	                        matching.add(fullClassName);
	                        diffClass.add(fullClassName);
	                        listener.getLogger().println("Class found: " + fullClassName);
	                        output.printf("%s\r\n", strFound);
	                    }
	                }
	            } catch (IOException e)
	            {
					RuntimeException re = new RuntimeException(e);
					re.setStackTrace(e.getStackTrace());
					throw e;
	            } finally {
	                if (output != null) {
	                    output.close();
	                }
	            }
	            
	            intersection = crashFinderImpl.intersection(matching,slice);
	            
	            //5.Instrument
	            crashFinderImpl.instrument(pathToJar,pathToInstrJar, intersection);
	            
	          
	            
	        } catch (IOException ex) 
	        {
	            Logger.getLogger(JenkinsCrashFinderRunnerFailing.class.getName()).log(Level.SEVERE, null, ex);
	        }
	        
	        
	    }


	public Statement getSeedStatement() {
		if (this.seedStatement == null) {
			throw new NullPointerException("Seed statement has not been " +
					"computed.");
		}
		return this.seedStatement;
	}
}
