package com.chinarewards.qqgbvpn.main.dao.qqapi.impl;

import com.chinarewards.qq.meishi.domain.QQMeishiXaction;
import com.chinarewards.qqgbvpn.core.BaseDao;
import com.chinarewards.qqgbvpn.main.dao.qqapi.QQMeishiDao;

public class QQMeishiDaoImpl extends BaseDao implements QQMeishiDao {

	@Override
	public void saveQQMeishiXaction(QQMeishiXaction qqmeishiXaction) {
		getEm().persist(qqmeishiXaction);
	}


	
}
