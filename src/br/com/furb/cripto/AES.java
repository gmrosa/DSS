package br.com.furb.cripto;

import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Criptografia AES.
 * 
 * @author Guilherme.Rosa
 */
public class AES {

    private SecretKeySpec skeySpec;
    private IvParameterSpec iv;

    /**
     * @param key
     *            chave
     * @throws Throwable
     */
    public AES(String key) throws Throwable {
	skeySpec = new SecretKeySpec(key.getBytes(), "AES");
	iv = generateParameterSpec();
    }

    /**
     * @param key
     *            bytes da chave
     * @throws Throwable
     */
    public AES(byte[] key) throws Throwable {
	skeySpec = new SecretKeySpec(key, "AES");
	iv = generateParameterSpec();
    }

    /**
     * @param key
     *            bytes da chave
     * @param iv
     *            vetor
     * @throws Throwable
     */
    public AES(byte[] key, IvParameterSpec iv) throws Throwable {
	skeySpec = new SecretKeySpec(key, "AES");
	this.iv = iv;
    }

    /**
     * Gera um novo vetor.
     * 
     * @return vetor
     * @throws Throwable
     */
    public static IvParameterSpec generateParameterSpec() throws Throwable {
	byte[] newKey = generateSecretKey().getEncoded();
	return new IvParameterSpec(newKey);
    }

    /**
     * Gera um vetor e grava em arquivo
     * 
     * @param name
     *            nome do arquivo
     * @param publicKey
     *            chave pública
     * @return vetor
     * @throws Throwable
     */
    public static IvParameterSpec generateParameterSpecInFile(String name, PublicKey publicKey) throws Throwable {
	File vectorFile = new File(Constants.tempDir.getAbsolutePath() + File.separator + name + ".iv");
	byte[] newKey = generateSecretKey().getEncoded();
	// criptografa RSA e encoda em Base64
	IvParameterSpec iv = new IvParameterSpec(newKey);
	byte[] encrypt = MyBase64.encode(RSA.encrypt(iv.getIV(), publicKey));
	MyFileUtils.writeInSameLine(vectorFile, new String(encrypt));

	return iv;
    }

    /**
     * Obtém um vetor a partir de um arquivo.
     * 
     * @param name
     *            nome do arquivo
     * @param privateKey
     *            chave privada
     * @return vetor
     * @throws Throwable
     */
    public static IvParameterSpec getParameterSpecByFile(String name, PrivateKey privateKey) throws Throwable {
	File vectorFile = new File(Constants.tempDir.getAbsolutePath() + File.separator + name + ".iv");
	if (vectorFile.exists()) {
	    String line = MyFileUtils.readLine(vectorFile);
	    byte[] decrypt = RSA.decrypt(MyBase64.decode(line.getBytes()), privateKey);
	    return new IvParameterSpec(decrypt);
	}
	return null;
    }

    /**
     * Gera uma chave secreta.
     * 
     * @return chave secreta
     * @throws Throwable
     */
    public static SecretKey generateSecretKey() throws Throwable {
	KeyGenerator keyGen = KeyGenerator.getInstance("AES");
	keyGen.init(128);
	return keyGen.generateKey();
    }

    /**
     * Criptografa.
     * 
     * @param value
     *            bytes
     * @return bytes criptografados
     * @throws Throwable
     */
    public byte[] encrypt(byte[] value) throws Throwable {
	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
	return cipher.doFinal(value);
    }

    /**
     * Descriptografa.
     * 
     * @param encrypted
     *            bytes criptografados
     * @return texto descriptografado
     * @throws Throwable
     */
    public byte[] decrypt(byte[] encrypted) throws Throwable {
	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
	return cipher.doFinal(encrypted);
    }

    /**
     * Apenas para testes.
     * 
     * @param args
     * @throws Throwable
     */
    public static void main(String[] args) throws Throwable {
	AES aes = new AES("Bar12345Bar12345");
	byte[] encript = aes.encrypt("Hello World".getBytes());
	System.out.println(new String(encript));
	String decript = new String(aes.decrypt(encript));
	System.out.println(decript);
    }

}