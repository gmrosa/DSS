package br.com.furb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Auth {

    public static void main(String[] args) throws Throwable {
	File dir = new File(System.getProperty("java.io.tmpdir") + File.separator + "furbAccounts");
	File usersFile = new File(dir.getAbsolutePath() + File.separator + "users.csv");
	File permissionsFile = new File(dir.getAbsolutePath() + File.separator + "permissions.csv");

	if (!usersFile.exists()) {
	    AccountsGenerator.main(null);
	    PermissionsGenerator.main(null);
	}

	String user = "user1";
	String password = "password1";

	try (BufferedReader ubr = new BufferedReader(new FileReader(usersFile))) {
	    String uLine;
	    while ((uLine = ubr.readLine()) != null) {
		String[] uRecord = uLine.split(";");
		String fileUser = uRecord[0];

		if (fileUser.equals(user)) {
		    int uFileRole = Integer.parseInt(uRecord[1]);
		    String fileSalt = uRecord[2];
		    String fileHash = uRecord[3];

		    if (Sha256.getHash(fileSalt + password).equals(fileHash)) {
			Role role = Role.values()[uFileRole];
			System.out.println("Role - " + role);
			try (BufferedReader pbr = new BufferedReader(new FileReader(permissionsFile))) {
			    String pLine;
			    while ((pLine = pbr.readLine()) != null) {
				String[] pRecord = pLine.split(";");
				int pFileRole = Integer.parseInt(pRecord[0]);
				if (role.ordinal() == pFileRole) {
				    int fileObj = Integer.parseInt(pRecord[1]);
				    Obj obj = Obj.values()[fileObj];
				    int filePermission = Integer.parseInt(pRecord[2]);
				    Permission permission = Permission.values()[filePermission];
				    System.out.println(obj + " - " + permission);
				}
			    }
			}
		    } else {
			throw new IllegalArgumentException("invalid password!");
		    }
		    break;
		}
	    }
	}
    }

}
