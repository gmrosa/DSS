package br.com.furb.cripto;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

public class RSASignature {

    public static KeyPair genetareKeyPair() throws Throwable {
	KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
	kpg.initialize(1024);
	return kpg.genKeyPair();
    }

    public static byte[] getSignature(byte[] data, PrivateKey pvt) throws Throwable {
	Signature sig = Signature.getInstance("MD5WithRSA");
	sig.initSign(pvt);
	sig.update(data);
	byte[] signatureBytes = sig.sign();
	return MyBase64.encode(signatureBytes);
    }

    public static boolean validateSignature(byte[] signatureBytes, byte[] data, PublicKey pub) throws Throwable {
	Signature sig = Signature.getInstance("MD5WithRSA");
	sig.initVerify(pub);
	sig.update(data);
	return sig.verify(MyBase64.decode(signatureBytes));
    }

    public static void main(String[] args) throws Throwable {
	KeyPair keyPair = genetareKeyPair();

	byte[] data = "mensagem".getBytes(StandardCharsets.UTF_8);
	System.out.println("main" + new String(data));
	byte[] signatureBytes = getSignature(data, keyPair.getPrivate());

	System.out.println(validateSignature(signatureBytes, data, keyPair.getPublic()));
    }

}
