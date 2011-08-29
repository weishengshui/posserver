package com.chinarewards.qqgbvpn.main.protocol.hander;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CommandHandler;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.IBodyMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.Message;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

/**
 * server handler
 * 
 * copy from ServerSessionHandler in test
 * 
 * @author huangwei
 *
 */
public class ServerSessionHandler extends IoHandlerAdapter {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	private Injector injector;
	
	public ServerSessionHandler(Injector injector){
		this.injector = injector;
	}
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		cause.printStackTrace();
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		log.debug("messageReceived start");
		Message msg = (Message) message;
		
		int cmdId = msg.getBodyMessage().getCmdId();
		CommandHandler commandHandler = null;
		//Dispatcher
		switch(cmdId){
		case CmdConstant.LOGIN_CMD_ID:
			commandHandler = injector.getInstance(Key.get(CommandHandler.class,
					Names.named(CmdConstant.LOGIN_CMD_NAME)));
			break;
			
		}
		if(commandHandler == null){
			//TODO no exits cmd
		}
		IBodyMessage responseMsgBody = commandHandler.execute(session, msg.getBodyMessage());
		
//		String str = message.toString();
//		if (str.trim().equalsIgnoreCase("quit")) {
//			session.close(false);
//			return;
//		}
//		System.out.println("Server received: session=" + session.getId()
//				+ " message=" + message);
//		Date date = new Date();
		msg.setBodyMessage(responseMsgBody);
		session.write(msg);
		log.debug("messageReceived end");
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
