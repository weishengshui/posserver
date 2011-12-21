package com.chinarewards.qqgbvpn.main.protocol.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.logic.challenge.ChallengeUtil;
import com.chinarewards.qqgbvpn.main.logic.login.LoginManager;
import com.chinarewards.qqgbvpn.main.protocol.ServiceHandler;
import com.chinarewards.qqgbvpn.main.protocol.ServiceRequest;
import com.chinarewards.qqgbvpn.main.protocol.ServiceResponse;
import com.chinarewards.qqgbvpn.main.protocol.ServiceSession;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
import com.chinarewards.qqgbvpn.main.protocol.cmd.InitRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.InitResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.init.InitResult;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;
import com.google.inject.Inject;

public class InitCommandHandler implements ServiceHandler {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Inject
	protected LoginManager loginManager;
	
	
	/* (non-Javadoc)
	 * @see com.chinarewards.qqgbvpn.main.protocol.ServiceHandler#execute(com.chinarewards.qqgbvpn.main.protocol.ServiceRequest, com.chinarewards.qqgbvpn.main.protocol.ServiceResponse)
	 */
	@Override
	public void execute(ServiceRequest request, ServiceResponse response) {
		
		InitRequestMessage bodyMessage = (InitRequestMessage)request.getParameter();
		
		log.debug("InitCommandHandler======execute==bodyMessage=: {}", bodyMessage);
		
		InitResponseMessage  initResponseMessage  = null;
		byte[] newChallenge = ChallengeUtil.generateChallenge();
		try {
			initResponseMessage = loginManager.init(bodyMessage, newChallenge);
			//save to session
			request.getSession().setAttribute(ServiceSession.CHALLENGE_SESSION_KEY, newChallenge);
			
		} catch (Throwable e) {
			// must refresh the challenge even an exception has occurred.
			log.warn("an unexpected error has occurred when POS ID "  + 
					bodyMessage.getPosId() + " tries to login", e);
			initResponseMessage = new InitResponseMessage();
			initResponseMessage
					.setChallenge(newChallenge);
			initResponseMessage.setResult(InitResult.OTHERS.getPosCode());
			request.getSession().setAttribute(ServiceSession.CHALLENGE_SESSION_KEY, newChallenge);
		}
		initResponseMessage.setCmdId(CmdConstant.INIT_CMD_ID_RESPONSE);
		response.writeResponse(initResponseMessage);
	}

}
