package com.chinarewards.qqadidas.main.protocol.handler;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqadidas.main.logic.QQAdidasManager;
import com.chinarewards.qqadidas.main.protocol.cmd.WeixinSignInRequestMessage;
import com.chinarewards.qqadidas.main.protocol.cmd.WeixinSignInResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.ServiceHandler;
import com.chinarewards.qqgbvpn.main.protocol.ServiceRequest;
import com.chinarewards.qqgbvpn.main.protocol.ServiceResponse;
import com.chinarewards.qqgbvpn.main.protocol.filter.LoginFilter;
import com.google.inject.Inject;

/**
 * 
 * @author weishengshui
 * 
 */
public class WeixinSignInCommandHandler implements ServiceHandler {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Inject
	QQAdidasManager qqAdidasManager;

	@Override
	public void execute(ServiceRequest request, ServiceResponse response) {

		WeixinSignInRequestMessage bodyMessage = (WeixinSignInRequestMessage) request
				.getParameter();

		log.debug("WeixinSignInCommandHandler======execute==bodyMessage=: {}",
				bodyMessage);

		WeixinSignInResponseMessage responseMessage = null;

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("weixinNo", bodyMessage.getWeixinNo());
		params.put("posId", String.valueOf(request.getSession().getAttribute(LoginFilter.POS_ID)));
		
		responseMessage = qqAdidasManager.weixinSignInCommand(params);

		response.writeResponse(responseMessage);

	}

}
