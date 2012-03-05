package com.chinarewards.qqgbvpn.main.protocol.handler;

import java.util.HashMap;

import org.apache.commons.configuration.Configuration;
import org.codehaus.jackson.JsonGenerationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.exception.SaveDBException;
import com.chinarewards.qqgbvpn.main.logic.qqapi.GroupBuyingManager;
import com.chinarewards.qqgbvpn.main.protocol.ServiceHandler;
import com.chinarewards.qqgbvpn.main.protocol.ServiceRequest;
import com.chinarewards.qqgbvpn.main.protocol.ServiceResponse;
import com.chinarewards.qqgbvpn.main.protocol.ServiceSession;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ValidateRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ValidateResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.filter.LoginFilter;
import com.chinarewards.qqgbvpn.main.vo.ValidateResponseMessageVO;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class ValidateCommandHandler implements ServiceHandler {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Inject
	public Provider<GroupBuyingManager> gbm;
	
	@Inject
	protected Configuration configuration;
	
	@Override
	public void execute(ServiceRequest request, ServiceResponse response) {
		
		ValidateRequestMessage bodyMessage = (ValidateRequestMessage) request.getParameter();
		ServiceSession session = request.getSession();

		log.debug("ValidateCommandHandler======execute==bodyMessage=: {}"+bodyMessage);
		ValidateRequestMessage validateRequestMessage = (ValidateRequestMessage) bodyMessage;
		ValidateResponseMessage validateResponseMessage = new ValidateResponseMessage();

		log.debug("ValidateCommandHandler======execute==posId=: {}"+String.valueOf(session.getAttribute(LoginFilter.POS_ID)));
		
		HashMap<String, String> postParams = new HashMap<String, String>();
		postParams.put("posId", String.valueOf(session.getAttribute(LoginFilter.POS_ID)));
		postParams.put("grouponId", validateRequestMessage.getGrouponId());
		postParams.put("token", validateRequestMessage.getGrouponVCode());
		postParams.put("key", configuration.getString("txserver.key"));
		log.debug("ValidateCommandHandler======execute==key=: {}"+configuration.getString("txserver.key"));
		
		try {
			ValidateResponseMessageVO vo = gbm.get().qqgbvValidationCommand(postParams);
			
			validateResponseMessage.setResult((int)vo.getQqws_resultcode());
			if(vo.getQqws_resultcode() == 0 && vo.getQqvalidate_resultstatus() != 0){
				validateResponseMessage.setResult(1);
			}
			validateResponseMessage.setResultName(vo.getResultName());
			validateResponseMessage.setCurrentTime(vo.getCurrentTime());
			validateResponseMessage.setResultExplain(vo.getResultExplain());
			validateResponseMessage.setUseTime(vo.getUseTime());
			validateResponseMessage.setValidTime(vo.getValidTime());
			validateResponseMessage.setCmdId(ValidateResponseMessage.VALIDATE_CMD_ID_RESPONSE);
			
		} catch (JsonGenerationException e) {
			log.error("JsonGenerationException:", e);
		} catch (SaveDBException e) {
			log.error("SaveDBException:", e);
		}

		log.debug("ValidateCommandHandler======execute==end=:"+validateResponseMessage.getCmdId());
		
		response.writeResponse(validateResponseMessage);
	}

}
