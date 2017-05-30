package br.com.furb;

import java.security.MessageDigest;

public class Sha256 {

    public static String getHash(String secret) throws Throwable {
	MessageDigest md = MessageDigest.getInstance("SHA-256");
	md.update(secret.getBytes("UTF-8"));
	byte[] digest = md.digest();
	String hash = String.format("%064x", new java.math.BigInteger(1, digest));
	return hash;
    }

}
