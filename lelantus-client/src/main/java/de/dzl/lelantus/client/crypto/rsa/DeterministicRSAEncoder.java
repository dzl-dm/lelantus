package de.dzl.lelantus.client.crypto.rsa;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.util.Objects;

import javax.crypto.Cipher;

import de.dzl.lelantus.crypto.BlockCoder;

/**
 * Encrypt identifying information using a deterministic public key encryption scheme.
 * Uses the algorithm {@value #IDAT_CIPHER_ALGORITHM}.
 *
 * Native pseudonym is a byte array returned by {@link #encryptIdent(String[])}.
 * Other public methods which return String pseudonyms utilize a Base64 encoding of the byte array.
 *
 * @author R.W.Majeed
 *
 */
public class DeterministicRSAEncoder  {
	/** cipher algorithm must be public key RSA without random padding (for anonymous identification) */
	private static final String IDAT_CIPHER_ALGORITHM = "RSA/ECB/NoPadding";
	/** Cipher used to encrypt IDAT. I.e. custodian */
	private BlockCoder blockCoder;
	/**
	 * Separator character used to join identity parts (name, surname, pepper, etc).
	 */
	private char fieldSeparator;
	/**
	 * Prefix for deterministic encryption to prevent 
	 * brute force and rainbow table attacks.
	 */
	private CharSequence pepper;
	private Cipher encryptCipher;

	public DeterministicRSAEncoder(Cipher encryptCipher, char fieldSeparator, CharSequence pepper) {
		Charset charset = StandardCharsets.UTF_8;
		this.encryptCipher = encryptCipher;
		this.blockCoder = new BlockCoder(charset, encryptCipher.getOutputSize(0));
		this.pepper = pepper;
		this.fieldSeparator = fieldSeparator;
	}

	public static Cipher loadEncryptCipher(PublicKey key)throws GeneralSecurityException{
		// make sure deterministic padding is used
	    Cipher encoder = Cipher.getInstance(IDAT_CIPHER_ALGORITHM);
	    encoder.init(Cipher.ENCRYPT_MODE, key);
		return encoder;
	}

	/**
	 * Encrypt a identifying data for a single patient
	 * The identifying data (strings) are converted to uppercase
	 * during that process to allow case insensitive matching
	 * @param ident array containing first name, surname, birth date (dd.mm.yyyy).
	 * @return encrypted byte array with encryption block length
	 * @throws GeneralSecurityException encryption error
	 * @throws ArrayIndexOutOfBoundsException for empty ident arrays (need at least one element)
	 */
	public byte[] encryptIdent(String... ident)throws GeneralSecurityException{
		// encode IDAT buffer
		CharBuffer buf = CharBuffer.allocate(blockCoder.getBlockSize());
		if( pepper != null ) {
			buf.append(pepper);
			buf.append(fieldSeparator);
		}
		for( int i=0; i<ident.length-1; i++) {
			buf.append(ident[i].toUpperCase());
			buf.append(fieldSeparator);
		}
		// no separator after last element
		buf.append(ident[ident.length-1].toUpperCase());
		// prepare for writing
		buf.flip();

		ByteBuffer encoded = blockCoder.encodeAndPad(buf);
		
		return encryptCipher.doFinal(encoded.array());
	}

	/**
	 * Encrypt a reference string. No correction is done for
	 * case sensitivity.
	 *
	 * @param reference reference string
	 * @param sourceSystem source system
	 * @return encrypted byte array with encryption block length
	 * @throws GeneralSecurityException encryption error
	 */
	public byte[] encryptReference(String reference, String sourceSystem)throws GeneralSecurityException{
		Objects.requireNonNull(reference);
		// encode reference buffer
		CharBuffer buf = CharBuffer.allocate(blockCoder.getBlockSize());
		if( pepper != null ) {
			buf.append(pepper);
			buf.append(fieldSeparator);
		}
		if( sourceSystem != null ) {
			buf.append(sourceSystem);
		}
		// always append separator (to prevent accidental match between pepper/system)
		buf.append(fieldSeparator);
		buf.append(reference);

		buf.flip();
		ByteBuffer encoded = blockCoder.encodeAndPad(buf);
		
		return encryptCipher.doFinal(encoded.array());
	}

	public int getBlockSize() {
		return blockCoder.getBlockSize();
	}
}
