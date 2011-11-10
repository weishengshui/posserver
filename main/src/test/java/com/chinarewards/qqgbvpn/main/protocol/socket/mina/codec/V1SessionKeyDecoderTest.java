/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.chinarewards.qqgbvpn.main.encoder.IUUIDEncoder;
import com.chinarewards.qqgbvpn.main.encoder.UUIDEncoderImpl;
import com.chinarewards.qqgbvpn.main.session.SessionKeyCodec;
import com.chinarewards.qqgbvpn.main.session.v1.V1SessionKey;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class V1SessionKeyDecoderTest {

	@Test
	public void testEncodeHeader() throws Exception {

		// content
		SessionKeyCodec codec = new SessionKeyCodec();
		IUUIDEncoder uuidCode = new UUIDEncoderImpl();
		V1SessionKey key = new V1SessionKey("1234567890ABCDEF1234567890ABCDEF");

		// encoded completed packet
		byte[] encoded = codec.encode(key);

		assertNotNull(encoded);
		assertEquals(20, encoded.length);

		assertEquals((byte) 0x01, encoded[0]);
		/* length */
		assertEquals((byte) 0x00, encoded[1]);
		/* length */
		assertEquals((byte) 0x10, encoded[3]);
		

		/* the key content */
		assertTrue(Arrays.equals(
				Arrays.copyOfRange(encoded, 4, encoded.length), uuidCode.encode("1234567890ABCDEF1234567890ABCDEF")));

	}

	@Test
	public void test() throws Exception {

		// content
		SessionKeyCodec codec = new SessionKeyCodec();
		V1SessionKey key = new V1SessionKey("1234567890ABCDEF1234567890ABCDEF");

		// encoded completed packet
		byte[] encoded = codec.encode(key);

		codec = new SessionKeyCodec();
		V1SessionKey decodedKey = (V1SessionKey) codec.decode(encoded);

		/*** validation ***/
		assertNotNull(decodedKey);
		assertEquals("1234567890ABCDEF1234567890ABCDEF".toLowerCase(), decodedKey.getKey());

	}

}
