package com.chinarewards.qqadidas.module;

import com.chinarewards.qqadidas.mian.dao.QQMemberReceiveGiftDao;
import com.chinarewards.qqadidas.mian.dao.QQMemberPrivilegeExchangeDao;
import com.chinarewards.qqadidas.mian.dao.WeixinSignInDao;
import com.chinarewards.qqadidas.mian.dao.impl.QQMemberReceiveGiftDaoImpl;
import com.chinarewards.qqadidas.mian.dao.impl.QQMemberPrivilegeExchangeDaoImpl;
import com.chinarewards.qqadidas.mian.dao.impl.WeixinSignInDaoImpl;
import com.chinarewards.qqadidas.mian.logic.QQMemberReceiveGiftLogic;
import com.chinarewards.qqadidas.mian.logic.QQMemberPrivilegeExchangeLogic;
import com.chinarewards.qqadidas.mian.logic.WeixinSignInLogic;
import com.chinarewards.qqadidas.mian.logic.impl.QQMemberReceiveGiftImpl;
import com.chinarewards.qqadidas.mian.logic.impl.QQMemberPrivilegeExchangeLogicImpl;
import com.chinarewards.qqadidas.mian.logic.impl.WeixinSignInLogicImpl;
import com.google.inject.AbstractModule;

public class QQAdidasModule extends AbstractModule {

	@Override
	protected void configure() {
		//QQ会员领取礼品
		bind(QQMemberReceiveGiftDao.class).to(QQMemberReceiveGiftDaoImpl.class);
		bind(QQMemberReceiveGiftLogic.class).to(QQMemberReceiveGiftImpl.class);
		//QQ会员权益兑换
		bind(QQMemberPrivilegeExchangeDao.class).to(QQMemberPrivilegeExchangeDaoImpl.class);
		bind(QQMemberPrivilegeExchangeLogic.class).to(QQMemberPrivilegeExchangeLogicImpl.class);
		//微信用户签到
		bind(WeixinSignInDao.class).to(WeixinSignInDaoImpl.class);
		bind(WeixinSignInLogic.class).to(WeixinSignInLogicImpl.class);
		
	}

}
