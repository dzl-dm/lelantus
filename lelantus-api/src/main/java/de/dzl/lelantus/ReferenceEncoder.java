package de.dzl.lelantus;

/**
 * Encoder used to create codes for (external) references
 * such as social security number, private system IDs, URLs etc.
 *
 * @author R.W.Majeed
 *
 */
public interface ReferenceEncoder extends Encoder {
	/**
	 * Encode a reference
	 * @param reference reference string
	 * @param system source system which generated the reference
	 * @return reference code
	 * @throws EncodingException unable to create code
	 */
	byte[] encodeReference(String reference, String system)
		throws EncodingException;
}
