/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.heidelberg.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author antsaharinala
 */
public class DocumentWriter {
    
    public static void  writeDocument(String output, File fileOutput) throws IOException{
		FileWriter fileWriter = new FileWriter(fileOutput);
		fileWriter.write(output);
		fileWriter.flush();
		fileWriter.close();
    }
    
}
