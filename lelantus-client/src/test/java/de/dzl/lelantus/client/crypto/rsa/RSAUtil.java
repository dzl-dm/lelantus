package de.dzl.lelantus.client.crypto.rsa;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class RSAUtil {

	public static KeyPair createKeyPair() {
		return createKeyPair(2048);
	}
	public static KeyPair createKeyPair(int bits) {
	    KeyPairGenerator generatorRSA;
		try {
			generatorRSA = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		}
	    generatorRSA.initialize(bits, new SecureRandom());
	    KeyPair keyRSA = generatorRSA.generateKeyPair();
	    return keyRSA;
	}
}
