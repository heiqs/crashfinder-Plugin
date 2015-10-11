package de.uni.heidelberg.PlugIn;


/*
 * The MIT License
 *
 * Copyright 2015 Antsa Harinala Andriamboavonjy.
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



import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.uni.heidelberg.Utils.DetectionXMLHTML;
import de.uni.heidelberg.Utils.DocumentReader;



/**
 * This class allows the search for stacktrace file after running test. 
 * @author Antsa Harinala Andriamboavonjy
 */
public class CrashFinderImplSearchStackTrace{
    
	/**
	 * This method extracts all files containing in a directory
	 * @param parentDirectory - path to parent directory
	 * @param fileResult - list containing File in the parent directory
	 *
	 */
    public static void listf(String parentDirectory, ArrayList<File> fileResult) 
    {
            File directory = new File(parentDirectory);

            // get all the files from directory
            File[] fList = directory.listFiles();
            for (File file : fList) 
            {
                if (file.isFile() && file.isHidden() == false) 
                {
                    fileResult.add(file);
                } else if (file.isDirectory() && file.isHidden() == false) {
                    listf(file.getAbsolutePath(), fileResult);
                }
            }
    }
	
	/**
	 * This method checks whether a file type contains stack trace
	 * @param file
	 * @return
	 * @throws IOException
	 */
    public static boolean isFileStackTrace(File file) throws IOException
    {
		
        boolean isStackTrace = false;
		String fileContent = DocumentReader.slurpFile(file);
		boolean isHTMLXML = DetectionXMLHTML.isHtml(fileContent);
		if(isHTMLXML)
		{
			String startTag =  "\\<\\w+((\\s+\\w+(\\s*\\=\\s*(?:\".*?\"|'.*?'|[^'\"\\>\\s]+))?)+\\s*|\\s*)\\>";
			String endTag = "\\</\\w+\\>";
			
			String regex = startTag + "([^<]*)" + endTag; // + endTag ;
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(fileContent);
			while(matcher.find())
			{
				String error = matcher.group();
				String newRegex = "([a-zA-Z]+)Exception:(\\s*([^<]*))+";
				Pattern newPattern = Pattern.compile(newRegex);
				Matcher newMatcher = newPattern.matcher(error);
				if(newMatcher.find())
				{
					String tagsError = newMatcher.group();
                    isStackTrace = true;
                }
			}
		
		//case txt file
		}else
		{
			 System.out.println("TXT FILE");
             String regexAlt = "(Caused by:\\s?(.*)Exception:(\\s+at(.*)|\\s+at)+)?";
             String regexText = "([a-zA-Z\\p{Punct}]+)Exception(:.*)?(\\n\\t?at.*)";
             //String regexText = "([a-zA-Z]+)Exception:\\s+(.*)(\\s+(.*)|\\s+at)+" ; //+ regexAlt;
             //String regexText = "([a-zA-Z]+)Exception: //+(.*)\\s+at(.*)";
             //String regexText = "Exception:(.*)at";//(.*)\\n(\\s*at(.*))+" ; //+ regexAlt;
             //String regexText = "[a-zA-Z]+"+ "Exception:";// + "[[a-zA-Z0-9]|\\s|\\p{Punct}]+" + "at" ; //+ "[[a-zA-Z0-9]|\\s|\\p{Punct}]+";
             Pattern pattern = Pattern.compile(regexText);
             Matcher matcher = pattern.matcher(fileContent);
             while(matcher.find())
             {
            	 String content = matcher.group();
            	 System.out.println("Content: " + content);
                 isStackTrace = true;
                 
             }//end while
			
		}
			
        return isStackTrace;
		
	}

            
    /**
     * This method extracts errors messages containing in xml or html File.
     * @param fileStackTrace - File containing stack trace
     * @param listener - 
     * @return string errors
     * @throws IOException
     */
    public static String extractErrorMessageHTMLXML(File fileStackTrace) throws IOException
	{
		
    		String fileContent = DocumentReader.slurpFile(fileStackTrace);
    		String error = "";
		
    		String startTag =  "\\<\\w+((\\s+\\w+(\\s*\\=\\s*(?:\".*?\"|'.*?'|[^'\"\\>\\s]+))?)+\\s*|\\s*)\\>";
			String endTag = "\\</\\w+\\>";
			
			String regex = startTag + "([^<]*)" + endTag; // + endTag ;
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(fileContent);
			while(matcher.find())
			{
                                
				String result = matcher.group();
				String newRegex = "([a-zA-Z]+)Exception:(\\s*([^<]*))+";
				Pattern newPattern = Pattern.compile(newRegex);
				Matcher newMatcher = newPattern.matcher(result);
				if(newMatcher.find())
				{
					error = error + newMatcher.group().trim() + "\n";
                }
			}
			return error;
                
     }
        

    /**
     * This method extracts string errors containing in non-html/xml files. 
     * @param parentDirectory - path to the parent directory
     * @param listener
     * @return
     * @throws IOException
     */
	public static ArrayList<String> searchFileStackTrace(String parentDirectory) throws IOException
	{
		ArrayList<String> listPathToStackTrace = new ArrayList<String>();
		
		ArrayList<File> listFiles = new ArrayList<File>();
		CrashFinderImplSearchStackTrace.listf(parentDirectory, listFiles);
        File file = null;
        String strStackTrace = "";
		for (int i  = 0 ; i < listFiles.size() ; i++)
		{
			File f  = listFiles.get(i);
			boolean value = isFileStackTrace(f);
			if(value == true)
			{
				file = f;
				String content = DocumentReader.slurpFile(file);
                if(DetectionXMLHTML.isHtml(content) == false)
                {
                	//return DocumentReader.slurpFile(file);
                	//return file.getAbsolutePath();
                	String absPathFile = file.getAbsolutePath();
                	listPathToStackTrace.add(absPathFile);
                }
                /**
                else
                {
                	String error = CrashFinderImpSearchStackTrace.extractErrorMessageHTMLXML(file, listener);
                    return error;
                }**/
                               
			}
		}
		
		return listPathToStackTrace;
	}
	
	
	public static String extractTestClass(File stackTraceFile) throws
		IOException {
			BufferedReader reader = new BufferedReader(new FileReader(stackTraceFile));
		    reader.readLine();
		    String secondLine = reader.readLine();
		    return secondLine.replace("Test set:", "").trim();
	}
	
	
	/**
	public static void main(String[] args) throws IOException
	{
		String pathToParentDirectory = "/home/antsaharinala/.jenkins/workspace/Correct_Version_Hadoop"; 
		String pathToStackTrace = CrashFinderImplSearchStackTrace.searchFileStackTrace(pathToParentDirectory);
		System.out.println("Path to stack trace: " + pathToStackTrace);
		
		/**
		String pathToStackTrace = "/home/antsaharinala/crashFinder-dump/Log.log";
		File fileStackTrace = new File(pathToStackTrace);
		boolean isStackTrace = CrashFinderImplSearchStackTrace.isFileStackTrace(fileStackTrace);
		System.out.println("Is stack trace: " + isStackTrace);**/
	//}


        
}

