package br.com.furb;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

public class Part {

	private final String name;
	// TODO Chave simétrica SHA1 512
	// private String password;

	private PrivateKey pvk;
	private PublicKey puk;
	private KeyPair keyPair;

	private Map<String, SecretKey> secretKeys = new HashMap<>();

	public static void main(String[] args) throws Throwable {
		Part part = new Part("teste");

		String text = "meu texto";

		byte[] encrypted = RSA.criptografar(text, part.getPublicKey());
		System.out.println(new String(RSA.decriptografar(encrypted, part.getPrivateKey())));
	}

	public Part(String name) throws Throwable {
		this.name = name;
		keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
		pvk = getPvtKey();
		puk = getPubKey();
	}

	public void put(String key, SecretKey value) {
		secretKeys.put(key, value);
	}

	private PrivateKey getPvtKey() throws Throwable {
		File path = new File(System.getProperty("java.io.tmpdir") + File.separator + "tratamsg" + File.separator);
		path.mkdirs();
		File keyFile = new File(path.getAbsolutePath() + File.separator + name + ".pvk");

		if (!keyFile.exists()) {
			keyFile.createNewFile();

			writeInSameLine(keyFile, new String(MyBase64.encode(keyPair.getPrivate().getEncoded())));
		}
		return getPrivateKey(keyFile);
	}

	private static PrivateKey getPrivateKey(File privateKeyFile) throws Throwable {
		String encoded = readLine(privateKeyFile);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(MyBase64.decode(encoded));
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePrivate(keySpec);
	}

	private PublicKey getPubKey() throws Throwable {
		File path = new File(System.getProperty("java.io.tmpdir") + File.separator + "tratamsg" + File.separator);
		path.mkdirs();
		File keyFile = new File(path.getAbsolutePath() + File.separator + name + ".puk");

		if (!keyFile.exists()) {
			keyFile.createNewFile();

			writeInSameLine(keyFile, new String(MyBase64.encode(keyPair.getPublic().getEncoded())));
		}
		return getPublicKey(keyFile);
	}

	private static PublicKey getPublicKey(File publicKeyFile) throws Throwable {
		String encodec = readLine(publicKeyFile);
		X509EncodedKeySpec spec = new X509EncodedKeySpec(MyBase64.decode(encodec.getBytes()));
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePublic(spec);
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

	public PrivateKey getPrivateKey() {
		return pvk;
	}

	public PublicKey getPublicKey() {
		return puk;
	}

}
