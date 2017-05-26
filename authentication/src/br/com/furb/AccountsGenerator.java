package br.com.furb;

import java.io.File;
import java.io.PrintWriter;
import java.util.Random;
import java.util.UUID;

public class AccountsGenerator {

    public static void main(String[] args) throws Throwable {
	File dir = new File(System.getProperty("java.io.tmpdir") + File.separator + "furbAccounts");
	dir.mkdir();
	File file = new File(dir.getAbsolutePath() + File.separator + "data.csv");

	if (!file.exists()) {
	    file.createNewFile();
	}

	System.out.println(file.getAbsolutePath());

	try (PrintWriter out = new PrintWriter(file)) {
	    for (int i = 0; i < 100; i++) {
		String salt = UUID.randomUUID().toString();
		String user = "usuario" + i;
		String password = "senhateste" + i;
		String secret = salt + password;

		out.println(user + ";" + new Random().nextInt(2) + ";" + salt + ";" + Sha256.getHash(secret));
	    }
	}

    }

}
