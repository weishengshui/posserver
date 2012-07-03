package com.chinarewards.qqadidas.main.protocol.handler;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqadidas.main.logic.QQAdidasManager;
import com.chinarewards.qqadidas.main.protocol.cmd.PrivilegeExchangeRequestMessage;
import com.chinarewards.qqadidas.main.protocol.cmd.PrivilegeExchangeResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.ServiceHandler;
import com.chinarewards.qqgbvpn.main.protocol.ServiceRequest;
import com.chinarewards.qqgbvpn.main.protocol.ServiceResponse;
import com.chinarewards.qqgbvpn.main.protocol.ServiceSession;
import com.chinarewards.qqgbvpn.main.protocol.filter.LoginFilter;
import com.google.inject.Inject;

/**
 * 
 * @author weishengshui
 * 
 */
public class PrivilegeExchangeCommandHandler implements ServiceHandler {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Inject
	private QQAdidasManager qqAdidasManager;
	@Override
	public void execute(ServiceRequest request, ServiceResponse response) {

		PrivilegeExchangeRequestMessage bodyMessage = (PrivilegeExchangeRequestMessage) request
				.getParameter();

		log.debug(
				"PrivilegeExchangeCommandHandler======execute==bodyMessage=: {}",
				bodyMessage);
		ServiceSession session = request.getSession();
		String posId = String.valueOf(session.getAttribute(LoginFilter.POS_ID));
		HashMap< String, Object> params = new HashMap<String, Object>();
		params.put("cdkey", bodyMessage.getCdkey());
		params.put("amount", bodyMessage.getAmount());
		params.put("posId", posId);
		PrivilegeExchangeResponseMessage privilegeExchangeResponseMessage = null;
		privilegeExchangeResponseMessage = qqAdidasManager.privilegeExchangeCommand(params);

		response.writeResponse(privilegeExchangeResponseMessage);

	}

}
