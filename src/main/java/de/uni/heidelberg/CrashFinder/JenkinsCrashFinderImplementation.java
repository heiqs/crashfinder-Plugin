/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.heidelberg.CrashFinder;

import com.ibm.wala.ipa.slicer.Slicer.ControlDependenceOptions;
import com.ibm.wala.ipa.slicer.Slicer.DataDependenceOptions;
import com.ibm.wala.ipa.slicer.Statement;
import com.ibm.wala.shrikeCT.InvalidClassFileException;
import com.ibm.wala.util.CancelException;
import de.hdu.pvs.crashfinder.analysis.FindSeed;
import de.hdu.pvs.crashfinder.analysis.IRStatement;
import de.hdu.pvs.crashfinder.analysis.Slicing;
import de.hdu.pvs.crashfinder.analysis.SlicingOutput;
import de.hdu.pvs.crashfinder.instrument.InstrumentStats;
import de.hdu.pvs.crashfinder.instrument.RelatedStmtInstrumenter;
import de.hdu.pvs.crashfinder.util.WALAUtils;
import hudson.model.BuildListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;




/**
 *
 * @author antsaharinala
 */
public class JenkinsCrashFinderImplementation implements CrashFinderImplementation{

    private String canonicalPathToWorkspaceDir;
    private String pathToDiffOut;
    
    private String pathToLogDiff;
    
    private final String pathToExclusionFile="resources/JavaAllExclusions.txt";
    
    private String pathToStackTrace;
    
    private String pathToJarFile;
    private String pathToInstrumentedJarFile;
    private BuildListener listener;
    
    
    
   
    public JenkinsCrashFinderImplementation(String pathToDiffOut, String pathToLogDiff,
                                            String pathToStackTrace, String pathToJarFile,
                                            String pathToInstrumentedJarFile,
                                            String canonicalPathToWorkspaceDir, BuildListener listener)
    {
        this.pathToDiffOut = pathToDiffOut;
        this.pathToLogDiff = pathToLogDiff;
        this.pathToJarFile = pathToJarFile;
        this.pathToInstrumentedJarFile = pathToInstrumentedJarFile;
        this.pathToStackTrace = pathToStackTrace;
        this.canonicalPathToWorkspaceDir = canonicalPathToWorkspaceDir;
        this.listener = listener;
        
    }
    
    //Default constructor
    public JenkinsCrashFinderImplementation()
    {
        this.pathToDiffOut = "";
        this.pathToLogDiff = "";
        this.pathToJarFile = "";
        this.pathToInstrumentedJarFile = "";
        this.pathToStackTrace = "";
    }
    
    public String getPathToDiffOut()
    {
        return this.pathToDiffOut;
    }
    
    public String getPathToLogDiff()
    {
        return this.pathToLogDiff;
    }
    
    public String getPathToJarFile()
    {
        return this.pathToJarFile;
    }
    
    public String getPathToInstrumentedJarFile()
    {
        return this.pathToInstrumentedJarFile;
    }
    
    public String getPathToStackTrace()
    {
        return this.pathToStackTrace;
    }

    public String getCanonicalPathToWorkspaceDir() {
        return canonicalPathToWorkspaceDir;
    }

    public void setPathToDiffOut(String pathToDiffOut)
    {
        this.pathToDiffOut = pathToDiffOut;
    }
    
    public void setPathToLogDiff(String pathToLogDiff)
    {
        this.pathToLogDiff = pathToLogDiff;
    }
    
    public void setPathToJarFile(String pathToJarFile)
    {
        this.pathToJarFile = pathToJarFile;
    }
    
    public void setPathToInstrumentedJar(String pathToInstrumentedJar)
    {
        this.pathToInstrumentedJarFile = pathToInstrumentedJar;
    }
    
    public void setPathToStackTrace(String pathToStackTrace)
    {
        this.pathToStackTrace = pathToStackTrace;
    }
    
