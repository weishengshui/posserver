package com.chinarewards.qqadidas.mian.logic;

import com.chinarewards.qqadidas.domain.QQActivityHistory;
import com.chinarewards.qqadidas.domain.status.GiftType;
import com.chinarewards.qqadidas.domain.status.PrivilegeStatus;
import com.chinarewards.qqadidas.main.protocol.cmd.PrivilegeExchangeRequestMessage;
import com.chinarewards.qqadidas.main.protocol.cmd.PrivilegeExchangeResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.ServiceRequest;

public interface QQMemberPrivilegeExchangeLogic {

	// cdkey错误
	public static final int CDKEY_NOT_EXISTS = -1;
	// 消费金额不足
	public static final int AMOUNT_INVALID = 0;
	// 会员之前已经返还100元
	public static final int PRIVILEGE_DONE = 1;
	// 会员之前未消费过，本次消费金额为300-599，返回50元优惠
	public static final int PRIVILEGE_FIRST_HALF = 2;
	// 会员之前已经返还过50元，本次消费金额为300及以上
	public static final int PRIVILEGE_SECOND_HALF = 3;
	// 会员之前未消费过，本次消费金额为600及以上，返回50元优惠
	public static final int PRIVILEGE_ALL = 4;
	//其它错误
	public static final int ERROR_CODE_OTHERS = 5;
	/**
	 * 判断cdkey是否存在，存在返回true，反之false
	 * 
	 * @param cdkey
	 * @return
	 */
	public boolean cdkeyExists(String cdkey);

	/**
	 * 根据cdkey查询权益兑换状态
	 * 
	 * @param cdkey
	 * @return
	 */
	public PrivilegeStatus getPrivilegeStatusByCdkey(String cdkey);

	/**
	 * @param cdkey
	 * @param privilegeStatus
	 */
	public void setPrivilegeStatus(String cdkey, PrivilegeStatus privilegeStatus);

	/**
	 * cdkey 不正确，返回-1 获得50元优惠，返回50 获得100元优惠，返回100 优惠已全部使用或消费金额不足，返回0
	 * 
	 * @param cdkey
	 * @param amount
	 * @return
	 */
	public int getResultStatusByCdkeyAmount(String cdkey, String amount);

	public QQActivityHistory getActivityHistoryByCdkeyAType(String cdkey,
			GiftType type);

	public void addQQActivityHistory(QQActivityHistory qqActivityHistory);

	public void updateQQActivityHistory(QQActivityHistory qqActivityHistory);

	public PrivilegeExchangeResponseMessage getExchangeResponseMessageByRequest(
			ServiceRequest request);
}
