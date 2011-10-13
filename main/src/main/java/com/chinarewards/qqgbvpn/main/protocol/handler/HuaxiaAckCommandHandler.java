package com.chinarewards.qqgbvpn.main.protocol.handler;

import java.text.SimpleDateFormat;
import java.util.Date;

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

public class HuaxiaAckCommandHandler implements ServiceHandler {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Inject
	public Provider<HuaxiaRedeemManager> mgr;
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(ServiceRequest request, ServiceResponse response) {
		
		log.debug("HuaxiaAckCommandHandler start1...");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String txDate = sdf.format(new Date());
		
		ServiceSession session = request.getSession();
		String posId = String.valueOf(session.getAttribute(LoginFilter.POS_ID));
		

		HuaxiaRequestMessage huaxiaRequestMessage = (HuaxiaRequestMessage) request.getParameter();
		HuaxiaResponseMessage huaxiaResponseMessage = new HuaxiaResponseMessage();

		
		HuaxiaRedeemVO params = new HuaxiaRedeemVO();
		params.setCardNum(huaxiaRequestMessage.getCardNum());
		params.setChanceId(huaxiaRequestMessage.getChanceId());
		params.setAckId(huaxiaRequestMessage.getAckId());
		params.setPosId(posId);
		
		log.debug("HuaxiaAckCommandHandler start2...");
		int result = mgr.get().huaxiaRedeemAck(params).getResult();
		log.debug("HuaxiaAckCommandHandler start3...");
		huaxiaResponseMessage.setCmdId(CmdConstant.HUAXIA_BANK_REDEEM_ACK_RESPONSE);
		huaxiaResponseMessage.setResult(result);
		huaxiaResponseMessage.setTxDate(txDate);
		log.debug("HuaxiaAckCommandHandler start4...");
		log.debug("huaxiaRequestMessage : {}" + huaxiaRequestMessage);
		log.debug("HuaxiaAckCommandHandler posId : {}" + posId);
		log.debug("HuaxiaAckCommandHandler Result : {}" + result);
		log.debug("HuaxiaAckCommandHandler TxDate : {}" + txDate);

		response.writeResponse(huaxiaResponseMessage);
	}

}
