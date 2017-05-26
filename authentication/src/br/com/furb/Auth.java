package br.com.furb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Auth {

    public static void main(String[] args) throws Throwable {
	File dir = new File(System.getProperty("java.io.tmpdir") + File.separator + "furbAccounts");
	File file = new File(dir.getAbsolutePath() + File.separator + "data.csv");

	String user = "usuario1";
	String password = "senhateste1";

	try (BufferedReader br = new BufferedReader(new FileReader(file))) {
	    String line;
	    while ((line = br.readLine()) != null) {
		String[] record = line.split(";");
		String fileUser = record[0];

		if (fileUser.equals(user)) {
		    int permission = Integer.parseInt(record[1]);
		    String fileSalt = record[2];
		    String fileHash = record[3];

		    if (Sha256.getHash(fileSalt + password).equals(fileHash)) {
			System.out.println(Permission.values()[permission]);
		    } else {
			throw new IllegalArgumentException("senha inválida");
		    }
		    break;
		}
	    }
	}
    }

}
