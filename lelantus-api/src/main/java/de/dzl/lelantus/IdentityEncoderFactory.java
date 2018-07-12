package de.dzl.lelantus;

public interface IdentityEncoderFactory extends CodingSystem{

	/**
	 * Create a new encoder
	 * @return encoder
	 * @throws EncoderConfigurationException when unable to create the encoder due to configuration errors
	 */
	IdentityEncoder createIdentityEncoder() throws EncoderConfigurationException;
}
