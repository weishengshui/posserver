/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.junit.Test;

import com.chinarewards.qqgbpvn.main.test.GuiceTest;
import com.chinarewards.qqgbvpn.main.protocol.cmd.GetFirmwareFragmentResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0 2011-09-15
 */
public class GetFirmwareFragmentResponseMessageCodecTest extends GuiceTest {

	@Test
	public void testEncode() throws Exception {

		GetFirmwareFragmentResponseMessageCodec codec = new GetFirmwareFragmentResponseMessageCodec();

		byte[] content = new byte[] { 0x01, 0x02, 0x03, 0x04, (byte)0xff };
		
		GetFirmwareFragmentResponseMessage msg = new GetFirmwareFragmentResponseMessage(
				GetFirmwareFragmentResponseMessage.RESULT_OK, content);
		
		byte[] out = codec.encode(msg, Charset.forName("GBK"));
		byte[] actualContent = Arrays.copyOfRange(out, ProtocolLengths.COMMAND + 2, out.length);
		
		assertNotNull(out);
		assertEquals(ProtocolLengths.COMMAND + 2 + 5, out.length);
		// byte content
		assertTrue(Arrays.equals(content, actualContent));

	}

}
