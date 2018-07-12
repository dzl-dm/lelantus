package de.dzl.lelantus;

public interface ReferenceEncoderFactory extends CodingSystem{

	/**
	 * Create a new encoder
	 * @return encoder
	 * @throws EncoderConfigurationException when unable to create the encoder due to configuration errors
	 */
	ReferenceEncoder createReferenceEncoder() throws EncoderConfigurationException;
}
