package de.uni.heidelberg.Utils;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class ExtractionClassJar {
	
	public ArrayList<String> extractClassJar(String pathToJar) throws IOException
	{
		ArrayList<String> classNames = new ArrayList<String> ();
		ZipInputStream zip = new ZipInputStream(new FileInputStream(pathToJar));
		for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
		    if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
		        // This ZipEntry represents a class. Now, what class does it represent?
		        String className = entry.getName().replace('/', '.'); // including ".class"
		        //System.out.println("Class name: " + className);
		        classNames.add(className.substring(0, className.length() - ".class".length()));
		    }
		}
		return classNames;
	}

}