    @Override
    public Slicing initializeSlicing(final String jar)
    {
	//final String exclusionsFileName = "JavaAllExclusions.txt";
	//try{
                //if (!new File(canonicalJoin(canonicalWorkspacePath,
		//			exclusionsFileName)).exists()) {
                /**
                    if(!new File(this.pathToExclusionFile).exists())
                    {
				/*
				 * load exclusions file as resource and copy into workspace,
				 * because Slicing() needs a file path as input
				 */
		//		ClassLoader classloader = this.getClass().getClassLoader();
		//		BufferedReader is = new BufferedReader(new InputStreamReader(
		//				classloader
		//						.getResourceAsStream("JavaAllExclusions.txt")));
		//		PrintWriter os = new PrintWriter(new FileOutputStream(
		//				canonicalJoin(canonicalWorkspacePath,
		//						"JavaAllExclusions.txt")));
		//		for (String line = is.readLine(); line != null; line = is
		//				.readLine()) {
		//			os.println(line);
		//		}
		//		is.close();
		//		os.close();
		//	}
		//} catch (IOException e) {
		//	listener.getLogger().println(e.getStackTrace());
		//}
        
               // now the actual slicing
		Slicing helper = null;
                helper = new Slicing(jar, "", pathToExclusionFile);
		helper.CallGraphBuilder();
		helper.setDataDependenceOptions(DataDependenceOptions.NONE);
		helper.setControlDependenceOptions(ControlDependenceOptions.NO_EXCEPTIONAL_EDGES);
		helper.setContextSensitive(true); // context-insensitive
		return helper;
                
    }
    
    
    
    public Collection<? extends Statement> backWardSlicing(Statement seedStatement, Slicing helper)
    {
        try {
		return helper.computeSlice(seedStatement);
                
            } catch (CancelException e) {
		e.printStackTrace();
		return null;
            }
    }
    
    @Override
    public Statement findSeedStatement(String pathToStackTrace, Slicing slicing)throws IOException
    {
		FindSeed computeSeed = new FindSeed();
		int lineNumber = computeSeed.computeSeed(pathToStackTrace)
				.getLineNumber();
		String seedClass = computeSeed.computeSeed(pathToStackTrace)
				.getSeedClass();
		listener.getLogger().println("Seed class: " + seedClass);
		try {
			Statement result = slicing.extractStatementfromException(seedClass,
					lineNumber);
			return result;
		} catch (InvalidClassFileException e) {
			listener.getLogger().println(e.getStackTrace());
			return null;
		}
    }
        
    public void instrument(String pathToJar, String pathToInstrJar, Collection<Statement> intersection) 
    {
        if (intersection.isEmpty()) {
            throw new IllegalArgumentException("Cannot instrument: " +
                    "Intersection is empty.");
        }
		Collection<IRStatement> irs = Slicing.convert(intersection);
		SlicingOutput output1 = new SlicingOutput(irs);
		RelatedStmtInstrumenter instrumenter = new RelatedStmtInstrumenter(
				output1);
		try {
			instrumenter.instrument(pathToJar,pathToInstrJar);
							
		}catch (Exception e) {
			listener.getLogger().println(e.getMessage());
		}
		InstrumentStats.showInstrumentationStats();
    }

    @Override
    //public Collection<Statement> intersection(List<String> diff, Collection<? extends Statement> passingSlice) {
    //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    //}
    public Collection<Statement> intersection(List<String> diff,
			Collection<? extends Statement> slice) 
    {
        if (diff.isEmpty()) {
            throw new IllegalArgumentException("Cannot intersect with empty " +
                    "diff.");
        }
        if (slice.isEmpty()) {
            throw new IllegalArgumentException("Cannot intersect with empty " +
                    "slice");
        }
		Collection<Statement> sliceDiff = new ArrayList<Statement>();
		for (Statement s1 : slice) {
			String fullClassName = null;
			String extractedFullClassName = WALAUtils.getJavaFullClassName(s1
					.getNode().getMethod().getDeclaringClass());
			if (extractedFullClassName.contains("$")) {

				String[] dollarReplace = extractedFullClassName.split("\\$");
				fullClassName = dollarReplace[0];
			} else {
				fullClassName = extractedFullClassName;
			}
			if (diff.contains(fullClassName)) {
				sliceDiff.add(s1);
			}
		}
		return sliceDiff;
    }

 
    private Collection<? extends Statement> backwardSlice(Statement seedStatement, Slicing helper) 
    {
	try {
		return helper.computeSlice(seedStatement);
            } catch (CancelException e) {
		e.printStackTrace();
		return null;
            }
    }

    

  
    
}
