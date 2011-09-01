package com.chinarewards.qqgbvpn.main.protocol.cmd.impl;

import java.util.HashMap;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.config.PosNetworkProperties;
import com.chinarewards.qqgbvpn.main.logic.login.LoginManager;
import com.chinarewards.qqgbvpn.main.logic.qqapi.GroupBuyingManager;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CommandHandler;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.cmd.login.LoginResult;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.LoginRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.LoginResponseMessage;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class BindCommandHandler implements CommandHandler {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Inject
	public LoginManager loginManager;

	@Inject
	public Provider<GroupBuyingManager> gbm;
	
	
	
	@Override
	public ICommand execute(IoSession session, ICommand bodyMessage) {
		log.debug("BindCommandHandler======execute==bodyMessage=:"+bodyMessage);
		LoginResponseMessage loginResponseMessage  = null;
		try{
			loginResponseMessage  = loginManager.bind((LoginRequestMessage) bodyMessage);
		}catch(Throwable e){
			loginResponseMessage.setChallenge(new byte[ProtocolLengths.CHALLEUGERESPONSE]);
			loginResponseMessage = new LoginResponseMessage();
			loginResponseMessage.setResult(LoginResult.OTHERS.getPosCode());
		}
		loginResponseMessage.setCmdId(CmdConstant.BIND_CMD_ID_RESPONSE);
		if(loginResponseMessage.getResult() == LoginResult.SUCCESS.getPosCode()){
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("posId", ((LoginRequestMessage) bodyMessage).getPosId());
			String serverKey = new PosNetworkProperties().getTxServerKey();
			log.debug("BindCommandHandler======execute==serverKey=:"+serverKey);
			params.put("key", serverKey);
			try {
				gbm.get().initGrouponCache(params);
			}catch (Throwable e) {
				log.error("initGrouponCache fail:"+e);
			}
		}
		return loginResponseMessage;
	}

}
