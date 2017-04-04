package br.com.furb.cripto;

import org.apache.commons.io.FileUtils;

public class Conversation {

    public static void main(String[] args) throws Throwable {
	FileUtils.cleanDirectory(Constants.tempDir);

	Part a = new Part("a");
	Part b = new Part("b");
	Part c = new Part("c");

	System.out.println(a.connect(b));
	System.out.println(a.connect(b));
	System.out.println(b.connect(c));
    }

}
