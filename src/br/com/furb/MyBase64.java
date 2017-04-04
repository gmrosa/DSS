package br.com.furb;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class MyBase64 {

	public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	public static String encode(String txt) {
		return Base64.getEncoder().encodeToString(txt.getBytes());
	}

	public static byte[] encode(byte[] bytes) {
		return Base64.getEncoder().encode(bytes);
	}

	public static byte[] decode(String txt) {
		return Base64.getDecoder().decode(txt.getBytes(DEFAULT_CHARSET));
	}

	public static byte[] decode(byte[] bytes) {
		return Base64.getDecoder().decode(bytes);
	}

	public static void main(String[] args) {
		System.out.println(decode(encode("Teste  çÃo")));
	}

}
