package br.com.furb.cripto;

import java.io.IOException;

/**
 * Criptografia OneTimeXor.
 * 
 * @author Guilherme.Rosa
 */
public class OneTimeXor {

    private String key;

    /**
     * Construtor.
     * 
     * @param key chave
     */
    public OneTimeXor(String key) {
	this.key = key;
    }

    /**
     * Criptografa.
     * 
     * @param input entrada
     * @return bytes criptografados
     */
    public byte[] encrypt(byte[] input) {
	byte[] keyBytes = key.getBytes();
	byte[] outputBytes = xorWithKey(input, keyBytes);

	return MyBase64.encode(outputBytes);
    }

    /**
     * Descriptografa.
     * 
     * @param input entrada
     * @return bytes descriptografados
     */
    public byte[] decrypt(byte[] input) {
	byte[] inputBytes = MyBase64.decode(input);
	byte[] keyBytes = key.getBytes();
	return xorWithKey(inputBytes, keyBytes);
    }

    /**
     * Realiza o xor a partir da chave.
     * 
     * @param input entrada
     * @param key chave
     * @return bytes com xor
     */
    private static byte[] xorWithKey(byte[] input, byte[] key) {
	byte[] output = new byte[input.length];

	int keyI = 0;
	for (int i = 0; i < output.length; i++, keyI++) {
	    output[i] = (byte) (input[i] ^ key[keyI]);
	    /*
	     * Aqui tem uma sacada fenomenal,
	     * a chave não precisa ter o mesmo tamanho do texto.
	     * Basta reiniciar o índice.
	     */
	    if (keyI + 1 == key.length) {
		keyI = -1;
	    }
	}
	return output;
    }

    /**
     * Apenas para testes.
     * 
     * @param args
     * @throws Throwable
     */
    public static void main(String args[]) throws IOException {
	String plainText = "Vamos fazer um teste maior. Será que vai funcionar agora, Guilherme? Teste com chave de tamanho diferente do texto.";
	String key = "Atenção!Atenção!Atenção!Atenção!Atenção!Atenção!Atenção!Atenção!Aten";

	OneTimeXor otx = new OneTimeXor(key);
	byte[] encrypt = otx.encrypt(plainText.getBytes());
	byte[] decrypt = otx.decrypt(encrypt);
	System.out.println(new String(decrypt));
    }

}
