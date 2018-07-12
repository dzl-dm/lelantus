package de.dzl.lelantus.client.crypto.rsa;

import java.security.GeneralSecurityException;
import java.security.PublicKey;

import de.dzl.lelantus.EncoderConfigurationException;
import de.dzl.lelantus.IdentityEncoder;
import de.dzl.lelantus.IdentityEncoderFactory;
import de.dzl.lelantus.ReferenceEncoder;
import de.dzl.lelantus.ReferenceEncoderFactory;

public class RSAIdentityEncoderFactory implements IdentityEncoderFactory, ReferenceEncoderFactory {
	protected PublicKey key;
	protected CharSequence pepper;
	protected char separator;

	/**
	 * Create instance using per default no pepper 
	 * and tabulator as identity part separator.
	 * @param key public key for the RSA encryption
	 */
	public RSAIdentityEncoderFactory(PublicKey key) {
		this.key = key;
		this.pepper = null;
		this.separator = '\t';
	}
	/**
	 * Pepper (salt) used to prefix all data before encryption.
	 * @param pepper encryption prefix
	 */
	public void setPepper(CharSequence pepper) {
		this.pepper = pepper;
	}
	/**
	 * Separator character used between identity parts (also after pepper)
	 * The character may not be used in the identity parts themselves.
	 * @param separator separator character (default is tabulator)
	 */
	public void setIdentityPartSeparator(char separator) {
		this.separator = separator;
	}
	@Override
	public String getCodingSystem() {
		return RSAIdentityEncoder.SYSTEM_ID;
	}

	public RSAIdentityEncoder createEncoder() throws EncoderConfigurationException{
		try {
			return new RSAIdentityEncoder(this);
		} catch (GeneralSecurityException e) {
			throw new EncoderConfigurationException("Unable to create encoder", e);
		}		
	}
	@Override
	public IdentityEncoder createIdentityEncoder() throws EncoderConfigurationException {
		return createEncoder();
	}
	@Override
	public ReferenceEncoder createReferenceEncoder() throws EncoderConfigurationException {
		return createEncoder();
	}

}
