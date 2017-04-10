package br.com.furb.cripto;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public class RSA {

    private KeyPair keyPair;

    public RSA() throws Throwable {
	keyPair = generateKeyPair();
    }

    public static KeyPair generateKeyPair() throws Throwable {
	return KeyPairGenerator.getInstance("RSA").generateKeyPair();
    }

    public static byte[] encrypt(byte[] bytes, PublicKey publicKey) throws Throwable {
	Cipher c = Cipher.getInstance("RSA");
	c.init(Cipher.ENCRYPT_MODE, publicKey);
	return MyBase64.encode(c.doFinal(bytes));
    }

    public static byte[] encrypt(String value, PublicKey publicKey) throws Throwable {
	return encrypt(value.getBytes(), publicKey);
    }

    private byte[] encrypt(String value) throws Throwable {
	return encrypt(value, keyPair.getPublic());
    }

    public static byte[] decrypt(byte[] encrypted, PrivateKey privateKey) throws Throwable {
	Cipher dec = Cipher.getInstance("RSA");
	dec.init(Cipher.DECRYPT_MODE, privateKey);
	return dec.doFinal(MyBase64.decode(encrypted));
    }

    private byte[] decrypt(byte[] encrypted) throws Throwable {
	return decrypt(encrypted, keyPair.getPrivate());
    }

    public static byte[] wrapKey(PublicKey publicKey, SecretKey secretKey) throws Throwable {
	final Cipher cipher = Cipher.getInstance("RSA");
	cipher.init(Cipher.WRAP_MODE, publicKey);
	final byte[] wrapped = cipher.wrap(secretKey);
	return wrapped;
    }

    public byte[] wrapKey(SecretKey secretKey) throws Throwable {
	return wrapKey(keyPair.getPublic(), secretKey);
    }

    public static SecretKey unwrapKey(PrivateKey privateKey, byte[] wraped) throws Throwable {
	Cipher cipher = Cipher.getInstance("RSA");
	cipher.init(Cipher.UNWRAP_MODE, privateKey);
	return (SecretKey) cipher.unwrap(wraped, "AES", Cipher.SECRET_KEY);
    }

    public SecretKey unwrapKey(byte[] wraped) throws Throwable {
	return unwrapKey(keyPair.getPrivate(), wraped);
    }

    public static void main(String[] args) throws Throwable {
	RSA rsa = new RSA();
	byte[] encrypt = rsa.encrypt("meu texto");
	System.out.println("encrypt " + new String(encrypt));
	System.out.println("decript " + new String(rsa.decrypt(encrypt)));
	SecretKey secretKey = AES.generateSecretKey();
	System.out.println("secretKey " + new String(secretKey.getEncoded()));
	byte[] wraped = rsa.wrapKey(secretKey);
	System.out.println("wraped " + new String(wraped));
	System.out.println("unwraped " + new String(rsa.unwrapKey(wraped).getEncoded()));

    }

}
