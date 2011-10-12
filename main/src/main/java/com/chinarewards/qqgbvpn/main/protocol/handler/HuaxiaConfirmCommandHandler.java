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

public class HuaxiaConfirmCommandHandler implements ServiceHandler {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Inject
	public Provider<HuaxiaRedeemManager> mgr;
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(ServiceRequest request, ServiceResponse response) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
		String txDate = sdf.format(new Date());
		
		ServiceSession session = request.getSession();
		String posId = String.valueOf(session.getAttribute(LoginFilter.POS_ID));
		

		HuaxiaRequestMessage huaxiaRequestMessage = (HuaxiaRequestMessage) request.getParameter();
		HuaxiaResponseMessage huaxiaResponseMessage = new HuaxiaResponseMessage();

		
		HuaxiaRedeemVO params = new HuaxiaRedeemVO();
		params.setCardNum(huaxiaRequestMessage.getCardNum());
		params.setPosId(posId);
		
		int result = mgr.get().huaxiaRedeemConfirm(params).getResult();
		
		huaxiaResponseMessage.setCmdId(CmdConstant.HUAXIA_BANK_REDEEM_CONFIRM_RESPONSE);
		huaxiaResponseMessage.setResult(result);
		huaxiaResponseMessage.setTxDate(txDate);
		
		log.debug("huaxiaRequestMessage : {}" + huaxiaRequestMessage);
		log.debug("HuaxiaConfirmCommandHandler posId : {}" + posId);
		log.debug("HuaxiaConfirmCommandHandler Result : {}" + result);
		log.debug("HuaxiaConfirmCommandHandler TxDate : {}" + txDate);

		response.writeResponse(huaxiaResponseMessage);
	}

}
