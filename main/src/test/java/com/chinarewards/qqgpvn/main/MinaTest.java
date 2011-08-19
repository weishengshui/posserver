package com.chinarewards.qqgpvn.main;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Date;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.junit.Test;

public class MinaTest {

	@Test
	public void testClientServer() throws Exception {
		int PORT = 1234;
		String HOSTNAME = "localhost";

		// =============== server side ===================
		IoAcceptor acceptor = new NioSocketAcceptor();
		InetSocketAddress serverAddr = new InetSocketAddress(PORT);

		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		acceptor.getFilterChain().addLast(
				"codec",
				new ProtocolCodecFilter(new TextLineCodecFactory(Charset
						.forName("UTF-8"))));

		acceptor.setHandler(new ServerSessionHandler());
		acceptor.setCloseOnDeactivation(true);

		acceptor.getSessionConfig().setReadBufferSize(2048);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		acceptor.bind(serverAddr);

		// =============== client side ===================
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
				System.out.println("Connected to " + HOSTNAME + ", Session="
						+ session.getId());
				break;
			} catch (RuntimeIoException e) {
				System.err.println("Failed to connect.");
				e.printStackTrace();
				Thread.sleep(5000);
			}
		}
		// session.write("Client First Message");
		Thread.sleep(500);
		connector.dispose();
		acceptor.unbind(serverAddr);
		acceptor.dispose();
	}

	public static class ServerSessionHandler extends IoHandlerAdapter {
		@Override
		public void exceptionCaught(IoSession session, Throwable cause)
				throws Exception {
			cause.printStackTrace();
		}

		@Override
		public void messageReceived(IoSession session, Object message)
				throws Exception {
			String str = message.toString();
			if (str.trim().equalsIgnoreCase("quit")) {
				session.close(false);
				return;
			}
			System.out.println("Server received: session=" + session.getId()
					+ " message=" + message);
			Date date = new Date();
			session.write("Server Date is: " + date.toString());
			System.out.println("Server written to client...");
		}

		@Override
		public void sessionIdle(IoSession session, IdleStatus status)
				throws Exception {
			System.out.println("IDLE " + session.getIdleCount(status));
		}

		@Override
		public void sessionOpened(IoSession session) throws Exception {
			super.sessionOpened(session);
			session.write("Thanks for connecting to me");
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
}
