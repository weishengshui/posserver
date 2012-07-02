package com.chinarewards.qqadidas.mian.logic.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqadidas.domain.QQWeixinSignIn;
import com.chinarewards.qqadidas.mian.dao.WeixinSignInDao;
import com.chinarewards.qqadidas.mian.logic.WeixinSignInLogic;
import com.google.inject.Inject;

public class WeixinSignInLogicImpl implements WeixinSignInLogic {
	Logger log = LoggerFactory.getLogger(getClass());
	@Inject
	private WeixinSignInDao dao;

	@Override
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
