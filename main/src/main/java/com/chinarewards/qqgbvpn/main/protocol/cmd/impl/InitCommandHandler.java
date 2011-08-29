package com.chinarewards.qqgbvpn.main.protocol.cmd.impl;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.protocol.cmd.CommandHandler;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.IBodyMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.InitRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.InitResponseMessage;

public class InitCommandHandler implements CommandHandler {
	
	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public IBodyMessage execute(IoSession session, IBodyMessage bodyMessage) {
		log.debug("bodyMessage=========:"+bodyMessage.getClass());
		// TODO Auto-generated method stub
		InitRequestMessage message = (InitRequestMessage) bodyMessage;
		
		InitResponseMessage initResponseMessage = new InitResponseMessage();
		initResponseMessage.setCmdId(message.getCmdId() + 1);
		initResponseMessage.setResult(0);
		byte[] challeuge = new byte[]{1,2,3,4,5,6,7,8};
		initResponseMessage.setChalleuge(challeuge);
		return initResponseMessage;
	}

}
