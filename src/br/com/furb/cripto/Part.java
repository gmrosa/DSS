package br.com.furb.cripto;

import java.io.File;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.io.FileUtils;

public class Part {

    private final String name;
    private AES passwordAES;
    private KeyPair keyPair;

    private Map<String/* Name */, SecretKey> secretKeys = new HashMap<>();
    private Map<String/* Name */, IvParameterSpec> vectors = new HashMap<>();

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

    private PrivateKey getPrivateKey() throws Throwable {
	Constants.tempDir.mkdirs();
	File keyFile = new File(Constants.tempDir.getAbsolutePath() + File.separator + name + ".pvk");

	if (!keyFile.exists()) {
	    keyFile.createNewFile();

	    String encrypted = new String(MyBase64.encode(passwordAES.encrypt(keyPair.getPrivate().getEncoded())));
	    MyFileUtils.writeInSameLine(keyFile, encrypted);
	}
	byte[] encrypted = passwordAES.decrypt(MyBase64.decode(MyFileUtils.readLine(keyFile).getBytes()));
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

	    MyFileUtils.writeInSameLine(keyFile, new String(MyBase64.encode(keyPair.getPublic().getEncoded())));
	    newKey = true;
	}
	String encodec = MyFileUtils.readLine(keyFile);
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

    public void sendMessage(Part otherPart, String msg) throws Throwable {
	if (secretKeys.containsKey(otherPart.getName())) {
	    System.out.println("\"" + name + "\" enviou mensagem para \"" + otherPart.getName() + "\" mensagem: " + msg);
	    SecretKey otherPartSecretKey = secretKeys.get(otherPart.getName());
	    IvParameterSpec iv = generateVectorByName(otherPart.getName());
	    AES otherAES = new AES(otherPartSecretKey.getEncoded(), iv);
	    byte[] data = otherAES.encrypt(msg.getBytes());
	    byte[] dataWithSignature = RSASignature.getSignature(data, getPrivateKey());
	    otherPart.receiveMessage(this, data, dataWithSignature);
	} else {
	    System.out.println("\"" + name + "\" não está conectado com \"" + otherPart.getName() + "\"");
	}
    }

    public IvParameterSpec generateVectorByName(String name) throws Throwable {
	if (vectors.containsKey(name)) {
	    return vectors.get(name);
	} else {
	    IvParameterSpec vector = AES.getParameterSpecByFile(name, PublicKeyRepository.getInstance().get(name));
	    vectors.put(name, vector);
	    return vector;
	}
    }

    private void receiveMessage(Part part, byte[] data, byte[] dataWithSignature) throws Throwable {
	if (RSASignature.validateSignature(dataWithSignature, data, PublicKeyRepository.getInstance().get(part.getName()))) {
	    SecretKey partSecretKey = secretKeys.get(part.getName());
	    AES otherAES = new AES(partSecretKey.getEncoded(), getVectorByName(part.getName()));
	    byte[] decrypt = otherAES.decrypt(data);
	    System.out.println("\"" + name + "\" recebeu mensagem de \"" + part.getName() + "\" mensagem " + new String(decrypt));
	} else {
	    System.out.println("\"" + name + "\" não está conectado com \"" + part.getName() + "\"");
	}
    }

    public IvParameterSpec getVectorByName(String name) throws Throwable {
	if (vectors.containsKey(name)) {
	    return vectors.get(name);
	} else {
	    IvParameterSpec iv = AES.getParameterSpecByFile(this.getName(), getPrivateKey());
	    vectors.put(name, iv);
	    return iv;
	}
    }

    public static void main(String[] args) throws Throwable {
	FileUtils.cleanDirectory(Constants.tempDir);

	Part part = new Part("teste");
	String text = "meu texto";
	byte[] encrypted = RSA.encrypt(text, part.getPublicKey());
	System.out.println(new String(RSA.decrypt(encrypted, part.getPrivateKey())));
    }

}
