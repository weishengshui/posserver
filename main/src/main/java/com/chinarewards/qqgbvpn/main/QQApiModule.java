package com.chinarewards.qqgbvpn.main;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import com.chinarewards.qqgbvpn.config.DatabaseProperties;
import com.chinarewards.qqgbvpn.config.URLProperties;
import com.chinarewards.qqgbvpn.main.dao.qqapi.GroupBuyingDao;
import com.chinarewards.qqgbvpn.main.dao.qqapi.impl.GroupBuyingDaoImpl;
import com.chinarewards.qqgbvpn.main.logic.qqapi.GroupBuyingManager;
import com.chinarewards.qqgbvpn.main.logic.qqapi.impl.GroupBuyingManagerImpl;
import com.chinarewards.qqgbvpn.qqapi.service.GroupBuyingService;
import com.chinarewards.qqgbvpn.qqapi.service.impl.GroupBuyingServiceImpl;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;

public class QQApiModule extends AbstractModule {

	@Override
	protected void configure() {
		
		//注入JPA
		//install(new JpaPersistModule("posnet").properties(new DatabaseProperties().getProperties())); 
		bind(EntityManager.class).toProvider(new Provider<EntityManager>() {
			@Override
			public EntityManager get() {
				return Persistence.createEntityManagerFactory("posnet",new DatabaseProperties().getProperties()).createEntityManager();
			}
		}).in(Singleton.class);
		
		bind(GroupBuyingService.class).toProvider(new Provider<GroupBuyingService>() {
			@Override
			public GroupBuyingService get() {
				try {
					return new GroupBuyingServiceImpl(new URLProperties().getProperties());
				} catch (Exception e) {
					return null;
				}
			}
		}).in(Singleton.class);
		
		bind(GroupBuyingManager.class).to(GroupBuyingManagerImpl.class).in(Singleton.class);
		
		bind(GroupBuyingDao.class).to(GroupBuyingDaoImpl.class);
		
	}

}
