package com.chinarewards.qqgpvn.main;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;

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

import com.chinarewards.qqgbvpn.main.protocol.socket.InitMsg2;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.encoder.InitMsgSocketFactory;
import com.chinarewards.qqgpvn.main.test.BaseTest;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class ProtcolOnMinaTest extends BaseTest {

	@Test
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

		}

		@Override
		public void sessionIdle(IoSession session, IdleStatus status)
				throws Exception {
			System.out.println("IDLE " + session.getIdleCount(status)
					+ ", Remote IP/Port:" + session.getRemoteAddress()
					+ ", Accum: " + session.getBothIdleCount());

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

	@Test
	public void testSendViaJavaSocket() throws Exception {

		Socket socket = new Socket("localhost", 1234);

		OutputStream os = socket.getOutputStream();

		byte[] msg = new byte[] {
				// SEQ
				0, 0, 0, 24,
				// ACK
				0, 0, 0, 0,
				// flags
				0, 0, 
				// checksum
				0, 2,
				// message length
				0, 0, 0, 32,
				// command ID
				0, 0, 0, 5,
				// POS ID
				'P', 'O', 'S', '-', '5', '6', '7', '8', '9',
				'0', '1', '2' };
		System.out.println("Packet size: " + msg.length);
		os.write(msg);
		os.close();
		socket.close();

	}

}
