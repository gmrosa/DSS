package br.com.furb.cripto;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    private SecretKeySpec skeySpec;
    private IvParameterSpec iv;

    public AES(String key) throws Throwable {
	skeySpec = new SecretKeySpec(key.getBytes(Constants.DEFAULT_CHARSET), "AES");
	byte[] newKey = generateSecretKey().getEncoded();
	iv = new IvParameterSpec(newKey);
    }

    public static SecretKey generateSecretKey() throws Throwable {
	KeyGenerator keyGen = KeyGenerator.getInstance("AES");
	keyGen.init(128);
	return keyGen.generateKey();
    }

    public byte[] encrypt(byte[] value) throws Throwable {
	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
	return cipher.doFinal(value);
    }

    public byte[] decrypt(byte[] encrypted) throws Throwable {
	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
	return cipher.doFinal(encrypted);
    }

    public static void main(String[] args) throws Throwable {
	AES aes = new AES("Bar12345Bar12345");
	byte[] encript = aes.encrypt("Hello World".getBytes());
	System.out.println(new String(encript));
	String decript = new String(aes.decrypt(encript));
	System.out.println(decript);
    }

}