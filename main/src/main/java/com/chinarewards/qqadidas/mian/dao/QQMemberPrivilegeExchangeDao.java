package com.chinarewards.qqadidas.mian.dao;

import com.chinarewards.qqadidas.domain.QQActivityHistory;
import com.chinarewards.qqadidas.domain.status.GiftType;
import com.chinarewards.qqadidas.domain.status.PrivilegeStatus;

public interface QQMemberPrivilegeExchangeDao {
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

	public void addQQActivityHistory(QQActivityHistory qqActivityHistory);

	public void updateQQActivityHistory(QQActivityHistory qqActivityHistory);

}
