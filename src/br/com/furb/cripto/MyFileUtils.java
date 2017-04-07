package br.com.furb.cripto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class MyFileUtils {
    
    public static void writeInSameLine(File file, String txt) throws Throwable {
	try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
	    writer.write(txt);
	    writer.flush();
	}
    }
    
    public static String readLine(File file) throws Throwable {
	try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
	    return reader.readLine();
	}
    }


}
