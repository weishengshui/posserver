package com.chinarewards.qqadidas.main.logic;

import java.util.HashMap;

import com.chinarewards.qqadidas.domain.QQActivityHistory;
import com.chinarewards.qqadidas.domain.QQActivityMember;
import com.chinarewards.qqadidas.domain.QQWeixinSignIn;
import com.chinarewards.qqadidas.domain.status.GiftType;
import com.chinarewards.qqadidas.domain.status.PrivilegeStatus;
import com.chinarewards.qqadidas.main.protocol.cmd.PrivilegeExchangeResponseMessage;
import com.chinarewards.qqadidas.main.protocol.cmd.ReceiveGiftResponseMessage;
import com.chinarewards.qqadidas.main.protocol.cmd.WeixinSignInResponseMessage;

public interface QQAdidasManager {
	
	public ReceiveGiftResponseMessage receiveGiftCommand(HashMap<String, Object> params);
	
	public PrivilegeExchangeResponseMessage privilegeExchangeCommand(HashMap<String, Object> params);
	
	public WeixinSignInResponseMessage weixinSignInCommand(HashMap<String, Object> params);
	
	/**
	 * 判断cdkey是否存在，存在返回QQActivityMember实例，反之null
	 * 
	 * @param cdkey
	 * @return
	 */
	public QQActivityMember getQQActivityMemberByCdkey(String cdkey);

	/**
	 * 根据cdkey查询礼品领取状态，未领取返回true，反之false
	 * 
	 * @param cdkey
	 * @return
	 */
	public boolean giftStatus(String cdkey);

	/**
	 * 更新QQActivityMember的礼品领取状态
	 * 
	 * @param cdkey
	 * @param giftStatus
	 */
	public boolean updateQQActivityMember(QQActivityMember qqActivityMember);

	public boolean addQQActivityHistory(QQActivityHistory qah);
	
	

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

		public boolean isSignInSuccess(QQWeixinSignIn qqWeixinSignIn);
}
