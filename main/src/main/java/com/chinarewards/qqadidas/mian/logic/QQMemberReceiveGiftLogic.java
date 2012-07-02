package com.chinarewards.qqadidas.mian.logic;

import com.chinarewards.qqadidas.domain.QQActivityHistory;
import com.chinarewards.qqadidas.domain.QQActivityMember;


public interface QQMemberReceiveGiftLogic {
	

	
	/**
	 * 判断cdkey是否存在，存在返回QQActivityMember实例，反之null
	 * @param cdkey
	 * @return
	 */
	public QQActivityMember getQQActivityMemberByCdkey(String cdkey);
	
	/**
	 * 根据cdkey查询礼品领取状态，未领取返回true，反之false
	 * @param cdkey
	 * @return
	 */
	public boolean giftStatus(String cdkey);
	
	/**
	 * 更新QQActivityMember的礼品领取状态
	 * @param cdkey
	 * @param giftStatus
	 */
	public boolean updateQQActivityMember(QQActivityMember qqActivityMember);
	
	public boolean addQQActivityHistory(QQActivityHistory qah);
}
