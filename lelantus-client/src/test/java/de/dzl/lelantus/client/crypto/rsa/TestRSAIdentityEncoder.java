package de.dzl.lelantus.client.crypto.rsa;

import java.security.KeyPair;
import java.util.Arrays;

import org.junit.Test;
import static org.junit.Assert.*;

import de.dzl.lelantus.EncoderConfigurationException;
import de.dzl.lelantus.EncodingException;
import de.dzl.lelantus.IdentityEncoder;

public class TestRSAIdentityEncoder {
	private static RSAIdentityEncoderFactory createFactory() {
		KeyPair pair = RSAUtil.createKeyPair();
		RSAIdentityEncoderFactory f = new RSAIdentityEncoderFactory(pair.getPublic());
		return f;
	}

	@Test
	public void verifyCaseInsensitiveMatching() throws EncoderConfigurationException, EncodingException {
		RSAIdentityEncoderFactory f = createFactory();
		byte[] result1 = f.createIdentityEncoder().encodeIdentity("Arno", "Nym", "1992-03-18");
		byte[] result2 = f.createIdentityEncoder().encodeIdentity("arno", "Nym", "1992-03-18");
		
		assertArrayEquals(result1, result2);
	}

	@Test
	public void verifyStringRepresentation() throws EncoderConfigurationException, EncodingException {
		RSAIdentityEncoderFactory f = createFactory();
		IdentityEncoder i = f.createIdentityEncoder();
		byte[] result1 = i.encodeIdentity("Arno", "Nym", "1992-03-18");
		String str = i.convertToString(result1);
		// conversion to string and back should yield original byte array
		assertArrayEquals(result1, i.convertToCode(str));
	}

	@Test
	public void verifyPepperEffective() throws EncodingException, EncoderConfigurationException {
		RSAIdentityEncoderFactory f = createFactory();
		String[] i = new String[] {"Arno", "Nym", "1992-03-18"};
		String pepper1 = "xi2]-3E#";
		// no pepper
		byte[] result1 = f.createIdentityEncoder().encodeIdentity(i[0], i[1], i[2]);
		// with pepper
		f.setPepper(pepper1);
		byte[] result2 = f.createIdentityEncoder().encodeIdentity(i[0], i[1], i[2]);
		assertFalse("Same identity with different pepper should have different result",
				Arrays.equals(result1, result2));
		// different pepper
		f.setPepper("different");
		byte[] result3 = f.createIdentityEncoder().encodeIdentity(i[0], i[1], i[2]);
		assertFalse("Same identity with different pepper should have different result",
				Arrays.equals(result2, result3));

		// same pepper
		f.setPepper(pepper1);
		byte[] result4 = f.createIdentityEncoder().encodeIdentity(i[0], i[1], i[2]);
		assertArrayEquals("Same identity with same pepper should have same result",
				result2, result4);
	}
}
