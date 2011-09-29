/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.mina.core.buffer.IoBuffer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.chinarewards.qqgbpvn.main.test.GuiceTest;
import com.chinarewards.qqgbvpn.main.protocol.CmdCodecFactory;
import com.chinarewards.qqgbvpn.main.protocol.CmdMapping;
import com.chinarewards.qqgbvpn.main.protocol.SimpleCmdCodecFactory;
import com.chinarewards.qqgbvpn.main.protocol.SimpleCmdMapping;
import com.chinarewards.qqgbvpn.main.protocol.cmd.FirmwareUpDoneRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.Message;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.ProtocolMessageDecoder.Result;

/**
 * 
 * 
 * @author cyril
 * @since 0.1.0
 */
public class ProtocolMessageDecoderTest extends GuiceTest {

	Charset charset = null;
	
	CmdMapping mapping = null;
	
	CmdCodecFactory cmdCodecFactory = null;
	
	ProtocolMessageDecoder msgDecoder = null;
	
	private static final String CHARSET_TO_TEST = "GB2312";
	
	private static final String POS_ID_TO_TEST = CHARSET_TO_TEST;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinarewards.qqgbpvn.main.test.GuiceTest#setUp()
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		charset = Charset.forName(CHARSET_TO_TEST);
		
		// prepare comamnd codec factory.
		mapping = new SimpleCmdMapping();
		mapping.addMapping(1, FirmwareUpDoneRequestCodec.class);
		
		// the command codec factory.
		cmdCodecFactory = new SimpleCmdCodecFactory(mapping);

		// the API we are going to test
		msgDecoder = new ProtocolMessageDecoder(cmdCodecFactory);
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinarewards.qqgbpvn.main.test.GuiceTest#tearDown()
	 */
	@After
	public void tearDown() throws Exception {
		// we are lazy, should remove reference to anything in setUp().
		super.tearDown();
	}

	/**
	 * Test the case a complete message, when converted to byte arrays, is
	 * divided into two parts which a incomplete header is sent.
	 * <p>
	 * 
	 * The message decoder should survive.
	 */
	@Test
	public void testDoDecode_TwoPartsHalfHeader() throws Exception {

		//
		// Prepare the original data which will be validated against the
		// output by the API to be tested.
		//
		
		// use any codec to play with this
		FirmwareUpDoneRequestCodec codec = new FirmwareUpDoneRequestCodec();
		FirmwareUpDoneRequestMessage srcMsg = new FirmwareUpDoneRequestMessage();
		srcMsg.setCmdId(1);
		srcMsg.setPosId(CHARSET_TO_TEST);
		//
		byte[] srcRawMsg = codec.encode(srcMsg, charset);
		byte[] srcCompleteRawMsg = PackageUtil.formatPackageContent(0, srcRawMsg);
		assertTrue(srcCompleteRawMsg.length == srcRawMsg.length + ProtocolLengths.HEAD);
		
		// this test requires the data length must be longer than the header
		// byte at least 1 byte.
		assertTrue("The source complete raw message is too short for testing",
				srcCompleteRawMsg.length > ProtocolLengths.HEAD + 2);

		// the API needs a IoBuffer, so we make it.
		IoBuffer in1 = IoBuffer.allocate(srcCompleteRawMsg.length - 1);
		in1.put(Arrays.copyOfRange(srcCompleteRawMsg, 0,
				srcCompleteRawMsg.length - 1));
		in1.position(0);
		
		//
		// Call the API for the first time.
		//
		// the first call - not sufficient data, should not have error.
		Result ret1 = msgDecoder.decode(in1, charset);
		assertNotNull(ret1);
		assertTrue("Decoder should report more data is needed!", ret1.isMoreDataRequired());	// more data needed
		assertNull(ret1.getMessage());	// no message returned.
		
		//
		// Prepare for the next call, this time with the new bytes arrived
		// which forms a complete message.
		//
		in1.position(srcCompleteRawMsg.length - 1);
		in1.expand(1);
		in1.put(Arrays.copyOfRange(srcCompleteRawMsg, 
				srcCompleteRawMsg.length - 1, srcCompleteRawMsg.length));
		in1.position(0);
		
		//
		// Call the API for the second time.
		//
		// the second call - sufficient data, should have message returned.
		Result ret2 = msgDecoder.decode(in1, charset);
		assertNotNull(ret2);
		assertFalse(ret2.isMoreDataRequired());
		assertNotNull(ret2.getMessage());
		
		// check the data
		Message parsedMsg = (Message)ret2.getMessage();
		FirmwareUpDoneRequestMessage parsedCmd = (FirmwareUpDoneRequestMessage) parsedMsg.getBodyMessage();
		assertEquals(1, parsedCmd.getCmdId());
		assertEquals(CHARSET_TO_TEST, parsedCmd.getPosId());

	}

