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

/**
 * Classe com o prop�sito de representar um usu�rio.
 * 
 * @author Guilherme.Rosa
 */
public class Part {

    private final String name;
    private AES passwordAES;
    private KeyPair keyPair;

    private Map<String/* Name */, SecretKey> secretKeys = new HashMap<>();
    private Map<String/* Name */, IvParameterSpec> vectors = new HashMap<>();

    /**
     * Construtor.
     * 
     * @param name
     *            nome do usu�rio
     * @throws Throwable
     */
    public Part(String name) throws Throwable {
	this.name = name;
	keyPair = RSA.generateKeyPair();
	// primeiros 128 bytes do hash usar como chave sim�trica para criptografar pvk
	String password = MD5.generateHash128(AES.generateSecretKey());
	passwordAES = new AES(password);
	getPrivateKey();
	getPublicKey();
    }

    /**
     * Conecta com outro usu�rio.
     * 
     * @param otherPart
     *            outro usu�rio
     * @return true se conectou com sucesso
     * @throws Throwable
     */
    public boolean connect(Part otherPart) throws Throwable {
	if (otherPart.containsKey(name)) {
	    // Se j� tem armazenado a chave secreta � por que j� se conectou
	    return true;
	} else {
	    // Nova conex�o
	    byte[] wrapedSecretKey = generateWrapedSecretKey(otherPart.getName());
	    byte[] signature = RSASignature.getSignature(wrapedSecretKey, getPrivateKey());
	    return otherPart.put(name, signature, wrapedSecretKey);
	}
    }

    /**
     * Envia uma mensagem para outro usu�rio.
     * 
     * @param otherPart
     *            outro usu�rio
     * @param msg
     *            mensagem
     * @throws Throwable
     */
    public void sendMessage(Part otherPart, String msg) throws Throwable {
	// S� pode trocar mensagens caso j� se conectaram
	if (secretKeys.containsKey(otherPart.getName())) {
	    System.out.println("\"" + name + "\" enviou mensagem para \"" + otherPart.getName() + "\" mensagem: " + msg);
	    // Criptografa a mensagem
	    SecretKey otherPartSecretKey = secretKeys.get(otherPart.getName());
	    IvParameterSpec iv = generateVectorByName(otherPart.getName());
	    AES otherAES = new AES(otherPartSecretKey.getEncoded(), iv);
	    byte[] data = otherAES.encrypt(msg.getBytes());
	    // Gera assinatura
	    byte[] dataWithSignature = RSASignature.getSignature(data, getPrivateKey());
	    otherPart.receiveMessage(this, data, dataWithSignature);
	} else {
	    // N�o se conectou, tem que usar o m�todo connect(); primeiro
	    System.out.println("\"" + name + "\" n�o est� conectado com \"" + otherPart.getName() + "\"");
	}
    }

    /**
     * Recebe uma mensagem de outro usu�rio.
     * 
     * @param part
     * @param data
     * @param dataWithSignature
     * @throws Throwable
     */
    public void receiveMessage(Part part, byte[] data, byte[] dataWithSignature) throws Throwable {
	// Valida assinatura
	if (RSASignature.validateSignature(dataWithSignature, data, PublicKeyRepository.getInstance().get(part.getName()))) {
	    // Descriptografia a mensagem
	    SecretKey partSecretKey = secretKeys.get(part.getName());
	    AES otherAES = new AES(partSecretKey.getEncoded(), getVectorByName(part.getName()));
	    byte[] decrypt = otherAES.decrypt(data);

	    String message = new String(decrypt);
	    System.out.println("\"" + name + "\" recebeu mensagem de \"" + part.getName() + "\" mensagem " + message);
	} else {
	    // Inv�lido
	    System.out.println("Usu�rio \"" + part.getName() + "\" est� com assinatura inv�lida");
	}
    }

    /**
     * Gera uma chave secreta criptografada com RSA.
     * 
     * @param otherName
     *            nome do outro usu�rio
     * @return chave secreta criptografada com RSA
     * @throws Throwable
     */
    private byte[] generateWrapedSecretKey(String otherName) throws Throwable {
	SecretKey secretKey;
	if (containsKey(otherName)) {
	    // J� tem armazenado a chave secreta
	    secretKey = secretKeys.get(otherName);
	} else {
	    // Nova chave secreta
	    secretKey = generateSecretKey(otherName);
	}
	// Criptografa com RSA
	return RSA.wrapKey(PublicKeyRepository.getInstance().get(otherName), secretKey);
    }

    /**
     * Gera uma nova chave secreta.
     * 
     * @param otherName
     *            nome do outro usu�rio
     * @return chave secreta
     * @throws Throwable
     */
    private SecretKey generateSecretKey(String otherName) throws Throwable {
	SecretKey secretKey = AES.generateSecretKey();
	secretKeys.put(otherName, secretKey);
	return secretKey;
    }

