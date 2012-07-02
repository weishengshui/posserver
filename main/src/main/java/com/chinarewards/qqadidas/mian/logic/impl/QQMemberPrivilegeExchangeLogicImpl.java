package com.chinarewards.qqadidas.mian.logic.impl;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqadidas.domain.QQActivityHistory;
import com.chinarewards.qqadidas.domain.status.GiftType;
import com.chinarewards.qqadidas.domain.status.PrivilegeStatus;
import com.chinarewards.qqadidas.main.protocol.cmd.PrivilegeExchangeRequestMessage;
import com.chinarewards.qqadidas.main.protocol.cmd.PrivilegeExchangeResponseMessage;
import com.chinarewards.qqadidas.mian.dao.QQMemberPrivilegeExchangeDao;
import com.chinarewards.qqadidas.mian.logic.QQMemberPrivilegeExchangeLogic;
import com.chinarewards.qqgbvpn.domain.event.DomainEntity;
import com.chinarewards.qqgbvpn.domain.event.DomainEvent;
import com.chinarewards.qqgbvpn.logic.journal.JournalLogic;
import com.chinarewards.qqgbvpn.main.protocol.ServiceRequest;
import com.chinarewards.qqgbvpn.main.protocol.ServiceSession;
import com.chinarewards.qqgbvpn.main.protocol.filter.LoginFilter;
import com.google.inject.Inject;

