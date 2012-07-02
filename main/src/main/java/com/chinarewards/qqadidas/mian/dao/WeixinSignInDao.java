package com.chinarewards.qqadidas.mian.dao;

import com.chinarewards.qqadidas.domain.QQWeixinSignIn;

public interface WeixinSignInDao {
	
	//添加一条微信签到记录
	public boolean addQQWeinxinSignIn(QQWeixinSignIn qqWeixinSignIn);
}
