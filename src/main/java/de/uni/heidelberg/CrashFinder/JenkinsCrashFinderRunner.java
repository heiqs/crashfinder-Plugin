/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author antsaharinala
 */
public class JenkinsCrashFinderRunner implements CrashFinderRunner{
    
    
    private final JenkinsCrashFinderImplementation crashFinderImpl;
    
    private BuildListener listener;
    
    public JenkinsCrashFinderRunner(JenkinsCrashFinderImplementation crashFinderImpl,BuildListener listener)
    {
        this.crashFinderImpl = crashFinderImpl;
        this.listener = listener;
    }

    public void runner() {
        
        try {
            
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            //crashFinderImpl.initializeSlicing();
            //crashFinderImpl.intersection();
            //crashFinderImpl.backWardSlicing();
            //crashFinderImpl.extractSeedStatement();
            //crashFinderImpl.instrument();
            
            //String pathToStackTrace, Slicing slicing
            
            String pathToJar = crashFinderImpl.getPathToJarFile();
            String pathToStackTrace = crashFinderImpl.getPathToStackTrace();
            String pathToDiffFile = crashFinderImpl.getPathToDiffOut();
            String pathToLogDiff = crashFinderImpl.getPathToLogDiff();
            String pathToInstrJar = crashFinderImpl.getPathToInstrumentedJarFile();
            //String pathToInstrJar = crashFinderImpl.getPathToInstrumentedJarFile();
            
            //1. Slicing
            Slicing slicing = crashFinderImpl.initializeSlicing(pathToJar);
            
            //2. Find statement
            //Statement seedStatement = null;
            //try {
            //    seedStatement = crashFinderImpl.findSeedStatement(pathToStackTrace, slicing);
            //} catch (IOException ex) {
            //    Logger.getLogger(JenkinsCrashFinderRunner.class.getName()).log(Level.SEVERE, null, ex);
            //}
            Statement seedStatement = crashFinderImpl.findSeedStatement(pathToStackTrace, slicing);
            listener.getLogger().println("Statement: " + seedStatement.toString());
            //3.Backward slicing
            Collection<? extends Statement> slice = crashFinderImpl.backWardSlicing(seedStatement, slicing);
            
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
                        String absPath = strFound.replace("+++ ", "");
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
            } finally {
                if (output != null) {
                    output.close();
                }
            }
            
            intersection = crashFinderImpl.intersection(matching,slice);
            
            //5. Instrument
            crashFinderImpl.instrument(pathToJar,pathToInstrJar, intersection);
            
            
        } catch (IOException ex) 
            {
            Logger.getLogger(JenkinsCrashFinderRunner.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        
    }
    
}
