package de.dzl.lelantus.crypto;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * Encodes or decodes strings to/from a byte array with fixed length.
 * The block is padded to the specified block length by using a
 * deterministic padding. The same input will always result in the
 * same byte block.
 * 
 * @author marap1
 *
 */
public class BlockCoder { 

	protected Charset charset;
	protected int blocksize;
	
	
	public BlockCoder(Charset charset, int blocksize){
		this.charset = charset;
		this.blocksize = blocksize;
	}
	
	public int getBlockSize(){return blocksize;}
	
	/**
	 * Encode a character buffer to a byte block of fixed length. Byte buffer is padded deterministically using content from the char buffer.
	 * @param input character buffer to encode
	 * @return byte buffer containing the input. the input is prefixed by a single byte specifying the unpadded length. after encoding, the buffer is padded by repeating the encoded pattern.
	 */
	public ByteBuffer encodeAndPad(CharBuffer input){
		ByteBuffer encoded = charset.encode(input);
		encoded.mark(); // remember start of encoded data (in this case always 0)
		ByteBuffer padded = ByteBuffer.allocate(blocksize);
		if( encoded.remaining() > 255 )throw new IllegalArgumentException("encoded character input exceeds 255 characters");
		// write length in single byte
		padded.put((byte)encoded.remaining());
		// write encoded buffer
		padded.put(encoded);
		// pad until end of buffer
		while( padded.hasRemaining() ){
			encoded.reset();
			if( encoded.remaining() > padded.remaining() ){
				// prevent buffer overflow exception
				encoded.limit(padded.remaining());
			}
			padded.put(encoded);
		}
		// make buffer readable from beginning
		padded.flip();
		return padded;
	}
	
	/**
	 * Decode a block of bytes to a char buffer with the 
	 * specified encoding.
	 * On return, the buffers position is incremented by
	 * the block size.
	 * 
	 * @param buffer raw byte buffer containing encoded characters (one byte for length followed by characters)
	 * @return decoded characters
	 */
	public CharBuffer decodeAndUnpad(ByteBuffer buffer){
		// make sure a full block is available
		assert buffer.remaining() == blocksize;
		// read unsigned byte
		int size = 0xFF & buffer.get();
		// don't read padding, set limit to end of data
		ByteBuffer unpadded = buffer.asReadOnlyBuffer();
		unpadded.limit(unpadded.position()+size);

		// position input buffer after processed block
		buffer.position(buffer.position()+blocksize-1);
		
		// decode unpadded buffer
		return charset.decode(unpadded);
	}
	
	public ByteBuffer encodeId(String localId){
		CharBuffer buffer = CharBuffer.wrap(localId);
		return encodeAndPad(buffer);
	}

}
