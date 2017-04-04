package br.com.furb.cripto;

import java.io.IOException;

public class OneTimePad {

    private static final int MIN_POS = 65;
    private static final int MOD_NUM = 26;

    private static String padronizarEntrada(String entrada) {
	return entrada.replace(" ", "").replaceAll("/[^A-Za-z0-9 ]/", "").toUpperCase();
    }

    private static String criptografar(String textoPlano, String chaveUsoUnico) {

	if (textoPlano.length() != chaveUsoUnico.length()) {
	    throw new IllegalArgumentException("Texto e cifra devem ter o mesmo tamanho");
	}

	StringBuilder textoCriptografado = new StringBuilder();

	for (int i = 0; i < textoPlano.length(); i++) {
	    int c1 = textoPlano.charAt(i) - MIN_POS;
	    int c2 = chaveUsoUnico.charAt(i) - MIN_POS;
	    int c3 = (MOD_NUM + c1 + c2) % MOD_NUM;
	    char c = (char) (c3 + MIN_POS);
	    textoCriptografado.append(c);
	}
	return textoCriptografado.toString();
    }

    public static String descriptografar(String textoCifrado, String chaveUsoUnico) {

	if (textoCifrado.length() != chaveUsoUnico.length()) {
	    throw new IllegalArgumentException("Texto e cifra devem ter o mesmo tamanho");
	}

	String textoCriptografado = "";

	for (int i = 0; i < textoCifrado.length(); i++) {
	    int c1 = textoCifrado.charAt(i) - MIN_POS;
	    int c2 = chaveUsoUnico.charAt(i) - MIN_POS;
	    int c3 = (MOD_NUM + c1 - c2) % MOD_NUM;
	    char c = (char) (c3 + MIN_POS);
	    textoCriptografado += c;
	}
	return textoCriptografado;
    }

    public static void main(String args[]) throws IOException {

	String entrada = "Boa aula";
	String plano = padronizarEntrada(entrada);
	String chave = "KPABWKN";
	String cifrado;

	cifrado = criptografar(plano, chave);
	System.out.println(cifrado);
	String voltaParaPlano = descriptografar(cifrado, chave);
	System.out.println(voltaParaPlano);
    }

}
