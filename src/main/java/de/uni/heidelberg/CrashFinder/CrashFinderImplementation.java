
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.heidelberg.CrashFinder;

import com.ibm.wala.ipa.slicer.Statement;
import de.hdu.pvs.crashfinder.analysis.Slicing;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.*;

/**
 *
 * @author antsaharinala
 */
public interface CrashFinderImplementation {
    
    public abstract Slicing initializeSlicing(final String jar);
    public abstract Collection<Statement> intersection(List<String> diff,Collection<? extends Statement> passingSlice);
    //public abstract Collection<? extends Statement> backWardSlicing(Statement seedStatement, Slicing helper);
    public abstract Statement findSeedStatement(String pathToStackTrace, Slicing slicing)throws IOException;
    public abstract void instrument(String pathToJar, String pathToInstrJar, Collection<Statement> intersection);
	public abstract Collection<? extends Statement> backWardSlicing(Statement seedStatement, Slicing helper, String pathToLogSlicing)
			throws FileNotFoundException;
    
}