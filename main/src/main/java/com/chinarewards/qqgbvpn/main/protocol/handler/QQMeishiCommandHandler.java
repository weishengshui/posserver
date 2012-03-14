package com.chinarewards.qqgbvpn.main.protocol.handler;

import java.util.HashMap;

import org.codehaus.jackson.JsonGenerationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.exception.SaveDBException;
import com.chinarewards.qqgbvpn.main.logic.qqapi.QQMeishiManager;
import com.chinarewards.qqgbvpn.main.protocol.ServiceHandler;
import com.chinarewards.qqgbvpn.main.protocol.ServiceRequest;
import com.chinarewards.qqgbvpn.main.protocol.ServiceResponse;
import com.chinarewards.qqgbvpn.main.protocol.ServiceSession;
import com.chinarewards.qqgbvpn.main.protocol.cmd.QQMeishiRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.QQMeishiResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.filter.LoginFilter;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class QQMeishiCommandHandler implements ServiceHandler {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Inject
	public Provider<QQMeishiManager> gbm;
	
	@Override
	public void execute(ServiceRequest request, ServiceResponse response) {
		
		QQMeishiRequestMessage bodyMessage = (QQMeishiRequestMessage) request.getParameter();
		ServiceSession session = request.getSession();

		log.debug("QQMeishiCommandHandler bodyMessage=: {}"+bodyMessage);

		log.debug("QQMeishiCommandHandler posId=: {}"+String.valueOf(session.getAttribute(LoginFilter.POS_ID)));
		
		//组装请求参数
		HashMap<String, String> postParams = new HashMap<String, String>();
		postParams.put("posId", String.valueOf(session.getAttribute(LoginFilter.POS_ID)));
		postParams.put("userToken", bodyMessage.getUserToken());
		postParams.put("password", bodyMessage.getPassword());
		postParams.put("amount", Double.toString(bodyMessage.getAmount()));
		
		QQMeishiResponseMessage responseMessage = null;
		
		try {
			//发送请求
			responseMessage = gbm.get().qqmeishiCommand(postParams);
			//设置指令
			responseMessage.setCmdId(QQMeishiResponseMessage.QQMEISHI_CMD_ID_RESPONSE);
			
		} catch (JsonGenerationException e) {
			log.error("JsonGenerationException:", e);
		} catch (SaveDBException e) {
			log.error("SaveDBException:", e);
		}

		log.debug("QQMeishiCommandHandler end=:"+responseMessage.getCmdId());
		
		response.writeResponse(responseMessage);
	}

}
