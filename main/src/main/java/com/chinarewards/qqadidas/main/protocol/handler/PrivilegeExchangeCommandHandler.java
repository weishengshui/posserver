package com.chinarewards.qqadidas.main.protocol.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqadidas.main.protocol.cmd.PrivilegeExchangeRequestMessage;
import com.chinarewards.qqadidas.main.protocol.cmd.PrivilegeExchangeResponseMessage;
import com.chinarewards.qqadidas.mian.logic.QQMemberPrivilegeExchangeLogic;
import com.chinarewards.qqgbvpn.main.protocol.ServiceHandler;
import com.chinarewards.qqgbvpn.main.protocol.ServiceRequest;
import com.chinarewards.qqgbvpn.main.protocol.ServiceResponse;
import com.google.inject.Inject;

/**
 * 
 * @author weishengshui
 * 
 */
public class PrivilegeExchangeCommandHandler implements ServiceHandler {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Inject
	QQMemberPrivilegeExchangeLogic logic;

	@Override
	public void execute(ServiceRequest request, ServiceResponse response) {

		PrivilegeExchangeRequestMessage bodyMessage = (PrivilegeExchangeRequestMessage) request
				.getParameter();

		log.debug(
				"PrivilegeExchangeCommandHandler======execute==bodyMessage=: {}",
				bodyMessage);

		PrivilegeExchangeResponseMessage privilegeExchangeResponseMessage = new PrivilegeExchangeResponseMessage();
		privilegeExchangeResponseMessage = logic
				.getExchangeResponseMessageByRequest(request);

		response.writeResponse(privilegeExchangeResponseMessage);

	}

}
