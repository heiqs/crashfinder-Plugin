/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.heidelberg.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author antsaharinala
 */
public class DocumentReader {

    public static String slurpFile(File file)throws IOException{
		
        //buffer
	StringBuilder buffer  = new StringBuilder();
	//read
	BufferedReader br = new BufferedReader(new FileReader(file));
	while(br.ready())
        {
            String line = br.readLine();
            buffer.append(line);
            buffer.append("\n");
	}
		
        return buffer.toString();
    }
    
    public static ArrayList<String> slurpFiles(File file)throws IOException
    {
        ArrayList<String> listLines = new ArrayList<String>();
        
        BufferedReader br = new BufferedReader(new FileReader(file));
        while(br.ready())
        {
            String line = br.readLine();
            listLines.add(line);
            //buffer.append(line);
            //buffer.append("\n");
        }
        
        return listLines;
    }
    
}
    

