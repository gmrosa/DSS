package br.com.furb.cripto;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class PublicKeyRepository {

    private static PublicKeyRepository instance;
    private Map<String, PublicKey> publicKeys = new HashMap<>();

    private PublicKeyRepository() {
    }

    public static PublicKeyRepository getInstance() {
	if (instance == null) {
	    instance = new PublicKeyRepository();
	}
	return instance;
    }

    public PublicKey get(String user) {
	return publicKeys.get(user);
    }

    public void put(String user, PublicKey puk) {
	publicKeys.put(user, puk);
    }

}
