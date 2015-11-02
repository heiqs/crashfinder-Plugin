package de.hdu.pvs.crashfinder;

import com.ibm.wala.ipa.slicer.Statement;
import de.hdu.pvs.crashfinder.analysis.Slicing;
import de.hdu.pvs.crashfinder.util.WALAUtils;
import de.hdu.pvs.utils.PackageExtractor;
import hudson.model.BuildListener;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Antsa Harinala Andriamboavonjy, Dominik Fay Created in October 2015
 * 
 */
public class JenkinsCrashFinderRunnerPassing implements CrashFinderRunner {

	private final JenkinsCrashFinderImplementation crashFinderImpl;

	private Statement seedStatement;

	private BuildListener listener;

	private String seed;

	public JenkinsCrashFinderRunnerPassing(
			JenkinsCrashFinderImplementation crashFinderImpl,
			BuildListener listener, String seed) {
		this.crashFinderImpl = crashFinderImpl;
		this.listener = listener;
		this.seed = seed;
	}

	public String getSeed() {
		return this.seed;
	}

	public void runner() {

		try {

			String pathToJar = crashFinderImpl.getPathToJarFile();
			//String pathToStackTrace = crashFinderImpl.getPathToStackTrace();
			String pathToDiffFile = crashFinderImpl.getPathToDiffOut();
			String pathToLogDiff = crashFinderImpl.getPathToLogDiff();
			String pathToInstrJar = crashFinderImpl
					.getPathToInstrumentedJarFile();
			String pathToLogSlicing = crashFinderImpl.getPathToLogSlicing();

			// 1. Slicing
			listener.getLogger().println("Initializing slicing ....");
			Slicing slicing = crashFinderImpl.initializeSlicing(pathToJar);

			try {
				this.seedStatement = crashFinderImpl.findSeedStatementPassing(
						this.seed, new File(pathToDiffFile), slicing);
				this.seed = crashFinderImpl.getSeed();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// 3.Backward slicing
			Collection<? extends Statement> slice = crashFinderImpl
					.backWardSlicing(seedStatement, slicing, pathToLogSlicing);
			// listener.getLogger().println("---START DUMP SLICE---");
			WALAUtils.dumpSliceToFile(new ArrayList<Statement>(slice), pathToLogSlicing);
			//			WALAUtils.dumpSlice(new ArrayList<Statement>(slice),
//					new PrintWriter(listener.getLogger()));
			// listener.getLogger().println("---END DUMP SLICE---");

			// 4. Intersection
			Collection<Statement> intersection = null;
			BufferedReader br = null;
			String sCurrentLine;
			PrintWriter output = null;
			List<String> diffClass = new ArrayList<String>();
			List<String> matching = new ArrayList<String>();

			try {
				output = new PrintWriter(new BufferedWriter(new FileWriter(
						pathToLogDiff)));
				output.write("");
				br = new BufferedReader(new FileReader(pathToDiffFile));
				String prefix = crashFinderImpl
						.getCanonicalPathToWorkspaceDir();
				while (prefix.endsWith("/")) {
					prefix = prefix.substring(0, prefix.length() - 1);
				}
				// Escape special characters for regex use
				prefix = Pattern.quote(prefix);
				// listener.getLogger().println("Prefix: " + prefix);
				while ((sCurrentLine = br.readLine()) != null) {
					Pattern p = Pattern.compile("\\+++ " + prefix + "/(.*?)"
							+ "\\.java");
					Matcher m = p.matcher(sCurrentLine);
					if (m.find()) {
						String strFound = m.group();
						String absPath = strFound.replace("+", "").trim();
						File javaFile = new File(absPath);
						String packageName = new PackageExtractor(javaFile)
								.extractPackageName();
						String fileName = javaFile.getName();
						String fullClassName = packageName + "."
								+ fileName.substring(0, fileName.length() - 5);
						matching.add(fullClassName);
						diffClass.add(fullClassName);
						output.printf("%s\r\n", strFound);
					}
				}
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			} finally {
				if (output != null) {
					output.close();
				}
			}

			listener.getLogger().println("Executing intersection ....");
			intersection = crashFinderImpl.intersection(matching, slice);

			// 5. Instrument
			listener.getLogger().println("Executing instrumentation ...");
			crashFinderImpl.instrument(pathToJar, pathToInstrJar, intersection);

		} catch (IOException ex) {

			throw new RuntimeException(ex.getMessage(), ex);
		}

	}

}
