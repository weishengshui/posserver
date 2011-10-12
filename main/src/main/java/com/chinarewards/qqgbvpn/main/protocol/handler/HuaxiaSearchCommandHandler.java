package com.chinarewards.qqgbvpn.main.protocol.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.logic.huaxia.HuaxiaRedeemManager;
import com.chinarewards.qqgbvpn.main.protocol.ServiceHandler;
import com.chinarewards.qqgbvpn.main.protocol.ServiceRequest;
import com.chinarewards.qqgbvpn.main.protocol.ServiceResponse;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
import com.chinarewards.qqgbvpn.main.protocol.cmd.HuaxiaRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.HuaxiaResponseMessage;
import com.chinarewards.qqgbvpn.main.vo.HuaxiaRedeemVO;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class HuaxiaSearchCommandHandler implements ServiceHandler {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Inject
	public Provider<HuaxiaRedeemManager> mgr;
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(ServiceRequest request, ServiceResponse response) {
		
//		ServiceSession session = request.getSession();
//		log.debug("HuaxiaSearchCommandHandler posId : {}"+String.valueOf(session.getAttribute(LoginFilter.POS_ID)));

		HuaxiaRequestMessage huaxiaRequestMessage = (HuaxiaRequestMessage) request.getParameter();
		HuaxiaResponseMessage huaxiaResponseMessage = new HuaxiaResponseMessage();

		
		HuaxiaRedeemVO params = new HuaxiaRedeemVO();
		params.setCardNum(huaxiaRequestMessage.getCardNum());
		
		HuaxiaRedeemVO redeemCount = mgr.get().huaxiaRedeemSearch(params);
		
		huaxiaResponseMessage.setCmdId(CmdConstant.HUAXIA_BANK_REDEEM_SEARCH_RESPONSE);
		
		if (redeemCount.getRedeemCount() > 0) {
			huaxiaResponseMessage.setResult(HuaxiaRedeemVO.REDEEM_RESULT_SUCCESS);
		} else {
			huaxiaResponseMessage.setResult(HuaxiaRedeemVO.REDEEM_RESULT_NONE);
		}
		
		log.debug("huaxiaRequestMessage : {}" + huaxiaRequestMessage);
		log.debug("HuaxiaSearchCommandHandler Result : {}" + huaxiaResponseMessage.getResult());

		response.writeResponse(huaxiaResponseMessage);
	}

}
