package br.com.furb.cripto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Utilitários para leitura e gravação de arquivo.
 * 
 * @author Guilherme.Rosa
 */
public class MyFileUtils {

    /**
     * Escreve um texto plano em uma única linha.
     * 
     * @param file
     *            arquivo
     * @param txt
     *            texto
     * @throws Throwable
     */
    public static void writeInSameLine(File file, String txt) throws Throwable {
	try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
	    writer.write(txt);
	    writer.flush();
	}
    }

    /**
     * Lê um texto plano de um arquivo.
     * 
     * @param file
     *            arquivo
     * @throws Throwable
     */
    public static String readLine(File file) throws Throwable {
	try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
	    return reader.readLine();
	}
    }

}
