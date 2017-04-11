package br.com.furb.cripto;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Classe para encodar/decodar em base 64.
 * 
 * @author Guilherme.Rosa
 */
public class MyBase64 {

    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * Encoda texto.
     * 
     * @param txt
     *            texto plano
     * @return texto encodado
     */
    public static String encode(String txt) {
	return Base64.getEncoder().encodeToString(txt.getBytes());
    }

    /**
     * Encoda texto.
     * 
     * @param bytes
     *            bytes do texto plano
     * @return bytes do texto encodado
     */
    public static byte[] encode(byte[] bytes) {
	return Base64.getEncoder().encode(bytes);
    }
    
    public static String encodeToString(byte[] bytes) {
	return Base64.getEncoder().encodeToString(bytes);
    }


    /**
     * Decoda texto.
     * 
     * @param txt
     *            texto plano
     * @return texto decodado
     */
    public static byte[] decode(String txt) {
	return Base64.getDecoder().decode(txt.getBytes(DEFAULT_CHARSET));
    }

    /**
     * Decoda texto.
     * 
     * @param bytes
     *            bytes do texto plano
     * @return bytes do texto decodado
     */
    public static byte[] decode(byte[] bytes) {
	return Base64.getDecoder().decode(bytes);
    }
    
//    public static String decodeToString(byte[] bytes) {
//	return Base64.getDecoder().deco
//    }

    /**
     * Apenas para testes
     * 
     * @param args
     * @throws Throwable
     */
    public static void main(String[] args) {
	System.out.println(decode(encode("Teste  çÃo")));
    }

}
