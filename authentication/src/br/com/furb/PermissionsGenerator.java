package br.com.furb;

import java.io.File;
import java.io.PrintWriter;
import java.util.Random;

public class PermissionsGenerator {

    public static void main(String[] args) throws Throwable {
	File dir = new File(System.getProperty("java.io.tmpdir") + File.separator + "furbAccounts");
	dir.mkdir();
	File file = new File(dir.getAbsolutePath() + File.separator + "permissions.csv");

	if (!file.exists()) {
	    file.createNewFile();
	}

	System.out.println(file.getAbsolutePath());

	try (PrintWriter out = new PrintWriter(file)) {
	    for (Role role : Role.values()) {
		for (Obj obj : Obj.values()) {
		    out.println(role.ordinal() + ";" + obj.ordinal() + ";" + new Random().nextInt(2));
		}
	    }
	}
    }

}
