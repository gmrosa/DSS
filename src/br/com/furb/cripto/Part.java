package br.com.furb.cripto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.apache.commons.io.FileUtils;

public class Part {

    private final String name;
    private AES passwordAES;

    /**
     * 1) Gerar chave simétrica da conversa / vetor inicialização
     * 2) Criptografar chave simétrica com a chave publica de B
     * 3) Assinar msg com a chave privada de A
     * 
     * 1) Verificar assinatura publica A
     * 2) descriptografar pvt B
     * 3) Guardar chave simétrica da conversa
     * 
     */
    private KeyPair keyPair;

    private Map<String/* Name */, SecretKey> secretKeys = new HashMap<>();

    public Part(String name) throws Throwable {
	this.name = name;
	keyPair = RSA.generateKeyPair();
	// primeiros 128 bytes do hash usar como chave simétrica para criptografar pvk
	String password = MD5.generateHash128(AES.generateSecretKey());
	passwordAES = new AES(password);
	getPrivateKey();
	getPublicKey();
    }

    public boolean connect(Part otherPart) throws Throwable {
	if (otherPart.containsKey(name)) {
	    return true;
	} else {
	    byte[] wrapedSecretKey = generateWrapedSecretKey(otherPart.getName());
	    byte[] signature = RSASignature.getSignature(wrapedSecretKey, getPrivateKey());
	    return otherPart.put(name, signature, wrapedSecretKey);
	}
    }

    public byte[] generateWrapedSecretKey(String otherName) throws Throwable {
	SecretKey secretKey;
	if (containsKey(otherName)) {
	    secretKey = secretKeys.get(otherName);
	} else {
	    secretKey = generateSecretKey(otherName);
	}
	return RSA.wrapKey(PublicKeyRepository.getInstance().get(otherName), secretKey);
    }

    private SecretKey generateSecretKey(String otherName) throws Throwable {
	SecretKey secretKey = AES.generateSecretKey();
	secretKeys.put(otherName, secretKey);
	return secretKey;
    }

    public boolean containsKey(String name) {
	return secretKeys.containsKey(name);
    }

    public boolean put(String otherName, byte[] signature, byte[] wrapedSecretKey) throws Throwable {
	if (RSASignature.validateSignature(signature, wrapedSecretKey, PublicKeyRepository.getInstance().get(otherName))) {
	    secretKeys.put(otherName, RSA.unwrapKey(getPrivateKey(), wrapedSecretKey));
	    return true;
	}
	return false;
    }

    private static void writeInSameLine(File file, String txt) throws Throwable {
	try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
	    writer.write(txt);
	    writer.flush();
	}
    }

    private static String readLine(File file) throws Throwable {
	try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
	    return reader.readLine();
	}
    }

    private PrivateKey getPrivateKey() throws Throwable {
	Constants.tempDir.mkdirs();
	File keyFile = new File(Constants.tempDir.getAbsolutePath() + File.separator + name + ".pvk");

	if (!keyFile.exists()) {
	    keyFile.createNewFile();

	    String encrypted = new String(MyBase64.encode(passwordAES.encrypt(keyPair.getPrivate().getEncoded())));
	    writeInSameLine(keyFile, encrypted);
	}
	byte[] encrypted = passwordAES.decrypt(MyBase64.decode(readLine(keyFile).getBytes()));
	PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encrypted);
	KeyFactory kf = KeyFactory.getInstance("RSA");
	return kf.generatePrivate(keySpec);
    }

    private PublicKey getPublicKey() throws Throwable {
	boolean newKey = false;
	Constants.tempDir.mkdirs();
	File keyFile = new File(Constants.tempDir.getAbsolutePath() + File.separator + name + ".puk");

	if (!keyFile.exists()) {
	    keyFile.createNewFile();

	    writeInSameLine(keyFile, new String(MyBase64.encode(keyPair.getPublic().getEncoded())));
	    newKey = true;
	}
	String encodec = readLine(keyFile);
	X509EncodedKeySpec spec = new X509EncodedKeySpec(MyBase64.decode(encodec.getBytes()));
	KeyFactory kf = KeyFactory.getInstance("RSA");

	PublicKey puk = kf.generatePublic(spec);
	if (newKey) {
	    // Troca mensagem com o repositório armazenar a chave pública
	    PublicKeyRepository.getInstance().put(name, puk);
	}
	return puk;
    }

    public String getName() {
	return name;
    }

    public static void main(String[] args) throws Throwable {
	FileUtils.cleanDirectory(Constants.tempDir);

	Part part = new Part("teste");
	String text = "meu texto";
	byte[] encrypted = RSA.encrypt(text, part.getPublicKey());
	System.out.println(new String(RSA.decrypt(encrypted, part.getPrivateKey())));
    }

}
