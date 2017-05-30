package br.com.furb;

import java.io.File;
import java.io.PrintWriter;
import java.util.Random;
import java.util.UUID;

public class AccountsGenerator {

    public static void main(String[] args) throws Throwable {
	File dir = new File(System.getProperty("java.io.tmpdir") + File.separator + "furbAccounts");
	dir.mkdir();
	File usersFile = new File(dir.getAbsolutePath() + File.separator + "users.csv");

	if (!usersFile.exists()) {
	    usersFile.createNewFile();
	}

	System.out.println(usersFile.getAbsolutePath());

	try (PrintWriter out = new PrintWriter(usersFile)) {
	    for (int i = 0; i < 100; i++) {
		String salt = UUID.randomUUID().toString();
		String user = "usuario" + i;
		String password = "senhateste" + i;
		String secret = salt + password;

		out.println(user + ";" + new Random().nextInt(3) + ";" + salt + ";" + Sha256.getHash(secret));
	    }
	}

    }

}
