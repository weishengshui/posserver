package com.chinarewards.qqadidas.mian.logic.impl;

import com.chinarewards.qqadidas.domain.QQActivityHistory;
import com.chinarewards.qqadidas.domain.QQActivityMember;
import com.chinarewards.qqadidas.mian.dao.QQMemberReceiveGiftDao;
import com.chinarewards.qqadidas.mian.logic.QQMemberReceiveGiftLogic;
import com.google.inject.Inject;
/**
 * 
 * @author weishengshui
 *
 */
public class QQMemberReceiveGiftImpl implements QQMemberReceiveGiftLogic {
	@Inject
	QQMemberReceiveGiftDao dao;


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
}
