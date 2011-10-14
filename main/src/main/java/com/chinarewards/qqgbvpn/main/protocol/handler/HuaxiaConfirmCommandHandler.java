package com.chinarewards.qqgbvpn.main.protocol.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.logic.huaxia.HuaxiaRedeemManager;
import com.chinarewards.qqgbvpn.main.protocol.ServiceHandler;
import com.chinarewards.qqgbvpn.main.protocol.ServiceRequest;
import com.chinarewards.qqgbvpn.main.protocol.ServiceResponse;
import com.chinarewards.qqgbvpn.main.protocol.ServiceSession;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
import com.chinarewards.qqgbvpn.main.protocol.cmd.HuaxiaRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.HuaxiaResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.filter.LoginFilter;
import com.chinarewards.qqgbvpn.main.vo.HuaxiaRedeemVO;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class HuaxiaConfirmCommandHandler implements ServiceHandler {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Inject
	public Provider<HuaxiaRedeemManager> mgr;
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(ServiceRequest request, ServiceResponse response) {
		
		ServiceSession session = request.getSession();
		String posId = String.valueOf(session.getAttribute(LoginFilter.POS_ID));
		

		HuaxiaRequestMessage huaxiaRequestMessage = (HuaxiaRequestMessage) request.getParameter();
		HuaxiaResponseMessage huaxiaResponseMessage = new HuaxiaResponseMessage();

		
		HuaxiaRedeemVO params = new HuaxiaRedeemVO();
		params.setCardNum(huaxiaRequestMessage.getCardNum());
		params.setPosId(posId);
		
		HuaxiaRedeemVO vo = mgr.get().huaxiaRedeemConfirm(params);
		int result = vo.getResult();
		String chanceId = vo.getChanceId();
		String ackId = vo.getAckId();
		
		huaxiaResponseMessage.setCmdId(CmdConstant.HUAXIA_BANK_REDEEM_CONFIRM_RESPONSE);
		huaxiaResponseMessage.setResult(result);
		huaxiaResponseMessage.setTxDate(vo.getTxDate());
		huaxiaResponseMessage.setChanceId(chanceId);
		huaxiaResponseMessage.setAckId(ackId);
		
		log.debug("huaxiaRequestMessage : {}" + huaxiaRequestMessage);
		log.debug("HuaxiaConfirmCommandHandler posId : {}" + posId);
		log.debug("HuaxiaConfirmCommandHandler Result : {}" + result);
		log.debug("HuaxiaConfirmCommandHandler TxDate : {}" + vo.getTxDate());
		log.debug("HuaxiaConfirmCommandHandler chanceId : {}" + chanceId);
		log.debug("HuaxiaConfirmCommandHandler ackId : {}" + ackId);

		response.writeResponse(huaxiaResponseMessage);
	}

}
