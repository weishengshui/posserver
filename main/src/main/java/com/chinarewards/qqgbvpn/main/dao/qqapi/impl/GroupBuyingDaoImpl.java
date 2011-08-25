package com.chinarewards.qqgbvpn.main.dao.qqapi.impl;

import javax.persistence.EntityManager;

import com.chinarewards.qqgbvpn.main.dao.qqapi.GroupBuyingDao;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class GroupBuyingDaoImpl implements GroupBuyingDao {

	private final Provider<EntityManager> em;

	@Inject
	public GroupBuyingDaoImpl(Provider<EntityManager> em) {
		this.em = em;
	}
	
	public void handleGroupBuyingSearch() throws Exception {
		if (em.get() != null) {
			System.out.println("注入成功");
		} else {
			System.out.println("注入不成功");
		}
	}
	
	public void handleGroupBuyingValidate() throws Exception {
		
	}
	
	public void handleGroupBuyingUnbind() throws Exception {
		
	}

}
