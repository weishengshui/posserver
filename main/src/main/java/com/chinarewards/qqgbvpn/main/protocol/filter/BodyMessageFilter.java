package com.chinarewards.qqgbvpn.main.protocol.filter;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.protocol.socket.message.ErrorBodyMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.IBodyMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.Message;

public class BodyMessageFilter extends IoFilterAdapter {

	Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void exceptionCaught(NextFilter nextFilter, IoSession session,
			Throwable cause) throws Exception {
		log.debug("ErrorBodyMessage exception:({})",cause);
	}

	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session,
			Object message){
		IBodyMessage msg = ((Message) message).getBodyMessage();
		//return when IBodyMessage instanceof ErrorBodyMessage
		if(msg instanceof ErrorBodyMessage){
			log.debug("IBodyMessage is ErrorBodyMessage");
			session.write(message);
		}else{
			log.debug("IBodyMessage is not ErrorBodyMessage");
			nextFilter.messageReceived(session, message);
		}
		
	}

}
