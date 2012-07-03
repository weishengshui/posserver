package com.chinarewards.qqadidas.module;

import com.chinarewards.qqadidas.main.dao.QQAdidasManagerDao;
import com.chinarewards.qqadidas.main.dao.impl.QQAdidasManagerDaoImpl;
import com.chinarewards.qqadidas.main.logic.QQAdidasManager;
import com.chinarewards.qqadidas.main.logic.impl.QQAdidasManagerImpl;
import com.google.inject.AbstractModule;

public class QQAdidasModule extends AbstractModule {

	@Override
	protected void configure() {
		
		bind(QQAdidasManager.class).to(QQAdidasManagerImpl.class);
		bind(QQAdidasManagerDao.class).to(QQAdidasManagerDaoImpl.class);

	}

}
