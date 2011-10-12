package com.chinarewards.qqgbpvn.main;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Random;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.service.IoServiceStatistics;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.junit.Test;

import com.chinarewards.qqgbpvn.main.test.BaseTest;
import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.main.protocol.socket.InitMsg2;
import com.chinarewards.qqgbvpn.main.protocol.socket.InitMsgResult;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.CodecUtil;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.InitMsgSocketFactory;
import com.chinarewards.qqgbvpn.main.util.HMAC_MD5;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class ProtocolOnMinaTest extends BaseTest {

	@Test
	public void testDummy() {
		
	}
	
	//@Test
	public void testClientServer() throws Exception {

		// the TCP port to listen
		int PORT = 1234;

		// server host
		String HOSTNAME = "localhost";

		// =============== server side ===================
		InetSocketAddress serverAddr = new InetSocketAddress(PORT);

		IoAcceptor acceptor = new NioSocketAcceptor();

		acceptor.getFilterChain().addLast("logger", new LoggingFilter());

		// FIXME not this
		acceptor.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(new InitMsgSocketFactory(false)));

		acceptor.getFilterChain().addLast("logger2", new LoggingFilter());

		acceptor.setHandler(new ServerSessionHandler());
		acceptor.setCloseOnDeactivation(true);

		// acceptor.getSessionConfig().setReadBufferSize(2048);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		acceptor.bind(serverAddr);

		// =============== client side ===================
		boolean runClient = false;
		if (runClient) {
			NioSocketConnector connector = new NioSocketConnector();
			InetSocketAddress clientAddr = new InetSocketAddress(HOSTNAME, PORT);

			// Configure the service.
			connector.setConnectTimeoutMillis(10000);
			connector.getFilterChain().addLast("logger", new LoggingFilter());
			connector.getFilterChain().addLast(
					"codec",
					new ProtocolCodecFilter(new TextLineCodecFactory(Charset
							.forName("UTF-8"))));
			connector.setHandler(new ClientSessionHandler(10));
			IoSession session = null;
			for (;;) {
				try {
					ConnectFuture future = connector.connect(clientAddr);
					future.awaitUninterruptibly();
					session = future.getSession();
					System.out.println("Connected to " + HOSTNAME
							+ ", Session=" + session.getId());
					break;
				} catch (RuntimeIoException e) {
					System.err.println("Failed to connect.");
					e.printStackTrace();
					Thread.sleep(5000);
				}
			}

		}

		long runForSeconds = 3600;
		log.info(
				"Server running, waiting incoming connection, will run for {} seconds",
				runForSeconds);
		// session.write("Client First Message");
		Thread.sleep(runForSeconds * 1000);
		// connector.dispose();
		acceptor.unbind(serverAddr);
		acceptor.dispose();
	}

	/**
	 * 
	 * 
	 */
	public static class ServerSessionHandler extends IoHandlerAdapter {

		@Override
		public void exceptionCaught(IoSession session, Throwable cause)
				throws Exception {
			cause.printStackTrace();
		}

		@Override
		public void messageReceived(IoSession session, Object message)
				throws Exception {

			InitMsg2 msg = (InitMsg2) message;

			System.out.println("Sequence =" + msg.getSeq());
			System.out.println("ACK      =" + msg.getAck());
			System.out.println("FLAGS    =" + msg.getFlags());
			System.out.println("Checksum =" + msg.getChecksum());
			System.out.println("length=" + msg.getLength());
			System.out.println("POS ID=[" + msg.getPosId() + "]");

			// construct random response
			System.out.println("Building response to client");

			// known challenge
			byte[] challenge = new byte[8];
			int i = 0;
			challenge[i++] = (byte) 0x01;
			challenge[i++] = (byte) 0x0a;
			challenge[i++] = (byte) 0xfe;
			challenge[i++] = (byte) 0xef;
			//
			challenge[i++] = (byte) 0xab;
			challenge[i++] = (byte) 0xcd;
			challenge[i++] = (byte) 0xef;
			challenge[i++] = (byte) 0x43;

			// debug: print the expected challenge response using the above
			// challenge.
			String key = "123456";

			byte[] challengeResponse = HMAC_MD5
					.getSecretContent(challenge, key);
			for (int j = 0; j < challengeResponse.length; j++) {
				System.out.print(Integer.toHexString(challengeResponse[j] & 0XFF));
				System.out.print(" ");
			}
			System.out.print("\n");

			// result
			Random randomGenerator = new Random();
			int result = randomGenerator.nextInt(2); // 0 to 1

			InitMsgResult ret = new InitMsgResult(result, challenge);
			session.write(ret);

			System.out.println("Wrote response to client.");
			System.out.println("Result is " + result);

		}

		@Override
		public void sessionIdle(IoSession session, IdleStatus status)
				throws Exception {
			System.out.println("IDLE " + session.getIdleCount(status)
					+ ", Remote IP/Port:" + session.getRemoteAddress()
					+ ", Accum: " + session.getBothIdleCount());

			System.out.println("getBothIdleTimeInMillis="
					+ session.getConfig().getBothIdleTimeInMillis());
			System.out
					.println("getBothIdleCount=" + session.getBothIdleCount());

			if (session.getBothIdleCount()
					* session.getConfig().getBothIdleTimeInMillis() >= 1000 * 60) {
				session.close(true);
			}

			IoServiceStatistics stat = session.getService().getStatistics();

			System.out.println("getManagedSessionCount="
					+ session.getService().getManagedSessionCount());
		}

		@Override
		public void sessionOpened(IoSession session) throws Exception {
			super.sessionOpened(session);

			System.out.println("session.getRemoteAddress():"
					+ session.getRemoteAddress());

			// session.write("Thanks for connecting to me");
		}

	}

	public static class ClientSessionHandler extends IoHandlerAdapter {

		int count;

		public ClientSessionHandler(int count) {
			this.count = count;
		}

		@Override
		public void exceptionCaught(IoSession session, Throwable cause)
				throws Exception {
			cause.printStackTrace();
		}

		@Override
		public void sessionClosed(IoSession session) {
			// Print out total number of bytes read from the remote peer.
			System.err.println("Total " + session.getReadBytes() + " byte(s)");
		}

		@Override
		public void messageReceived(IoSession session, Object message) {
			System.out.println("Client Received: " + message);
			session.write("Client Say Hello!");
			count--;
			System.out.println("Client Written Message to Server: count="
					+ count);
			if (count <= 0) {
				// client initiate close, see TimerServerHandler
				session.write("quit");
				session.close(false);
			}

		}

		@Override
		public void sessionIdle(IoSession session, IdleStatus status)
				throws Exception {
			System.out.println("IDLE " + session.getIdleCount(status));
		}
	}

