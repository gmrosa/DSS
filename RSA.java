package br.com.furb;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

public class RSA {
	private static KeyPair keyPair;

	public static byte[] criptografar(String value, PublicKey publicKey) throws Throwable {
		Cipher c = Cipher.getInstance("RSA");
		c.init(Cipher.ENCRYPT_MODE, publicKey);
		return MyBase64.encode(c.doFinal(value.getBytes()));
	}

	public static byte[] decriptografar(byte[] encrypted, PrivateKey privateKey) throws Throwable {
		Cipher dec = Cipher.getInstance("RSA");
		dec.init(Cipher.DECRYPT_MODE, privateKey);
		return dec.doFinal(MyBase64.decode(encrypted));
	}

	private static byte[] criptografar(String value) throws Throwable {
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		keyPair = kpg.generateKeyPair();
		return criptografar(value, keyPair.getPublic());
	}

	private static byte[] decriptografar(byte[] encrypted) throws Throwable {
		return decriptografar(encrypted, keyPair.getPrivate());
	}

	public static void main(String[] args) throws Throwable {
		byte[] criptografado = criptografar("meu texto");
		System.out.println(new String(criptografado));
		System.out.println(new String(decriptografar(criptografado)));
	}

}