    /**
     * Se j� tem a chave secreta entre um usu�rio espec�fico.
     * 
     * @param otherName
     *            nome do outro usu�rio
     * @return true se j� possui a chave secreta entre ambas as partes
     */
    private boolean containsKey(String otherName) {
	return secretKeys.containsKey(otherName);
    }

    /**
     * Adiciona a chave secreta somente se garantir a autenticidade.
     * 
     * @param otherName
     *            nome do outro usu�rio
     * @param signature
     *            assinatura do outro usu�rio
     * @param wrapedSecretKey
     *            chave secreta criptografada
     * @return
     * @throws Throwable
     */
    private boolean put(String otherName, byte[] signature, byte[] wrapedSecretKey) throws Throwable {
	// Valida assinatura
	if (RSASignature.validateSignature(signature, wrapedSecretKey, PublicKeyRepository.getInstance().get(otherName))) {
	    // V�lido
	    secretKeys.put(otherName, RSA.unwrapKey(getPrivateKey(), wrapedSecretKey));
	    return true;
	}
	// Inv�lido
	return false;
    }

    /**
     * Obt�m a chave privada a partir de um arquivo.
     * 
     * @return chave privada
     * @throws Throwable
     */
    private PrivateKey getPrivateKey() throws Throwable {
	Constants.tempDir.mkdirs();
	File keyFile = new File(Constants.tempDir.getAbsolutePath() + File.separator + name + ".pvk");

	// Se n�o tem gera um novo arquivo
	if (!keyFile.exists()) {
	    keyFile.createNewFile();

	    String encrypted = new String(MyBase64.encode(passwordAES.encrypt(keyPair.getPrivate().getEncoded())));
	    MyFileUtils.writeInSameLine(keyFile, encrypted);
	}
	// L� o arquivo
	byte[] encrypted = passwordAES.decrypt(MyBase64.decode(MyFileUtils.readLine(keyFile).getBytes()));
	// Gera a chave privada
	PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encrypted);
	KeyFactory kf = KeyFactory.getInstance("RSA");
	return kf.generatePrivate(keySpec);
    }

    /**
     * Obt�m a chave p�blica a partir de um arquivo.
     * 
     * @return chave p�blica
     * @throws Throwable
     */
    private PublicKey getPublicKey() throws Throwable {
	boolean newKey = false;
	Constants.tempDir.mkdirs();
	File keyFile = new File(Constants.tempDir.getAbsolutePath() + File.separator + name + ".puk");

	// Se n�o tem gera um novo arquivo
	if (!keyFile.exists()) {
	    keyFile.createNewFile();

	    MyFileUtils.writeInSameLine(keyFile, new String(MyBase64.encode(keyPair.getPublic().getEncoded())));
	    newKey = true;
	}
	// L� o arquivo
	String encodec = MyFileUtils.readLine(keyFile);
	// Gera a chave p�blica
	X509EncodedKeySpec spec = new X509EncodedKeySpec(MyBase64.decode(encodec.getBytes()));
	KeyFactory kf = KeyFactory.getInstance("RSA");

	PublicKey puk = kf.generatePublic(spec);
	if (newKey) {
	    // Troca mensagem com o reposit�rio armazenar uma nova chave p�blica
	    PublicKeyRepository.getInstance().put(name, puk);
	}
	return puk;
    }

    /**
     * Gera um vetor a partir de um nome de usu�rio.
     * 
     * @param otherName
     *            nome do outro usu�rio
     * @return vetor
     * @throws Throwable
     */
    public IvParameterSpec generateVectorByName(String otherName) throws Throwable {
	if (vectors.containsKey(otherName)) {
	    return vectors.get(otherName);
	} else {
	    IvParameterSpec vector = AES.generateParameterSpecInFile(otherName, PublicKeyRepository.getInstance().get(otherName));
	    vectors.put(otherName, vector);
	    return vector;
	}
    }

    /**
     * Obt�m o vetor a partir de um nome de usu�rio.
     * 
     * @param name
     *            nome do outro usu�rio
     * @return vetor
     * @throws Throwable
     */
    public IvParameterSpec getVectorByName(String name) throws Throwable {
	if (vectors.containsKey(name)) {
	    return vectors.get(name);
	} else {
	    IvParameterSpec iv = AES.getParameterSpecByFile(this.getName(), getPrivateKey());
	    vectors.put(name, iv);
	    return iv;
	}
    }

    /**
     * @return nome do usu�rio
     */
    public String getName() {
	return name;
    }

    /**
     * Apenas para testes.
     * 
     * @param args
     * @throws Throwable
     */
    public static void main(String[] args) throws Throwable {
	FileUtils.cleanDirectory(Constants.tempDir);

	Part part = new Part("teste");
	String text = "meu texto";
	byte[] encrypted = RSA.encrypt(text, part.getPublicKey());
	System.out.println(new String(RSA.decrypt(encrypted, part.getPrivateKey())));
    }

}
