package br.com.furb;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class OneTimeXor {

	private static final Charset DEFAULT_CHARSET = StandardCharsets.US_ASCII;

	public static String criptografar(String entrada, String chave) {
		byte[] bytesEntrada = entrada.getBytes(DEFAULT_CHARSET);
		byte[] bytesChave = chave.getBytes(DEFAULT_CHARSET);
		byte[] bytesSaida = XorComChave(bytesEntrada, bytesChave);
		// encoda
		String saida = Base64.getEncoder().encodeToString(bytesSaida);
		return saida;
	}

	public static String descriptografar(String entrada, String chave) {
		// decoda
		byte[] bytesEntrada = Base64.getDecoder().decode(entrada.getBytes(DEFAULT_CHARSET));
		byte[] bytesChave = chave.getBytes(DEFAULT_CHARSET);
		byte[] bytesSaida = XorComChave(bytesEntrada, bytesChave);
		String saida = new String(bytesSaida);
		return saida;
	}

	private static byte[] XorComChave(byte[] entrada, byte[] chave) {
		byte[] saida = new byte[entrada.length];

		if (entrada.length != chave.length) {
			throw new IllegalArgumentException("Texto e cifra devem ter o mesmo tamanho");
		}

		for (int i = 0; i < saida.length; i++) {
			saida[i] = (byte) (entrada[i] ^ chave[i]);
		}

		return saida;
	}

	public static void main(String args[]) throws IOException {
		String plano = "Vamos fazer um teste maior. Será que vai funcionar agora, Guilherme?";
		String chave = "Atenção!Atenção!Atenção!Atenção!Atenção!Atenção!Atenção!Atenção!Aten";

		String cifrado = criptografar(plano, chave);

		System.out.println(cifrado);
		String voltaParaPlano = descriptografar(cifrado, chave);
		System.out.println(voltaParaPlano);
	}

}