//	@Test
	public void testSendViaJavaSocket() throws Exception {

		Socket socket = new Socket("localhost", 1235);

		OutputStream os = socket.getOutputStream();
		InputStream is = socket.getInputStream();

		byte[] msg = new byte[] {
				// SEQ
				0, 0, 0, 24,
				// ACK
				0, 0, 0, 0,
				// flags
				0, 0,
				// checksum
				0, 0,
				// message length
				0, 0, 0, 32,
				// command ID
				0, 0, 0, 5,
				// POS ID
				'P', 'O', 'S', '-', '5', '6', '7', '8', '9', '0', '1', '2' };
		
		// calculate checksum
		int checksum = Tools.checkSum(msg, msg.length);
		Tools.putUnsignedShort(msg, checksum, 10);
		
		System.out.println("Packet size: " + msg.length);

		long runForSeconds = 1;
		// write response
		log.info("Send request to server");
		os.write(msg);
		// session.write("Client First Message");
		Thread.sleep(runForSeconds * 1000);
		// read
		log.info("Read response");
		byte[] response = new byte[30];
		int n = is.read(response);
		System.out.println("Number of bytes read: " + n);
		for (int i = 0; i < n; i++) {
			String s = Integer.toHexString((byte) response[i]);
			if (s.length() < 2)
				s = "0" + s;
			if (s.length() > 2)
				s = s.substring(s.length() - 2);
			System.out.print(s + " ");
			if ((i + 1) % 8 == 0)
				System.out.println("");
		}

		os.close();
		socket.close();

	}

//	@Test
	public void testSendViaJavaSocket_FreeToChangeThisCode() throws Exception {

		Socket socket = new Socket("localhost", 1234);

		OutputStream os = socket.getOutputStream();
		InputStream is = socket.getInputStream();

		byte[] msg = new byte[] {
				// SEQ
				0, 0, 0, 24,
				// ACK
				0, 0, 0, 0,
				// flags
				0, 0,
				// checksum
				0, 0,
				// message length
				0, 0, 0, 32,
				// command ID
				0, 0, 0, 5,
				// POS ID
				'P', 'O', 'S', '-', '5', '6', '7', '8', '9', '0', '1', '2' };
		byte[] msg2 = new byte[] {
				// SEQ
				0, 0, 0, 25,
				// ACK
				0, 0, 0, 0,
				// flags
				0, 0,
				// checksum
				0, 0,
				// message length
				0, 0, 0, 32,
				// command ID
				0, 0, 0, 5,
				// POS ID
				'P', 'O', 'S', '-', '5', '6', '7', '8', '9', '0', '1', '3' };
		
		// calculate checksum
		int checksum = Tools.checkSum(msg, msg.length);
		Tools.putUnsignedShort(msg, checksum, 10);
		// calculate checksum
		int checksum2 = Tools.checkSum(msg2, msg2.length);
		Tools.putUnsignedShort(msg2, checksum2, 10);
		

		long runForSeconds = 180;
		// write response
		log.info("Send request to server");
		
		// send both message at once
		int rubbishLength = 4;
		byte[] outBuf = new byte[msg.length + msg2.length + rubbishLength];
		System.arraycopy(msg, 0, outBuf, 0, msg.length);
		System.arraycopy(msg2, 0, outBuf, msg.length, msg2.length);
		
		os.write(outBuf);
		
		// ----------
		
		// session.write("Client First Message");
		Thread.sleep(runForSeconds * 1000);
		// read
		log.info("Read response");
		byte[] response = new byte[30];
		int n = is.read(response);
		System.out.println("Number of bytes read: " + n);
		for (int i = 0; i < n; i++) {
			String s = Integer.toHexString((byte) response[i]);
			if (s.length() < 2)
				s = "0" + s;
			if (s.length() > 2)
				s = s.substring(s.length() - 2);
			System.out.print(s + " ");
			if ((i + 1) % 8 == 0)
				System.out.println("");
		}

		os.close();
		socket.close();

	}

