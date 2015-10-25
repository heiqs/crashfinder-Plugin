
package de.uni.heidelberg.CrashFinder;

import com.ibm.wala.ipa.slicer.Statement;
import de.hdu.pvs.crashfinder.analysis.Slicing;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.*;
import java.io.*;

/**
 *
 * @author Antsa Harinala Andriamboavonjy,Dominik Fay
 * Created in October 2015
 * 
 */
public interface CrashFinderImplementation {
    
    public abstract Slicing initializeSlicing(final String jar);
    public abstract Collection<Statement> intersection(List<String> diff,Collection<? extends Statement> passingSlice);
    //public abstract Collection<? extends Statement> backWardSlicing(Statement seedStatement, Slicing helper);
    public abstract Statement findSeedStatementFailing(String pathToStackTrace, Slicing slicing)throws IOException, InterruptedException;
    public abstract Statement findSeedStatementPassing(String seed, File fileDiff, Slicing slicing)throws IOException, InterruptedException;
    public abstract void instrument(String pathToJar, String pathToInstrJar, Collection<Statement> intersection);
	public abstract Collection<? extends Statement> backWardSlicing(Statement seedStatement, Slicing helper, String pathToLogSlicing)
			throws FileNotFoundException;
    
}
