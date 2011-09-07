package com.chinarewards.qqgbvpn.main.protocol.handler;

import java.util.HashMap;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.logic.login.LoginManager;
import com.chinarewards.qqgbvpn.main.logic.qqapi.GroupBuyingManager;
import com.chinarewards.qqgbvpn.main.protocol.ServiceHandler;
import com.chinarewards.qqgbvpn.main.protocol.ServiceRequest;
import com.chinarewards.qqgbvpn.main.protocol.ServiceResponse;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
import com.chinarewards.qqgbvpn.main.protocol.cmd.LoginRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.LoginResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.login.LoginResult;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class BindCommandHandler implements ServiceHandler {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Inject
	protected LoginManager loginManager;

	@Inject
	protected Provider<GroupBuyingManager> gbm;
	
	@Inject
	protected Configuration configuration;
	
	@Override
	public void execute(ServiceRequest request, ServiceResponse response) {
		
		LoginRequestMessage bodyMessage = (LoginRequestMessage)request.getParameter();
		
		log.debug("BindCommandHandler======execute==bodyMessage=:"+bodyMessage);
		LoginResponseMessage loginResponseMessage  = null;
		try{
			loginResponseMessage  = loginManager.bind(bodyMessage);
		}catch(Throwable e){
			loginResponseMessage.setChallenge(new byte[ProtocolLengths.CHALLENGE_RESPONSE]);
			loginResponseMessage = new LoginResponseMessage();
			loginResponseMessage.setResult(LoginResult.OTHERS.getPosCode());
		}
		loginResponseMessage.setCmdId(CmdConstant.BIND_CMD_ID_RESPONSE);
		if(loginResponseMessage.getResult() == LoginResult.SUCCESS.getPosCode()){
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("posId", ((LoginRequestMessage) bodyMessage).getPosId());
			String serverKey = configuration.getString("txserver.key");
			log.debug("BindCommandHandler======execute==serverKey=:"+serverKey);
			params.put("key", serverKey);
			try {
				gbm.get().initGrouponCache(params);
			}catch (Throwable e) {
				log.error("initGrouponCache fail:"+e);
			}
		}
		
		response.writeResponse(loginResponseMessage);
	}

}
