package com.chinarewards.qqadidas.mian.dao;

import com.chinarewards.qqadidas.domain.QQActivityHistory;
import com.chinarewards.qqadidas.domain.QQActivityMember;


public interface QQMemberReceiveGiftDao {
	
	public QQActivityMember getQQActivityMemberByCdkey(String cdkey);

	public boolean giftStatus(String cdkey);
	
	public boolean updateQQActivityMember(QQActivityMember qqActivityMember);
	

	public boolean addQQActivityHistory(QQActivityHistory qah);
	
	
	
}
