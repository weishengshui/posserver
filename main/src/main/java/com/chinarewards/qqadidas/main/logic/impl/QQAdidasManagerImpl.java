package com.chinarewards.qqadidas.main.logic.impl;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqadidas.domain.QQActivityHistory;
import com.chinarewards.qqadidas.domain.QQActivityMember;
import com.chinarewards.qqadidas.domain.QQWeixinSignIn;
import com.chinarewards.qqadidas.domain.status.GiftStatus;
import com.chinarewards.qqadidas.domain.status.GiftType;
import com.chinarewards.qqadidas.domain.status.PrivilegeStatus;
import com.chinarewards.qqadidas.main.dao.QQAdidasManagerDao;
import com.chinarewards.qqadidas.main.logic.QQAdidasManager;
import com.chinarewards.qqadidas.main.protocol.cmd.PrivilegeExchangeResponseMessage;
import com.chinarewards.qqadidas.main.protocol.cmd.ReceiveGiftResponseMessage;
import com.chinarewards.qqadidas.main.protocol.cmd.WeixinSignInResponseMessage;
import com.chinarewards.qqgbvpn.domain.event.DomainEntity;
import com.chinarewards.qqgbvpn.domain.event.DomainEvent;
import com.chinarewards.qqgbvpn.logic.journal.JournalLogic;
import com.google.inject.Inject;

