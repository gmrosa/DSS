package br.com.furb.cripto;

import javax.crypto.SecretKey;

public class MD5 {

    public static String generateHash128(SecretKey input) {
	return generateHash(input).substring(0, 16);
    }

    public static String generateHash(SecretKey input) {
	return generateHash(new String(input.getEncoded()));
    }

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

    public static void main(String[] args) {
	System.out.println(generateHash("Hash").length());
    }

}