	/**
	 * Test the case a complete message, when converted to byte arrays, is
	 * divided into two parts which a incomplete header is sent.
	 * <p>
	 * 
	 * The first part of the message, contains the header which is just before
	 * the message size is given.
	 */
	@Test
	public void testDoDecode_TwoPartsIncompleteHeaderBeforeMessageSizeFirst() throws Exception {

		//
		// Prepare the original data which will be validated against the
		// output by the API to be tested.
		//
		
		// use any codec to play with this
		FirmwareUpDoneRequestCodec codec = new FirmwareUpDoneRequestCodec();
		FirmwareUpDoneRequestMessage srcMsg = new FirmwareUpDoneRequestMessage();
		srcMsg.setCmdId(1);
		srcMsg.setPosId(CHARSET_TO_TEST);
		//
		byte[] srcRawMsg = codec.encode(srcMsg, charset);
		byte[] srcCompleteRawMsg = PackageUtil.formatPackageContent(0, srcRawMsg);
		assertTrue(srcCompleteRawMsg.length == srcRawMsg.length + ProtocolLengths.HEAD);
		
		// this test requires the data length must be longer than the header
		// byte at least 1 byte.
		assertTrue("The source complete raw message is too short for testing",
				srcCompleteRawMsg.length > ProtocolLengths.HEAD + 2);

		// the API needs a IoBuffer, so we make it.
		// we make 1 byte sent first. Is short enough.
		IoBuffer in1 = IoBuffer.allocate(1);
		in1.put(Arrays.copyOfRange(srcCompleteRawMsg, 0, 1));
		in1.position(0);	// must reset the position... Mina's API OK?
		
		//
		// Call the API for the first time.
		//
		// the first call - not sufficient data, should not have error.
		Result ret1 = msgDecoder.decode(in1, charset);
		assertNotNull(ret1);
		assertTrue("Decoder should report more data is needed!", ret1.isMoreDataRequired());	// more data needed
		assertNull(ret1.getMessage());	// no message returned.
		
		//
		// Prepare for the next call, this time with the new bytes arrived
		// which forms a complete message.
		//
		in1.position(1);
		in1.expand(srcCompleteRawMsg.length - 1);
		in1.put(Arrays.copyOfRange(srcCompleteRawMsg, 1, srcCompleteRawMsg.length));
		in1.position(0);
		
		//
		// Call the API for the second time.
		//
		// the second call - sufficient data, should have message returned.
		Result ret2 = msgDecoder.decode(in1, charset);
		assertNotNull(ret2);
		assertFalse(ret2.isMoreDataRequired());
		assertNotNull(ret2.getMessage());
		
		// check the data
		Message parsedMsg = (Message)ret2.getMessage();
		FirmwareUpDoneRequestMessage parsedCmd = (FirmwareUpDoneRequestMessage) parsedMsg.getBodyMessage();
		assertEquals(1, parsedCmd.getCmdId());
		assertEquals(CHARSET_TO_TEST, parsedCmd.getPosId());

	}
	
}
