package com.chinarewards.qqgbvpn.main.protocol.hander;

import java.util.Date;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 * server handler
 * 
 * copy from ServerSessionHandler in test
 * 
 * @author huangwei
 *
 */
public class ServerSessionHandler extends IoHandlerAdapter {
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
