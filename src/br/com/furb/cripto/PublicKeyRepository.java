package br.com.furb.cripto;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * Repositório de chaves públicas
 * 
 * @author Guilherme.Rosa
 */
public class PublicKeyRepository {

    private static PublicKeyRepository instance;
    private Map<String, PublicKey> publicKeys = new HashMap<>();

    /**
     * Privado para a instância ser obtida apenas pelo singleton
     */
    private PublicKeyRepository() {
    }

    /**
     * @return singleton
     */
    public static PublicKeyRepository getInstance() {
	if (instance == null) {
	    instance = new PublicKeyRepository();
	}
	return instance;
    }

    /**
     * Obtém uma chave pública de um usuário.
     * 
     * @param user nome do usuário
     * @return chave pública de um usuário
     */
    public PublicKey get(String user) {
	return publicKeys.get(user);
    }

    /**
     * Adiciona uma chave pública de um usuário.
     * 
     * @param user nome do usuário
     * @param puk chave pública
     */
    public void put(String user, PublicKey puk) {
	publicKeys.put(user, puk);
    }

}
