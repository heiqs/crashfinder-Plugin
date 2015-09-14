/*
 * The MIT License
 *
 * Copyright 2015 antsaharinala.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.uni.heidelberg.CrashFinder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author antsaharinala
 */
public class FindSeed {
    

	
	private boolean passed = false;
	List<String> lines = new ArrayList<String>();
	List<String> stackFrames = new ArrayList<String>();
	Charset charset = Charset.forName("US-ASCII");
	
	public Seed computeSeed (String failedLogFile)
			throws FileNotFoundException {
		BufferedReader br = null;
		String line;
		try {
			br = new BufferedReader(new FileReader(failedLogFile));
			while ((line = br.readLine()) != null) {
		        lines.add(line.replaceAll("^\\s+", ""));
		    }
		} catch (IOException x) {
		    System.err.println();
		}
		
		for (int i = lines.size()-1; i > 0; i--) {
		    if (lines.get(i).startsWith("at ")){
		    	stackFrames.add(lines.get(i).replace("at ", ""));
		    	passed = true;
		    }else if (passed){
		    	break;
		    }
		}
		String rawSeed = stackFrames.get(stackFrames.size()-1);
		
	
		String classFile = rawSeed.split(".java:")[0].split("\\(")[1];
		int lineNumber = Integer.parseInt(rawSeed.split(".java:")[1].split("\\)")[0]);
		String packageName = rawSeed.split(classFile)[0];
		String seedClass = packageName + classFile;

		return new Seed(lineNumber, seedClass);
		
	}

	public class Seed {
		String seedClass;	
		int lineNumber;
		public Seed(int lineNumber, String seedClass) {
			super();
			this.lineNumber = lineNumber;
			this.seedClass = seedClass;
		}
		public String getSeedClass() {
			return seedClass;
		}
		public void setSeedClass(String seed) {
			this.seedClass = seed;
		}
		public int getLineNumber() {
			return lineNumber;
		}
		public void setLineNumber(int lineNumber) {
			this.lineNumber = lineNumber;
		}
	}
}
    

