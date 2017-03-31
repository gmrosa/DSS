package br.com.furb;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class PublicKeyRepository {

	private Map<String, PublicKey> publicKeys = new HashMap<>();
	private static final String NAME = "name";

	public static void main(String[] args) throws Throwable {
		PublicKeyRepository pkr = new PublicKeyRepository();
		System.out.println(pkr.publicKeys.size());
	}

	public PublicKeyRepository() throws Throwable {
		for (int i = 0; i < 100; i++) {
			String name = (NAME + i);
			Part part = new Part(name);
			publicKeys.put(name, part.getPublicKey());
		}
	}

}
