package br.com.furb.cripto;

import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    private SecretKeySpec skeySpec;
    private IvParameterSpec iv;

    public AES(String key) throws Throwable {
	skeySpec = new SecretKeySpec(key.getBytes(), "AES");
	iv = generateParameterSpec();
    }

    public AES(byte[] key) throws Throwable {
	skeySpec = new SecretKeySpec(key, "AES");
	iv = generateParameterSpec();
    }

    public AES(byte[] key, IvParameterSpec iv) throws Throwable {
	skeySpec = new SecretKeySpec(key, "AES");
	this.iv = iv;
    }

    public static IvParameterSpec generateParameterSpec() throws Throwable {
	byte[] newKey = generateSecretKey().getEncoded();
	return new IvParameterSpec(newKey);
    }

    public static IvParameterSpec getParameterSpecByFile(String name, PublicKey publicKey) throws Throwable {
	File vectorFile = new File(Constants.tempDir.getAbsolutePath() + File.separator + name + ".iv");
	byte[] newKey = generateSecretKey().getEncoded();
	IvParameterSpec iv = new IvParameterSpec(newKey);
	byte[] encrypt = MyBase64.encode(RSA.encrypt(iv.getIV(), publicKey));
	MyFileUtils.writeInSameLine(vectorFile, new String(encrypt));

	return iv;
    }

    public static IvParameterSpec getParameterSpecByFile(String name, PrivateKey privateKey) throws Throwable {
	File vectorFile = new File(Constants.tempDir.getAbsolutePath() + File.separator + name + ".iv");
	if (vectorFile.exists()) {
	    String line = MyFileUtils.readLine(vectorFile);
	    byte[] decrypt = RSA.decrypt(MyBase64.decode(line.getBytes()), privateKey);
	    return new IvParameterSpec(decrypt);
	}
	return null;
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