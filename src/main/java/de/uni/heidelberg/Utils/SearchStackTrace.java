/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.heidelberg.Utils;

import hudson.model.BuildListener;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author antsaharinala
 */
public class SearchStackTrace {
    
    public static void listf(String directoryName, ArrayList<File> files) 
    {
            File directory = new File(directoryName);

            // get all the files from a directory
            File[] fList = directory.listFiles();
            for (File file : fList) 
            {
                if (file.isFile() && file.isHidden() == false) 
                {
                    files.add(file);
		} else if (file.isDirectory()) {
                    listf(file.getAbsolutePath(), files);
		}//end if
			
            }//end for
    }
	
	
	/**
	public static boolean isFileStackTrace(File file) throws IOException
	{
                boolean isStackTrace = false;
		
		String fileContent = DocumentReader.slurpFile(file);
		boolean isHTMLXML = isHTMLXMLFile(fileContent);
		if(isHTMLXML == true)
		{
			
		}else
		{
			String regexAlt = "(Caused by: (.*)Exception:(\\s+at(.*))+)?";
			String regexText = "\\s+" + "(.*)Exception:(.*)(\\s+at(.*))+" + regexAlt;
			Pattern pattern = Pattern.compile(regexText);
			Matcher matcher = pattern.matcher(fileContent);
			while(matcher.find())
			{
				String result = matcher.group();
				//System.out.println(file.getName());
				//System.out.println("Result: " + result);
				isStackTrace = true;
			}
		}
			
                return isStackTrace;
		
	}//end method 
	**/
        public static boolean isFileStackTrace(File file) throws IOException
        {
		
                boolean isStackTrace = false;
		String fileContent = DocumentReader.slurpFile(file);
		boolean isHTMLXML = DetectXMLHTML.isHtml(fileContent);
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
			
                        String regexAlt = "(Caused by:\\s?(.*)Exception:(\\s+at(.*)|\\s+at)+)?";
			String regexText = "([a-zA-Z]+)Exception:\\s+(.*)(\\s+(.*)|\\s+at)+" ; //+ regexAlt;
			//String regexText = "([a-zA-Z]+)Exception: //+(.*)\\s+at(.*)";
			//String regexText = "Exception:(.*)at";//(.*)\\n(\\s*at(.*))+" ; //+ regexAlt;
			//String regexText = "[a-zA-Z]+"+ "Exception:";// + "[[a-zA-Z0-9]|\\s|\\p{Punct}]+" + "at" ; //+ "[[a-zA-Z0-9]|\\s|\\p{Punct}]+";
			Pattern pattern = Pattern.compile(regexText);
			//System.out.println(fileContent);
			Matcher matcher = pattern.matcher(fileContent);
			while(matcher.find())
			{
				
				String content = matcher.group();
                                isStackTrace = true;
			}
			
		}
			
                return isStackTrace;
		
	}

            
        public static String extractErrorMessageHTMLXML(File fileStackTrace,BuildListener listener) throws IOException
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
                                listener.getLogger().println("MATCH FOUND");
				String result = matcher.group();
				String newRegex = "([a-zA-Z]+)Exception:(\\s*([^<]*))+";
				Pattern newPattern = Pattern.compile(newRegex);
				Matcher newMatcher = newPattern.matcher(result);
				if(newMatcher.find())
				{
					error = error + newMatcher.group().trim() + "\n";
                                        listener.getLogger().println("Error stack trace: " + error);
				}//end if
				
			}//ehd while
			
		
                /**else
		{
			String regexAlt = "(Caused by:\\s?(.*)Exception:(\\s+at(.*)|\\s+at)+)?";
			String regexText = "([a-zA-Z]+)Exception:\\s+(.*)(\\s+(.*)|\\s+at)+" ; //+ regexAlt;
			Pattern pattern = Pattern.compile(regexText);
			Matcher matcher = pattern.matcher(fileContent);
			while(matcher.find())
			{
				error = error + matcher.group().trim() + "\n";
			}
			
		}**/
                return error;
                
        }//end method
        

	public static String searchFileStackTrace(String pathToDirectoryRoot, BuildListener listener) throws IOException
	{
		ArrayList<File> listResult = new ArrayList<File>();
		//ArrayList<File> listStackTrace = new ArrayList<File>();
		//ArrayList<String> listFilename = new ArrayList<String>();
		
                //HashMap<String,String> mapErrorMessageFilename = new HashMap<String,String>();
                
		SearchStackTrace.listf(pathToDirectoryRoot, listResult);
                File file = null;
                String strStackTrace = "";
		for (int i  = 0 ; i < listResult.size() ; i++)
		{
			File f  = listResult.get(i);
			boolean value = isFileStackTrace(f);
			if(value == true)
			{
				file = f;
                                listener.getLogger().println("File stack trace: " + f.getAbsolutePath());
                                String content = DocumentReader.slurpFile(file);
                                if(DetectXMLHTML.isHtml(content) == false)
                                {
                                    listener.getLogger().println("Not html xml");
                                    return DocumentReader.slurpFile(file);
                                }else
                                {
                                    listener.getLogger().println("HTML XML STACK TRACE");
                                    String error = SearchStackTrace.extractErrorMessageHTMLXML(file, listener);
                                   return error;
                                }
                               
                       }//end if
		}
		return strStackTrace;
	}


	public static String extractTestClass(File stackTraceFile) throws
			IOException {
		BufferedReader reader = new BufferedReader(new FileReader
				(stackTraceFile));
		reader.readLine();
		String secondLine = reader.readLine();
		return secondLine.replace("Test set:", "").trim();
	}
}