public class QQAdidasManagerImpl implements QQAdidasManager {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	@Inject
	private JournalLogic journalLogic;
	@Inject
	private QQAdidasManagerDao dao;
	@Override
	public ReceiveGiftResponseMessage receiveGiftCommand(
			HashMap<String, Object> params) {
		ReceiveGiftResponseMessage responseMessage = new ReceiveGiftResponseMessage();

		Date now = new Date();
		Calendar xact = Calendar.getInstance();
		xact.setTime(now);

		String title = null;
		String tip = null;
		String domainEvent = DomainEvent.QQADIDAS_RECEIVE_GIFT_FAILED
				.toString();
		String posId = (String)params.get("posId");
		String cdkey = (String)params.get("cdkey");
		QQActivityHistory qah;
		QQActivityMember qm = getQQActivityMemberByCdkey(cdkey);
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
				if (addQQActivityHistory(qah)) {
					log.info(
							"successfully added a QQActivityHistory: cdkey={}, GiftType={}, CreatedAt={}, lastModifiedAt={}",
							new Object[] { cdkey, GiftType.GIFT, now, now });

					qm.setGiftStatus(GiftStatus.DONE);
					if (updateQQActivityMember(qm)) {
						title = ReceiveGiftResponseMessage.TITLE;
						tip = ReceiveGiftResponseMessage.TIP;
						responseMessage
								.setResult(ReceiveGiftResponseMessage.SUCCESS_CODE);
						domainEvent = DomainEvent.QQADIDAS_RECEIVE_GIFT_OK
								.toString();
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
		// Add journal
		ObjectMapper mapper = new ObjectMapper();
		String eventDetail = null;
		try {
			eventDetail = mapper.writeValueAsString(responseMessage);
		} catch (Exception e) {
			log.error("mapping ReceiveGiftResponse error.", e);
			eventDetail = e.toString();
		}
		journalLogic.logEvent(domainEvent,
				DomainEntity.QQACTIVITYMEMBER.toString(),
				cdkey, eventDetail);

		return responseMessage;
	}

	@Override
	public PrivilegeExchangeResponseMessage privilegeExchangeCommand(
			HashMap<String, Object> params) {
		
		String domainEvent = DomainEvent.QQADIDAS_PRIVILEGE_EXCHANGE_FAILED
				.toString();

		PrivilegeExchangeResponseMessage privilegeExchangeResponseMessage = new PrivilegeExchangeResponseMessage();

		Date now = new Date();
		Calendar xact = Calendar.getInstance();
		xact.setTime(now);

		String title = null;
		String tip = null;
		String posId = (String)params.get("posId");
		String cdkey = (String)params.get("cdkey");
		String amount = (String)params.get("amount");
		QQActivityHistory qqActivityHistory = null;
		int resultCode;
		double amountD = Double.parseDouble(amount);
		log.debug("privilege exchange cdkey is: " + cdkey);

		resultCode = getResultStatusByCdkeyAmount(cdkey, amount);

		// 只有3中情况response的result是success，而且只有只有这三种情况需要title
		if (resultCode == PrivilegeExchangeResponseMessage.PRIVILEGE_ALL
				|| resultCode == PrivilegeExchangeResponseMessage.PRIVILEGE_FIRST_HALF
				|| resultCode == PrivilegeExchangeResponseMessage.PRIVILEGE_SECOND_HALF) {
			privilegeExchangeResponseMessage
					.setResult(PrivilegeExchangeResponseMessage.RESPONSE_RESULT_SUCCESS);
			domainEvent = DomainEvent.QQADIDAS_PRIVILEGE_EXCHANGE_OK.toString();
			title = PrivilegeExchangeResponseMessage.TITLE;
		}

		if (resultCode == PrivilegeExchangeResponseMessage.CDKEY_NOT_EXISTS) {
			// case 1: cdkey不正确
			privilegeExchangeResponseMessage
					.setResult(PrivilegeExchangeResponseMessage.RESPONSE_RESULT_CDKEY_INVALID);
			log.info("cdkey={}无效。", cdkey);
		} else if (resultCode == PrivilegeExchangeResponseMessage.AMOUNT_INVALID) {
			// case 2: 消费金额不足
			privilegeExchangeResponseMessage
					.setResult(PrivilegeExchangeResponseMessage.RESPONSE_RESULT_AMOUNT_INVALID);
			log.info("cdkey={}正确、amount={}<300，未能获得优惠。", cdkey, amount);
		} else if (resultCode == PrivilegeExchangeResponseMessage.PRIVILEGE_FIRST_HALF) {
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
		} else if (resultCode == PrivilegeExchangeResponseMessage.PRIVILEGE_ALL) {
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
		} else if (resultCode == PrivilegeExchangeResponseMessage.PRIVILEGE_SECOND_HALF) {
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
		} else if (resultCode == PrivilegeExchangeResponseMessage.PRIVILEGE_DONE) {
			// case 6: 会员的优惠已经全部使用
			log.info("cdkey={}正确、amount={}，未能获得优惠，优惠已全部使用完毕。", cdkey, amount);
			privilegeExchangeResponseMessage
					.setResult(PrivilegeExchangeResponseMessage.RESPONSE_RESULT_ALL_USE);
		} else if (resultCode == PrivilegeExchangeResponseMessage.ERROR_CODE_OTHERS) {
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
				cdkey, eventDetail);
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
	public WeixinSignInResponseMessage weixinSignInCommand(
			HashMap<String, Object> params) {
		
		String domainEvent = DomainEvent.QQADIDAS_WEIXIN_SIGNIN_FAILED
				.toString();

		WeixinSignInResponseMessage responseMessage = new WeixinSignInResponseMessage();

		String weixinNo = (String)params.get("weixinNo");
		String posId = (String)params.get("posId");
		QQWeixinSignIn qqWeixinSignIn;
		
		if (weixinNo == null || weixinNo.trim().length() < 1
				|| weixinNo.trim().length() > 16) {
			responseMessage.setResult(WeixinSignInResponseMessage.RESULT_ERROR);
		} else {
			Date signInDate = new Date();
			qqWeixinSignIn = new QQWeixinSignIn();
			qqWeixinSignIn.setWeixinNo(weixinNo);
			qqWeixinSignIn.setPosId(posId);
			qqWeixinSignIn.setCreatedAt(signInDate);
			qqWeixinSignIn.setLastModifiedAt(signInDate);
			if (isSignInSuccess(qqWeixinSignIn)) {
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
			log.error("mapping WeixinSignInResponse error.", e);
			eventDetail = e.toString();
		}
		journalLogic.logEvent(domainEvent,
				DomainEntity.QQWEIXINSIGNIN.toString(),
				weixinNo, eventDetail);

		return responseMessage;
	}


	public QQActivityMember getQQActivityMemberByCdkey(String cdkey) {
		return dao.getQQActivityMemberByCdkey(cdkey);
	}

	@Override
	public boolean updateQQActivityMember(QQActivityMember qqActivityMember) {
		return dao.updateQQActivityMember(qqActivityMember);
	}

	@Override
	public boolean giftStatus(String cdkey) {
		return dao.giftStatus(cdkey);
	}

	@Override
	public boolean addQQActivityHistory(QQActivityHistory qah) {
		return dao.addQQActivityHistory(qah);
	}	
	
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
			return PrivilegeExchangeResponseMessage.CDKEY_NOT_EXISTS;
		}
		double amountD = Double.parseDouble(amount);
		if (amountD < 300) {
			return PrivilegeExchangeResponseMessage.AMOUNT_INVALID;
		}
		PrivilegeStatus privilegeStatus = dao.getPrivilegeStatusByCdkey(cdkey);
		if (privilegeStatus == PrivilegeStatus.DONE) {
			return PrivilegeExchangeResponseMessage.PRIVILEGE_DONE;
		}
		if (amountD < 600) {
			if (privilegeStatus == PrivilegeStatus.NEW) {
				dao.setPrivilegeStatus(cdkey, PrivilegeStatus.HALF);
				log.info(
						"update QQActivityMember set PrivilegeStatus from {} to {} where cdkey={}",
						new Object[] { privilegeStatus, PrivilegeStatus.HALF,
								cdkey });
				return PrivilegeExchangeResponseMessage.PRIVILEGE_FIRST_HALF;
			} else if (privilegeStatus == PrivilegeStatus.HALF) {
				dao.setPrivilegeStatus(cdkey, PrivilegeStatus.DONE);
				log.info(
						"update QQActivityMember set PrivilegeStatus from {} to {} where cdkey={}",
						new Object[] { privilegeStatus, PrivilegeStatus.DONE,
								cdkey });
				return PrivilegeExchangeResponseMessage.PRIVILEGE_SECOND_HALF;
			}
		} else {
			if (privilegeStatus == PrivilegeStatus.NEW) {
				dao.setPrivilegeStatus(cdkey, PrivilegeStatus.DONE);
				log.info(
						"update QQActivityMember set PrivilegeStatus from {} to {} where cdkey={}",
						new Object[] { privilegeStatus, PrivilegeStatus.DONE,
								cdkey });
				return PrivilegeExchangeResponseMessage.PRIVILEGE_ALL;
			} else if (privilegeStatus == PrivilegeStatus.HALF) {
				dao.setPrivilegeStatus(cdkey, PrivilegeStatus.DONE);
				log.info(
						"update QQActivityMember set PrivilegeStatus from {} to {} where cdkey={}",
						new Object[] { privilegeStatus, PrivilegeStatus.DONE,
								cdkey });
				return PrivilegeExchangeResponseMessage.PRIVILEGE_SECOND_HALF;
			}
		}
		return PrivilegeExchangeResponseMessage.ERROR_CODE_OTHERS;
	}

	@Override
	public QQActivityHistory getActivityHistoryByCdkeyAType(String cdkey,
			GiftType type) {
		return dao.getActivityHistoryByCdkeyAType(cdkey, type);
	}

	public boolean isSignInSuccess(QQWeixinSignIn qqWeixinSignIn) {
		if (dao.addQQWeinxinSignIn(qqWeixinSignIn)) {
			log.info(
					"successfully added a QQWeixin SignIn resord: weixinNo={}, posId={}, signInDateTime={}",
					new Object[] { qqWeixinSignIn.getWeixinNo(),
							qqWeixinSignIn.getPosId(),
							qqWeixinSignIn.getCreatedAt() });
			return true;
		}else{
			log.info(
					"failure to add a QQWeixin SignIn resord: weixinNo={}, posId={}, signInDateTime={}",
					new Object[] { qqWeixinSignIn.getWeixinNo(),
							qqWeixinSignIn.getPosId(),
							qqWeixinSignIn.getCreatedAt() });
			return false;
		}
	}
	
	

}
