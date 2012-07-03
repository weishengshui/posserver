package com.chinarewards.qqadidas.main.dao;

import com.chinarewards.qqadidas.domain.QQActivityHistory;
import com.chinarewards.qqadidas.domain.QQActivityMember;
import com.chinarewards.qqadidas.domain.QQWeixinSignIn;
import com.chinarewards.qqadidas.domain.status.GiftType;
import com.chinarewards.qqadidas.domain.status.PrivilegeStatus;

public interface QQAdidasManagerDao {

	public QQActivityMember getQQActivityMemberByCdkey(String cdkey);

	public boolean giftStatus(String cdkey);

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

	public QQActivityHistory getActivityHistoryByCdkeyAType(String cdkey,
			GiftType type);

	public void updateQQActivityHistory(QQActivityHistory qqActivityHistory);
	
	// 添加一条微信签到记录
		public boolean addQQWeinxinSignIn(QQWeixinSignIn qqWeixinSignIn);

}
