package de.dzl.lelantus.crypto;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import org.junit.Test;
/**
 * Test {@link BlockCoder} with both long text containing umlaut characters 
 * and a single character text. The resulting output block should have the same 
 * size. 
 * @author marap1
 *
 */
public class TestBlockCoder {

	public static final String testString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZÄÜÖÉäüöéß";
	public static final String shortestString = "a";

	@Test
	public void testEncodeDecodeUtf8German() {
		Charset charset = Charset.forName("utf-8");
		testEncodeDecode(charset, testString);
		testEncodeDecode(charset, shortestString);
	}
	
	public void testEncodeDecode(Charset charset, String string){
		BlockCoder bc = new BlockCoder(charset,256);
		ByteBuffer bb = bc.encodeAndPad(CharBuffer.wrap(testString));
		assertEquals(bc.getBlockSize(), bb.remaining());
		CharBuffer cb = bc.decodeAndUnpad(bb);
		assertEquals(0, bb.remaining());
		assertEquals(testString, cb.toString());		
	}

}
