package com.chinarewards.qqadidas.main.protocol.handler;

import java.util.Date;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqadidas.domain.QQWeixinSignIn;
import com.chinarewards.qqadidas.main.protocol.cmd.WeixinSignInRequestMessage;
import com.chinarewards.qqadidas.main.protocol.cmd.WeixinSignInResponseMessage;
import com.chinarewards.qqadidas.mian.logic.WeixinSignInLogic;
import com.chinarewards.qqgbvpn.domain.event.DomainEntity;
import com.chinarewards.qqgbvpn.domain.event.DomainEvent;
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
public class WeixinSignInCommandHandler implements ServiceHandler {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Inject
	private WeixinSignInLogic logic;
	@Inject
	private JournalLogic journalLogic;

	@Override
	public void execute(ServiceRequest request, ServiceResponse response) {

		String domainEvent = DomainEvent.QQADIDAS_WEIXIN_SIGNIN_FAILED.toString();
		WeixinSignInRequestMessage bodyMessage = (WeixinSignInRequestMessage) request
				.getParameter();

		log.debug("WeixinSignInCommandHandler======execute==bodyMessage=: {}",
				bodyMessage);
		QQWeixinSignIn qqWeixinSignIn;
		WeixinSignInResponseMessage responseMessage = new WeixinSignInResponseMessage();

		String weixinNo = bodyMessage.getWeixinNo();
		if (weixinNo == null || weixinNo.trim().length() < 1
				|| weixinNo.trim().length() > 16) {
			responseMessage.setResult(WeixinSignInResponseMessage.RESULT_ERROR);
		} else {
			ServiceSession session = request.getSession();
			String posId = String.valueOf(session
					.getAttribute(LoginFilter.POS_ID));
			Date signInDate = new Date();
			qqWeixinSignIn = new QQWeixinSignIn();
			qqWeixinSignIn.setWeixinNo(weixinNo);
			qqWeixinSignIn.setPosId(posId);
			qqWeixinSignIn.setCreatedAt(signInDate);
			qqWeixinSignIn.setLastModifiedAt(signInDate);
			if (logic.isSignInSuccess(qqWeixinSignIn)) {
				responseMessage
						.setResult(WeixinSignInResponseMessage.RESULT_SUCCESS);
				domainEvent = DomainEvent.QQADIDAS_WEIXIN_SIGNIN_OK.toString();
			} else {
				responseMessage
						.setResult(WeixinSignInResponseMessage.RESULT_ERROR);
			}
		}
		
		responseMessage
				.setCmdId(WeixinSignInResponseMessage.WEIXIN_SIGN_IN_CMD_ID_RESPONSE);

		ObjectMapper mapper = new ObjectMapper();
		String eventDetail = null;
		try {
			eventDetail = mapper.writeValueAsString(responseMessage);
		} catch (Exception e) {
			log.error("mapping WeixinSignInResponse error.",e);
			eventDetail = e.toString();
		}
		journalLogic.logEvent(domainEvent, DomainEntity.QQWEIXINSIGNIN.toString(), bodyMessage.getWeixinNo(), eventDetail);
		
		response.writeResponse(responseMessage);

	}

}
