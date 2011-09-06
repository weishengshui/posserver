/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol;

import static org.junit.Assert.*;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.chinarewards.qqgbpvn.core.test.GuiceTest;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.cmd.InitRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.ICommandCodec;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.InitMessageCodec;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class CmdCodecTest extends GuiceTest {

	public static class TestRequestCmd implements ICommand {

		private String name;

		private int i;

		public TestRequestCmd(String name, int i) {
			this.name = name;
			this.i = i;
		}

		@Override
		public long getCmdId() {
			return 654;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the i
		 */
		public int getI() {
			return i;
		}

	}

	public static class TestResponseCmd implements ICommand {

		private String name;

		public TestResponseCmd(String name) {
			this.name = name;
		}

		@Override
		public long getCmdId() {
			return 6858;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

	}

	public static class TestServiceHandler implements ServiceHandler {

		@Override
		public void execute(ServiceRequest request, ServiceResponse response) {

			TestRequestCmd msg = (TestRequestCmd) request.getParameter();

			String name = "Result: " + msg.getI() + " - " + msg.getName();

			TestResponseCmd o = new TestResponseCmd(name);
			response.writeResponse(o);

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinarewards.qqgbpvn.main.test.JpaGuiceTest#setUp()
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinarewards.qqgbpvn.main.test.JpaGuiceTest#tearDown()
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Most simple case for dispatching a command.
	 */
	@Test
	public void testDecode_Simple_OK() throws Exception{

		// fake a message
		InitRequestMessage msg = new InitRequestMessage();
		msg.setCmdId(CmdConstant.INIT_CMD_ID);
		msg.setPosid("POS-56789012");

		CmdMapping cmdMapping = new SimpleCmdMapping();
		cmdMapping.addMapping(1, InitMessageCodec.class);

		Charset charset = Charset.forName("ISO-8859-1");

		// 4 byte + 12 byte, see wiki
		IoBuffer buffer = IoBuffer.allocate(4 + 12);
		buffer.putUnsignedInt(msg.getCmdId());
		buffer.putString(msg.getPosId(), charset.newEncoder());
		buffer.position(0); // must reset

		// get an instance of command codec
		CmdCodecFactory cmdCodecFactory = new SimpleCmdCodecFactory(cmdMapping);
		ICommandCodec codec = cmdCodecFactory.getCodec(1);
		InitRequestMessage decodedMsg = (InitRequestMessage)codec.decode(buffer, charset);
		
		// validation
		assertEquals(CmdConstant.INIT_CMD_ID, decodedMsg.getCmdId());
		assertEquals("POS-56789012", decodedMsg.getPosId());

	}
}
