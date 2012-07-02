package com.chinarewards.qqadidas.mian.dao.impl;

import javax.persistence.EntityManager;

import com.chinarewards.qqadidas.domain.QQWeixinSignIn;
import com.chinarewards.qqadidas.mian.dao.WeixinSignInDao;
import com.google.inject.Inject;

public class WeixinSignInDaoImpl implements WeixinSignInDao {

	@Inject
	private EntityManager em;
	
	@Override
	public boolean addQQWeinxinSignIn(QQWeixinSignIn qqWeixinSignIn) {
		try {
			em.persist(qqWeixinSignIn);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
