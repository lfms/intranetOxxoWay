/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blitz.adminpago.util;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author pgrande
 */
public class ArchivoUtil {
    
    
    private static Logger log = Logger.getLogger("com.hitss.ventanilla.util.ArchivoUtil");
    
    
    public static void writeBuffered(List records, int bufSize, String nameFile) throws IOException {
        File file = new File(nameFile);
        try {
            
            file.setReadOnly();
            
            FileWriter writer = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(writer, bufSize);

            log.info("Writing buffered (buffer size: " + bufSize + ")... ");
            write(records, bufferedWriter);
            
        } 
        catch(Exception e) {
            log.info("Error File:" + e.toString());
            e.printStackTrace();
        }
        finally {
            // comment this out if you want to inspect the files afterward
            //file.delete();
            ;
        }
    }

    private static void write(List records, Writer writer) throws IOException {
        long start = System.currentTimeMillis();
        
        
        
        for (Object record: records) {
            writer.write(record.toString());
        }
        writer.flush();
        writer.close();
        long end = System.currentTimeMillis();
        log.info((end - start) / 1000f + " seconds");
    }    
    
    
}