public class QQMemberPrivilegeExchangeLogicImpl implements
		QQMemberPrivilegeExchangeLogic {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Inject
	QQMemberPrivilegeExchangeDao dao;
	@Inject
	JournalLogic journalLogic;

	@Override
	public boolean cdkeyExists(String cdkey) {
		return dao.cdkeyExists(cdkey);
	}

	@Override
	public PrivilegeStatus getPrivilegeStatusByCdkey(String cdkey) {
		return dao.getPrivilegeStatusByCdkey(cdkey);
	}

	@Override
	public void setPrivilegeStatus(String cdkey, PrivilegeStatus privilegeStatus) {
		dao.setPrivilegeStatus(cdkey, privilegeStatus);
	}

	@Override
	public int getResultStatusByCdkeyAmount(String cdkey, String amount) {

		boolean iscdkeyExists = dao.cdkeyExists(cdkey);
		if (!iscdkeyExists) {
			return CDKEY_NOT_EXISTS;
		}
		double amountD = Double.parseDouble(amount);
		if (amountD < 300) {
			return AMOUNT_INVALID;
		}
		PrivilegeStatus privilegeStatus = dao.getPrivilegeStatusByCdkey(cdkey);
		if (privilegeStatus == PrivilegeStatus.DONE) {
			return PRIVILEGE_DONE;
		}
		if (amountD < 600) {
			if (privilegeStatus == PrivilegeStatus.NEW) {
				dao.setPrivilegeStatus(cdkey, PrivilegeStatus.HALF);
				log.info(
						"update QQActivityMember set PrivilegeStatus from {} to {} where cdkey={}",
						new Object[] { privilegeStatus, PrivilegeStatus.HALF,
								cdkey });
				return PRIVILEGE_FIRST_HALF;
			} else if (privilegeStatus == PrivilegeStatus.HALF) {
				dao.setPrivilegeStatus(cdkey, PrivilegeStatus.DONE);
				log.info(
						"update QQActivityMember set PrivilegeStatus from {} to {} where cdkey={}",
						new Object[] { privilegeStatus, PrivilegeStatus.DONE,
								cdkey });
				return PRIVILEGE_SECOND_HALF;
			}
		} else {
			if (privilegeStatus == PrivilegeStatus.NEW) {
				dao.setPrivilegeStatus(cdkey, PrivilegeStatus.DONE);
				log.info(
						"update QQActivityMember set PrivilegeStatus from {} to {} where cdkey={}",
						new Object[] { privilegeStatus, PrivilegeStatus.DONE,
								cdkey });
				return PRIVILEGE_ALL;
			} else if (privilegeStatus == PrivilegeStatus.HALF) {
				dao.setPrivilegeStatus(cdkey, PrivilegeStatus.DONE);
				log.info(
						"update QQActivityMember set PrivilegeStatus from {} to {} where cdkey={}",
						new Object[] { privilegeStatus, PrivilegeStatus.DONE,
								cdkey });
				return PRIVILEGE_SECOND_HALF;
			}
		}
		return ERROR_CODE_OTHERS;
	}

	@Override
	public QQActivityHistory getActivityHistoryByCdkeyAType(String cdkey,
			GiftType type) {
		return dao.getActivityHistoryByCdkeyAType(cdkey, type);
	}

	@Override
	public PrivilegeExchangeResponseMessage getExchangeResponseMessageByRequest(
			ServiceRequest request) {

		String domainEvent = DomainEvent.QQADIDAS_PRIVILEGE_EXCHANGE_FAILED
				.toString();

		PrivilegeExchangeRequestMessage bodyMessage = (PrivilegeExchangeRequestMessage) request
				.getParameter();
		PrivilegeExchangeResponseMessage privilegeExchangeResponseMessage = new PrivilegeExchangeResponseMessage();

		Date now = new Date();
		Calendar xact = Calendar.getInstance();
		xact.setTime(now);

		String title = null;
		String tip = null;
		String cdkey = bodyMessage.getCdkey();
		String amount = bodyMessage.getAmount();
		ServiceSession session = request.getSession();
		String posId = String.valueOf(session.getAttribute(LoginFilter.POS_ID));
		QQActivityHistory qqActivityHistory = null;
		double amountD = Double.parseDouble(amount);
		log.debug("privilege exchange cdkey is: " + cdkey);
		int resultCode = getResultStatusByCdkeyAmount(cdkey, amount);

		// 只有3中情况response的result是success，而且只有只有这三种情况需要title
		if (resultCode == QQMemberPrivilegeExchangeLogic.PRIVILEGE_ALL
				|| resultCode == QQMemberPrivilegeExchangeLogic.PRIVILEGE_FIRST_HALF
				|| resultCode == QQMemberPrivilegeExchangeLogic.PRIVILEGE_SECOND_HALF) {
			privilegeExchangeResponseMessage
					.setResult(PrivilegeExchangeResponseMessage.RESPONSE_RESULT_SUCCESS);
			domainEvent = DomainEvent.QQADIDAS_PRIVILEGE_EXCHANGE_OK.toString();
			title = PrivilegeExchangeResponseMessage.TITLE;
		}

		if (resultCode == QQMemberPrivilegeExchangeLogic.CDKEY_NOT_EXISTS) {
			// case 1: cdkey不正确
			privilegeExchangeResponseMessage
					.setResult(PrivilegeExchangeResponseMessage.RESPONSE_RESULT_CDKEY_INVALID);
			log.info("cdkey={}无效。", cdkey);
		} else if (resultCode == QQMemberPrivilegeExchangeLogic.AMOUNT_INVALID) {
			// case 2: 消费金额不足
			privilegeExchangeResponseMessage
					.setResult(PrivilegeExchangeResponseMessage.RESPONSE_RESULT_AMOUNT_INVALID);
			log.info("cdkey={}正确、amount={}<300，未能获得优惠。", cdkey, amount);
		} else if (resultCode == QQMemberPrivilegeExchangeLogic.PRIVILEGE_FIRST_HALF) {
			// case 3: 会员之前未消费过，本次消费为300-599，返回50元优惠
			log.info("cdkey={}正确、amount={}，首次获得优惠50元。", cdkey, amount);
			tip = cdkey + "本次消费" + amount + "元，即刻享受50元折扣优惠，折扣后实际支付为"
					+ (amountD - 50)
					+ "元。您尚有一次满额折抵50元优惠可用。本券打印后仅限当次使用。即日起至6月19日止。";
			qqActivityHistory = new QQActivityHistory();
			qqActivityHistory.setCdkey(cdkey);
			qqActivityHistory.setAType(GiftType.PRIVILEGE);
			qqActivityHistory.setConsumeAmt(Double.parseDouble(amount));
			qqActivityHistory.setRebateAmt(50);
			qqActivityHistory.setPosId(posId);
			qqActivityHistory.setCreatedAt(now);
			qqActivityHistory.setLastModifiedAt(now);
			dao.addQQActivityHistory(qqActivityHistory);
		} else if (resultCode == QQMemberPrivilegeExchangeLogic.PRIVILEGE_ALL) {
			// case 4: 会员之前未消费过，本次消费为600元及以上，返回100元优惠
			tip = cdkey + "本次消费" + amount + "元，即刻享受100元折扣优惠，折扣后实际支付为"
					+ (amountD - 100) + "元。本券打印后仅限当次使用。即日起至6月19日止。";
			log.info("cdkey={}正确、amount={}，首次获得优惠100元，全部优惠使用完毕。", cdkey, amount);
			qqActivityHistory = new QQActivityHistory();
			qqActivityHistory.setCdkey(cdkey);
			qqActivityHistory.setAType(GiftType.PRIVILEGE);
			qqActivityHistory.setConsumeAmt(Double.parseDouble(amount));
			qqActivityHistory.setRebateAmt(100);
			qqActivityHistory.setPosId(posId);
			qqActivityHistory.setCreatedAt(now);
			qqActivityHistory.setLastModifiedAt(now);
			dao.addQQActivityHistory(qqActivityHistory);
		} else if (resultCode == QQMemberPrivilegeExchangeLogic.PRIVILEGE_SECOND_HALF) {
			// case 5: 会员之前已经返回50元，本次消费金额为300元及以上
			log.info("cdkey={}正确、amount={}，第二次获得优惠50元，全部优惠使用完毕。", cdkey, amount);
			QQActivityHistory activityHistory = getActivityHistoryByCdkeyAType(
					cdkey, GiftType.PRIVILEGE);
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			tip = cdkey + "在"
					+ format.format(activityHistory.getLastModifiedAt())
					+ "获得50元折扣优惠。本次消费 " + amount + "元，即刻享受50元折扣优惠，折扣后实际支付额为"
					+ (amountD - 50) + "元。本券打印后仅限当次使用。即日起至6月19日止。";
			qqActivityHistory = new QQActivityHistory();
			qqActivityHistory.setCdkey(cdkey);
			qqActivityHistory.setAType(GiftType.PRIVILEGE);
			qqActivityHistory.setConsumeAmt(Double.parseDouble(amount));
			qqActivityHistory.setRebateAmt(50);
			qqActivityHistory.setPosId(posId);
			qqActivityHistory.setCreatedAt(now);
			qqActivityHistory.setLastModifiedAt(now);
			dao.addQQActivityHistory(qqActivityHistory);
		} else if (resultCode == QQMemberPrivilegeExchangeLogic.PRIVILEGE_DONE) {
			// case 6: 会员的优惠已经全部使用
			log.info("cdkey={}正确、amount={}，未能获得优惠，优惠已全部使用完毕。", cdkey, amount);
			privilegeExchangeResponseMessage
					.setResult(PrivilegeExchangeResponseMessage.RESPONSE_RESULT_ALL_USE);
		} else if (resultCode == QQMemberPrivilegeExchangeLogic.ERROR_CODE_OTHERS) {
			// case 7: 其它未知错误
			privilegeExchangeResponseMessage
					.setResult(PrivilegeExchangeResponseMessage.RESPONSE_RESULT_ERROR_OTHERS);
		}
		Charset charset = Charset.forName("gbk");
		privilegeExchangeResponseMessage.setXact_time(xact);
		if (title == null) {
			privilegeExchangeResponseMessage.setTitleLength(0);
		} else {
			privilegeExchangeResponseMessage.setTitleLength(title
					.getBytes(charset).length);
		}
		privilegeExchangeResponseMessage.setTitle(title);
		if (tip == null) {
			privilegeExchangeResponseMessage.setTipLength(0);
		} else {
			privilegeExchangeResponseMessage
					.setTipLength(tip.getBytes(charset).length);
		}
		privilegeExchangeResponseMessage.setTip(tip);

		privilegeExchangeResponseMessage
				.setCmdId(PrivilegeExchangeResponseMessage.RECEIVE_GIFT_CMD_ID_RESPONSE);

		ObjectMapper mapper = new ObjectMapper();
		String eventDetail = null;
		try {
			eventDetail = mapper
					.writeValueAsString(privilegeExchangeResponseMessage);
		} catch (Exception e) {
			log.error("mapping PrivilegeExchangeResponse error.", e);
			eventDetail = e.toString();
		}
		journalLogic.logEvent(domainEvent,
				DomainEntity.QQACTIVITYMEMBER.toString(),
				bodyMessage.getCdkey(), eventDetail);
		if (qqActivityHistory != null) {
			try {
				eventDetail = mapper.writeValueAsString(qqActivityHistory);
			} catch (Exception e) {
				eventDetail = e.toString();
			}
			journalLogic.logEvent(
					DomainEvent.QQADIDAS_QQACTIVITYHISTORY_CREATE,
					DomainEntity.QQACTIVITYHISTORY,
					qqActivityHistory.getCdkey(), eventDetail);
		}

		return privilegeExchangeResponseMessage;
	}

	@Override
	public void updateQQActivityHistory(QQActivityHistory qqActivityHistory) {
		dao.updateQQActivityHistory(qqActivityHistory);
	}

	@Override
	public void addQQActivityHistory(QQActivityHistory qqActivityHistory) {
		dao.addQQActivityHistory(qqActivityHistory);
	}
}
