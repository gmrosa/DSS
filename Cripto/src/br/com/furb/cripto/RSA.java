package br.com.furb.cripto;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

/**
 * Criptografia RSA.
 * 
 * @author Guilherme.Rosa
 */
public class RSA {

    private KeyPair keyPair;

    public RSA() throws Throwable {
	keyPair = generateKeyPair();
    }

    /**
     * Gera um par de chaves.
     * 
     * @return par de chaves
     * @throws Throwable
     */
    public static KeyPair generateKeyPair() throws Throwable {
	return KeyPairGenerator.getInstance("RSA").generateKeyPair();
    }

    /**
     * Criptografa.
     * 
     * @param bytes
     *            bytes do texto
     * @param publicKey
     *            chave pública
     * @return bytes criptografados
     * @throws Throwable
     */
    public static byte[] encrypt(byte[] bytes, PublicKey publicKey) throws Throwable {
	Cipher c = Cipher.getInstance("RSA");
	c.init(Cipher.ENCRYPT_MODE, publicKey);
	return MyBase64.encode(c.doFinal(bytes));
    }

    /**
     * Criptografa.
     * 
     * @param bytes
     *            texto
     * @param publicKey
     *            chave pública
     * @return bytes criptografados
     * @throws Throwable
     */
    public static byte[] encrypt(String value, PublicKey publicKey) throws Throwable {
	return encrypt(value.getBytes(), publicKey);
    }

    /**
     * Criptografa.
     * 
     * @param value
     *            texto
     * @return bytes criptografados
     * @throws Throwable
     */
    private byte[] encrypt(String value) throws Throwable {
	return encrypt(value, keyPair.getPublic());
    }

    /**
     * Descriptografa.
     * 
     * @param encrypted
     *            texto criptografado
     * @param privateKey
     *            chave privada
     * @return bytes descriptografados
     * @throws Throwable
     */
    public static byte[] decrypt(byte[] encrypted, PrivateKey privateKey) throws Throwable {
	Cipher dec = Cipher.getInstance("RSA");
	dec.init(Cipher.DECRYPT_MODE, privateKey);
	return dec.doFinal(MyBase64.decode(encrypted));
    }

    /**
     * Descriptografa.
     * 
     * @param encrypted
     *            bytes do texto criptografado
     * @return bytes descriptografados
     * @throws Throwable
     */
    private byte[] decrypt(byte[] encrypted) throws Throwable {
	return decrypt(encrypted, keyPair.getPrivate());
    }

    /**
     * Criptografa chave secreta.
     * 
     * @param publicKey
     *            chave pública
     * @param secretKey
     *            chave secreta
     * @return bytes da chave secreta criptografados
     * @throws Throwable
     */
    public static byte[] wrapKey(PublicKey publicKey, SecretKey secretKey) throws Throwable {
	final Cipher cipher = Cipher.getInstance("RSA");
	cipher.init(Cipher.WRAP_MODE, publicKey);
	final byte[] wrapped = cipher.wrap(secretKey);
	return wrapped;
    }

    /**
     * Criptografa chave secreta.
     * 
     * @param secretKey
     *            chave secreta
     * @return bytes da chave secreta criptografados
     * @throws Throwable
     */
    public byte[] wrapKey(SecretKey secretKey) throws Throwable {
	return wrapKey(keyPair.getPublic(), secretKey);
    }

    /**
     * Descriptografa chave secreta.
     * 
     * @param privateKey
     *            chave privada
     * @param wraped
     *            bytes da chave secreta criptografados
     * @return chave secreta
     * @throws Throwable
     */
    public static SecretKey unwrapKey(PrivateKey privateKey, byte[] wraped) throws Throwable {
	Cipher cipher = Cipher.getInstance("RSA");
	cipher.init(Cipher.UNWRAP_MODE, privateKey);
	return (SecretKey) cipher.unwrap(wraped, "AES", Cipher.SECRET_KEY);
    }

    /**
     * Descriptografa chave secreta.
     * 
     * @param wraped
     *            bytes da chave secreta criptografados
     * @return chave secreta
     * @throws Throwable
     */
    public SecretKey unwrapKey(byte[] wraped) throws Throwable {
	return unwrapKey(keyPair.getPrivate(), wraped);
    }

    /**
     * Apenas para testes.
     * 
     * @param args
     * @throws Throwable
     */
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