//	@Test
	public void testSendFirmwareUpdateRequestViaJavaSocket() throws Exception {

		Socket socket = new Socket("192.168.4.121", 1234);

		OutputStream os = socket.getOutputStream();
		InputStream is = socket.getInputStream();

		byte[] msg = new byte[] {
				// SEQ
				0, 0, 0, 24,
				// ACK
				0x20, 0, 0, 0x04,
				// flags
				0, 0,
				// checksum (auto-calculated)
				0, 0,
				// message length
				0, 0, 0, 0x20,
				// command ID
				0, 0, 0, 13,
				// POS ID
				'R', 'E', 'W', 'A', 'R', 'D', 'S', '-', '0', '0', '0', '1' };
		
		// calculate checksum
		int checksum = Tools.checkSum(msg, msg.length);
		Tools.putUnsignedShort(msg, checksum, 10);
		
		System.out.println("Packet size: " + msg.length);

		long runForSeconds = 1;
		// write response
		log.info("Send request to server");
		os.write(msg);
		// session.write("Client First Message");
		Thread.sleep(runForSeconds * 1000);
		// read
		log.info("Read response");
		byte[] response = new byte[30];
		int n = is.read(response);
		System.out.println("Number of bytes read: " + n + "\n");
		CodecUtil.debugRaw(log, response);
		
		for (int i = 0; i < n; i++) {
			String s = Integer.toHexString((byte) response[i]);
			if (s.length() < 2)
				s = "0" + s;
			if (s.length() > 2)
				s = s.substring(s.length() - 2);
			System.out.print(s + " ");
			if ((i + 1) % 8 == 0)
				System.out.println("");
		}

		os.close();
		socket.close();

	}

//	@Test
	public void testSendFirmwareUpdateRequestViaJavaSocket_Free() throws Exception {

		Socket socket = new Socket("192.168.1.42", 1234);

		OutputStream os = socket.getOutputStream();
		InputStream is = socket.getInputStream();

		byte[] msg = new byte[] {
				// SEQ
				0, 0, 0, 24,
				// ACK
				0x20, 0, 0, 0x04,
				// flags
				0, 0,
				// checksum (auto-calculated)
				0, 0,
				// message length
				0, 0, 0, 0x20,
				// command ID
				0, 0, 0, 13,
				// POS ID
				'R', 'E', 'W', 'A', 'R', 'D', 'S', '-', '0', '0', '0', '1' };

		msg = new byte[] {
				// SEQ
				0, 0, 0, 24,
				// ACK
				0x20, 0, 0, 0x04,
				// flags
				0, 0,
				// checksum (auto-calculated)
				0, 0,
				// message length
				0, 0, 0, 0x20,
				// command ID
				0, 0, 0, 5,
				// POS ID
				'R', 'E', 'W', 'A', 'R', 'D', 'S', '-', '0', '0', '0', '1' };
		
		// calculate checksum
		int checksum = Tools.checkSum(msg, msg.length);
		Tools.putUnsignedShort(msg, checksum, 10);
		
		System.out.println("Packet size: " + msg.length);
		
		int loop = 5;

		long runForSeconds = 1;
		// write response
		log.info("Send request to server");
		for (int j = 0; j < loop; j++) {
			msg[3] = (byte)0x0a;
			msg[3] += (byte)j;

			// uncomment the following to enable individual CORRECT checksum
			// to be sent
			
			boolean 
			doSaneChecksum = true;
			
			if (j == 3) {
				doSaneChecksum = false;
			}
			
			if (doSaneChecksum) {
				msg[10] = 0;
				msg[11] = 0;
				int checksum2 = Tools.checkSum(msg, msg.length);
				Tools.putUnsignedShort(msg, checksum2, 10);
			}
			
			
			os.write(msg);
//			os.flush();
//			Thread.sleep(1);
			
			
			msg[31] += 1;

		}
		
//		os.flush();
//		Thread.sleep(1000);
//		os.write(msg);
//		os.flush();
		
		// session.write("Client First Message");
		Thread.sleep(runForSeconds * 1000);
		// read
		log.info("Read response");
		byte[] response = new byte[30 * loop];
		int n = is.read(response);
		System.out.println("Number of bytes read: " + n + "\n");
		CodecUtil.debugRaw(log, response);
		
		for (int i = 0; i < n; i++) {
			String s = Integer.toHexString((byte) response[i]);
			if (s.length() < 2)
				s = "0" + s;
			if (s.length() > 2)
				s = s.substring(s.length() - 2);
			System.out.print(s + " ");
			if ((i + 1) % 8 == 0)
				System.out.println("");
		}

		os.close();
		socket.close();

	}

}
