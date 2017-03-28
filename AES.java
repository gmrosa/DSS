package br.com.furb;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {

	private static final String key = "Bar12345Bar12345"; // 128 bit key
	private static SecretKeySpec skeySpec;
	private static IvParameterSpec iv;

	public static byte[] criptografar(String value) {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
			keyGenerator.init(128);
			skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
			byte[] newKey = keyGenerator.generateKey().getEncoded();
			// System.out.println(new String(newKey));
			iv = new IvParameterSpec(newKey);

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

			byte[] encrypted = cipher.doFinal(value.getBytes());
			return MyBase64.encode(encrypted);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public static String decriptografar(byte[] encrypted) throws Throwable {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

		byte[] original = cipher.doFinal(MyBase64.decode(encrypted));

		return new String(original);
	}

	public static void main(String[] args) throws Throwable {
		byte[] encript = criptografar("Hello World");
		System.out.println(new String(encript));
		String decript = decriptografar(encript);
		System.out.println(decript);
	}

	public static class MyBase64 {

		private static final Charset DEFAULT_CHARSET = StandardCharsets.US_ASCII;

		public static String encode(String txt) {
			return Base64.getEncoder().encodeToString(txt.getBytes());
		}

		public static byte[] encode(byte[] bytes) {
			return Base64.getEncoder().encode(bytes);
		}

		public static String decode(String txt) {
			return new String(Base64.getDecoder().decode(txt.getBytes(DEFAULT_CHARSET)));
		}

		public static byte[] decode(byte[] bytes) {
			return Base64.getDecoder().decode(bytes);
		}

		public static void main(String[] args) {
			System.out.println(decode(encode("Teste  çÃo")));
		}

	}

}