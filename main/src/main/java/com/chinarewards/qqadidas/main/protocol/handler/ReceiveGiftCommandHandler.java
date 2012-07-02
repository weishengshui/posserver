package com.chinarewards.qqadidas.main.protocol.handler;

import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqadidas.domain.QQActivityHistory;
import com.chinarewards.qqadidas.domain.QQActivityMember;
import com.chinarewards.qqadidas.domain.status.GiftStatus;
import com.chinarewards.qqadidas.domain.status.GiftType;
import com.chinarewards.qqadidas.main.protocol.cmd.ReceiveGiftRequestMessage;
import com.chinarewards.qqadidas.main.protocol.cmd.ReceiveGiftResponseMessage;
import com.chinarewards.qqadidas.mian.logic.QQMemberReceiveGiftLogic;
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
public class ReceiveGiftCommandHandler implements ServiceHandler {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Inject
	QQMemberReceiveGiftLogic qqMemberReceiveGift;
	@Inject
	JournalLogic journalLogic;

	@Override
	public void execute(ServiceRequest request, ServiceResponse response) {

		ReceiveGiftRequestMessage bodyMessage = (ReceiveGiftRequestMessage) request
				.getParameter();

		log.debug("ReceiveGiftHandler======execute==bodyMessage=: {}",
				bodyMessage);

		ReceiveGiftResponseMessage responseMessage = new ReceiveGiftResponseMessage();

		Date now = new Date();
		Calendar xact = Calendar.getInstance();
		xact.setTime(now);

		String title = null;
		String tip = null;
		String domainEvent = DomainEvent.QQADIDAS_RECEIVE_GIFT_FAILED
				.toString();
		ServiceSession session = request.getSession();
		String posId = String.valueOf(session.getAttribute(LoginFilter.POS_ID));
		QQActivityHistory qah;
		String cdkey = bodyMessage.getCdkey();
		QQActivityMember qm = qqMemberReceiveGift
				.getQQActivityMemberByCdkey(cdkey);
		if (cdkey == null || cdkey.trim().length() < 1
				|| cdkey.trim().length() > 16) {
			responseMessage.setResult(ReceiveGiftResponseMessage.CDKEY_INVALID);
		} else if (qm == null) {
			responseMessage.setResult(ReceiveGiftResponseMessage.CDKEY_INVALID);
		} else {
			log.debug("receive gift cdkey is: " + cdkey);
			if (qm.getGiftStatus() == GiftStatus.NEW) {
				qah = new QQActivityHistory();
				qah.setCdkey(cdkey);
				qah.setAType(GiftType.GIFT);
				qah.setPosId(posId);
				qah.setCreatedAt(now);
				qah.setLastModifiedAt(now);
				if (qqMemberReceiveGift.addQQActivityHistory(qah)) {
					log.info(
							"successfully added a QQActivityHistory: cdkey={}, GiftType={}, CreatedAt={}, lastModifiedAt={}",
							new Object[] { cdkey, GiftType.GIFT, now, now });

					qm.setGiftStatus(GiftStatus.DONE);
					if (qqMemberReceiveGift.updateQQActivityMember(qm)) {
						title = ReceiveGiftResponseMessage.TITLE;
						tip = ReceiveGiftResponseMessage.TIP;
						responseMessage
								.setResult(ReceiveGiftResponseMessage.SUCCESS_CODE);
						domainEvent = DomainEvent.QQADIDAS_RECEIVE_GIFT_OK.toString();
						log.debug(
								"update QQActivityMember GiftStatus from GiftStatus.NEW to GiftStatus.DONE where cdkey = {}",
								cdkey);
						log.debug("the gift status has changed");
					} else {
						responseMessage
								.setResult(ReceiveGiftResponseMessage.ERROR_CODE_OTHER);
					}

				} else {
					responseMessage
							.setResult(ReceiveGiftResponseMessage.ERROR_CODE_OTHER);
				}
			} else if (qm.getGiftStatus() == GiftStatus.DONE) {
				responseMessage
						.setResult(ReceiveGiftResponseMessage.ALREADY_GET);
			} else {
				responseMessage
						.setResult(ReceiveGiftResponseMessage.ERROR_CODE_OTHER);
			}
		}
		Charset charset = Charset.forName("gbk");
		responseMessage.setXact_time(xact);
		if (title != null && tip != null) {
			responseMessage.setTitleLength(title.getBytes(charset).length);
			responseMessage.setTipLength(tip.getBytes(charset).length);
			responseMessage.setTitle(title);
			responseMessage.setTip(tip);
		} else {
			responseMessage.setTitleLength(0);
			responseMessage.setTipLength(0);
		}

		responseMessage
				.setCmdId(ReceiveGiftResponseMessage.RECEIVE_GIFT_CMD_ID_RESPONSE);
		//Add journal
		ObjectMapper mapper = new ObjectMapper();
		String eventDetail = null;
		try {
			eventDetail = mapper.writeValueAsString(responseMessage);
		} catch (Exception e) {
			log.error("mapping ReceiveGiftResponse error.",e);
			eventDetail = e.toString();
		}
		journalLogic.logEvent(domainEvent, DomainEntity.QQACTIVITYMEMBER.toString(), bodyMessage.getCdkey(), eventDetail);
		
		response.writeResponse(responseMessage);

	}

}
