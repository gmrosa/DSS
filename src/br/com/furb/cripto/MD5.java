package br.com.furb.cripto;

import javax.crypto.SecretKey;

/**
 * Classe para geração de hash.
 * 
 * @author Guilherme.Rosa
 */
public class MD5 {

    /**
     * Gera um hash de 128 bits.
     * 
     * @param input
     *            chave
     * @return hash
     */
    public static String generateHash128(String input) {
	return generateHash(input).substring(0, 16);
    }

    /**
     * Gera um hash de 128 bits.
     * 
     * @param input
     *            chave secreta
     * @return hash
     */
    public static String generateHash128(SecretKey input) {
	return generateHash(input).substring(0, 16);
    }

    /**
     * Gera um hash.
     * 
     * @param input
     *            chave secreta
     * @return hash
     */
    public static String generateHash(SecretKey input) {
	return generateHash(new String(input.getEncoded()));
    }

    /**
     * Gera um hash.
     * 
     * @param input
     *            texto plano
     * @return hash
     */
    public static String generateHash(String input) {
	String md5 = null;
	try {
	    java.security.MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
	    // Update input string in message digest
	    digest.update(input.getBytes(), 0, input.length());
	    // Converts message digest value in base 16 (hex)
	    md5 = new java.math.BigInteger(1, digest.digest()).toString(16);
	} catch (java.security.NoSuchAlgorithmException e) {
	    e.printStackTrace();
	}
	return md5;
    }

    /**
     * Apenas para testes
     * 
     * @param args
     * @throws Throwable
     */
    public static void main(String[] args) {
	System.out.println(generateHash("Hash"));
	System.out.println(generateHash("Hash").length());
    }

}
