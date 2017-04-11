package br.com.furb.cripto;

import org.apache.commons.io.FileUtils;

/**
 * Representa a troca de mensagens entre usuários.
 * 
 * @author Guilherme.Rosa
 */
public class Conversation {

    /**
     * Apenas para testes
     * 
     * @param args
     * @throws Throwable
     */
    public static void main(String[] args) throws Throwable {
	// Chamar se quiser que limpe o diretório
	FileUtils.cleanDirectory(Constants.tempDir);

	Part a = new Part("usuarioA", "senhaA");
	// Part b = new Part("usuarioB", "senhaB");
	// Part c = new Part("usuarioC", "senhaC");
	// Part d = new Part("usuarioD", "senhaD");

	// System.out.println("a conectado com b:" + a.connect(b));
	// System.out.println("c conectado com a:" + c.connect(a));
	// System.out.println("b conectado com c:" + b.connect(c));

	// a.sendMessage(b, "1 a para b");
	// b.sendMessage(a, "2 b para a");
	// a.sendMessage(c, "3 a para c");
	// c.sendMessage(a, "4 c para a");
	// b.sendMessage(a, "5 b para a");
	// b.sendMessage(c, "6 b para c");
	//
	// a.sendMessage(d, "7 a para d");
    }

}
