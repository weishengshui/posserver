package com.chinarewards.qqgbvpn.main.protocol.cmd.impl;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.logic.login.LoginManager;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CommandHandler;
import com.chinarewards.qqgbvpn.main.protocol.cmd.init.InitResult;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.IBodyMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.InitRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.InitResponseMessage;
import com.google.inject.Inject;

public class InitCommandHandler implements CommandHandler {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Inject
	public LoginManager loginManager;
	
	@Override
	public IBodyMessage execute(IoSession session, IBodyMessage bodyMessage) {
		log.debug("InitCommandHandler======execute==bodyMessage=:"+bodyMessage);
		InitResponseMessage  initResponseMessage  = null;
		try{
			initResponseMessage = loginManager.init((InitRequestMessage) bodyMessage);
		}catch(Throwable e){
			initResponseMessage = new InitResponseMessage();
			initResponseMessage.setChallenge(new byte[ProtocolLengths.CHALLEUGE]);
			initResponseMessage.setResult(InitResult.OTHERS.getPosCode());
		}
		initResponseMessage.setCmdId(CmdConstant.INIT_CMD_ID_RESPONSE);
		return initResponseMessage;
	}

}
