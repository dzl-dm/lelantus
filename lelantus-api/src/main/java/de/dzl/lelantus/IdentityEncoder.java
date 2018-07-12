package de.dzl.lelantus;

public interface IdentityEncoder extends Encoder {
	/**
	 * Encode an identity
	 * @param givenName given name (first name)
	 * @param lastName last name (surname)
	 * @param birthDate birth date in the form YYYY-MM-DD
	 *  partial information can be specified by substituting
	 *  the respective part with XX. E.g. 2001-XX-XX.
	 * @return identity code
	 * @throws EncodingException unable to create code
	 */
	byte[] encodeIdentity(String givenName, String lastName, String birthDate)
		throws EncodingException;
	
	//String encode(String givenName, String lastName, String birthDate, String ...additionalInfo);
}
