package de.dzl.lelantus.client.crypto.rsa;

import java.security.GeneralSecurityException;

import de.dzl.lelantus.EncodingException;
import de.dzl.lelantus.IdentityEncoder;
import de.dzl.lelantus.ReferenceEncoder;

public class RSAIdentityEncoder extends DeterministicRSAEncoder implements IdentityEncoder, ReferenceEncoder{
	public static final String SYSTEM_ID = "lelaRSA";

	protected RSAIdentityEncoder(RSAIdentityEncoderFactory factory) throws GeneralSecurityException {
		super(DeterministicRSAEncoder.loadEncryptCipher(factory.key),
				factory.separator,
				factory.pepper);
	}

	@Override
	public String getCodingSystem() {
		return SYSTEM_ID;
	}

	@Override
	public byte[] encodeIdentity(String givenName, String lastName, String birthDate) throws EncodingException {
		try {
			return encryptIdent(givenName,lastName,birthDate);
		} catch (GeneralSecurityException e) {
			throw new EncodingException("Unable to calculate cryptographic pseudonym", e);
		}
	}

	@Override
	public byte[] encodeReference(String reference, String system) throws EncodingException {
		try {
			return encryptReference(reference, system);
		} catch (GeneralSecurityException e) {
			throw new EncodingException("Unable to calculate cryptographic pseudonym", e);
		}
	}

	@Override
	public int getCodeLength() {
		return super.getBlockSize();
	}

}
