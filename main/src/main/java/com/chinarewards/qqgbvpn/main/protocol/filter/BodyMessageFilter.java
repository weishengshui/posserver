package com.chinarewards.qqgbvpn.main.protocol.filter;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.protocol.cmd.ErrorBodyMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.cmd.Message;

public class BodyMessageFilter extends IoFilterAdapter {

	Logger log = LoggerFactory.getLogger(getClass());

//	@Override
//	public void exceptionCaught(NextFilter nextFilter, IoSession session,
//			Throwable cause) throws Exception {
//		log.error("BodyMessageFilter caught an exception", cause);
//	}

	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session,
			Object message) {
		
		log.trace("messageReceived() started");
		
		ICommand msg = ((Message) message).getBodyMessage();
		// return when IBodyMessage instanceof ErrorBodyMessage
		if (msg instanceof ErrorBodyMessage) {
			log.debug("IBodyMessage is ErrorBodyMessage");
			session.write(message);
		} else {
			log.debug("IBodyMessage is not ErrorBodyMessage");
			nextFilter.messageReceived(session, message);
		}

		log.trace("messageReceived() done");
	}

}
