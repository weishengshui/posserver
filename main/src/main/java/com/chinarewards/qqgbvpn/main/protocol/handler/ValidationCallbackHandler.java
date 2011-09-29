package com.chinarewards.qqgbvpn.main.protocol.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.logic.qqapi.GroupBuyingManager;
import com.chinarewards.qqgbvpn.main.protocol.ServiceHandler;
import com.chinarewards.qqgbvpn.main.protocol.ServiceRequest;
import com.chinarewards.qqgbvpn.main.protocol.ServiceResponse;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ValCallbackRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ValCallbackResponseMessage;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class ValidationCallbackHandler implements ServiceHandler{


	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Inject
	protected Provider<GroupBuyingManager> gbm;
	
	@Override
	public void execute(ServiceRequest request, ServiceResponse response) {
		ValCallbackRequestMessage bodyMessage = (ValCallbackRequestMessage)request.getParameter();
		
		log.debug("ValidationCallbackHandler======execute==bodyMessage=:"+bodyMessage);
		
		// FIXME what the ****? should report error if groupon ID and / or code does not exist.
		ValCallbackResponseMessage  valCallbackResponseMessage  = new ValCallbackResponseMessage();
		log.debug("ValidationCallbackHandler======execute==grouponId({})=====grouponVcode({})=:",new Object[]{bodyMessage.getGrouponId(),bodyMessage.getGrouponVCode()});
		try {
			gbm.get().groupBuyValidateCallBack(bodyMessage.getGrouponId(), bodyMessage.getGrouponVCode());
			valCallbackResponseMessage.setResult(0);
		} catch (Throwable e) {
			e.printStackTrace();
			valCallbackResponseMessage.setResult(1);
		}
		valCallbackResponseMessage.setCmdId(CmdConstant.VAL_CALLBACK_CMD_ID_RESPONSE);
		response.writeResponse(valCallbackResponseMessage);
	}

}
