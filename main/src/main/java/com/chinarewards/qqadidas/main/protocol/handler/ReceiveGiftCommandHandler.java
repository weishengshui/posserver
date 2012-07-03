package com.chinarewards.qqadidas.main.protocol.handler;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqadidas.main.logic.QQAdidasManager;
import com.chinarewards.qqadidas.main.protocol.cmd.ReceiveGiftRequestMessage;
import com.chinarewards.qqadidas.main.protocol.cmd.ReceiveGiftResponseMessage;
import com.chinarewards.qqgbvpn.logic.journal.JournalLogic;
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
public class ReceiveGiftCommandHandler implements ServiceHandler {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Inject
	QQAdidasManager qqAdidasManager;
	@Inject
	JournalLogic journalLogic;

	@Override
	public void execute(ServiceRequest request, ServiceResponse response) {

		ReceiveGiftRequestMessage bodyMessage = (ReceiveGiftRequestMessage) request
				.getParameter();

		log.debug("ReceiveGiftHandler======execute==bodyMessage=: {}",
				bodyMessage);

		ReceiveGiftResponseMessage responseMessage = null;

		ServiceSession session = request.getSession();
		String posId = String.valueOf(session.getAttribute(LoginFilter.POS_ID));
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("cdkey", bodyMessage.getCdkey());
		params.put("posId", posId);
		responseMessage = qqAdidasManager.receiveGiftCommand(params);
		response.writeResponse(responseMessage);

	}

}
