package br.com.furb.cripto;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * Reposit�rio de chaves p�blicas
 * 
 * @author Guilherme.Rosa
 */
public class PublicKeyRepository {

    private static PublicKeyRepository instance;
    private Map<String, PublicKey> publicKeys = new HashMap<>();

    /**
     * Privado para a inst�ncia ser obtida apenas pelo singleton
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
     * Obt�m uma chave p�blica de um usu�rio.
     * 
     * @param user nome do usu�rio
     * @return chave p�blica de um usu�rio
     */
    public PublicKey get(String user) {
	return publicKeys.get(user);
    }

    /**
     * Adiciona uma chave p�blica de um usu�rio.
     * 
     * @param user nome do usu�rio
     * @param puk chave p�blica
     */
    public void put(String user, PublicKey puk) {
	publicKeys.put(user, puk);
    }

}
