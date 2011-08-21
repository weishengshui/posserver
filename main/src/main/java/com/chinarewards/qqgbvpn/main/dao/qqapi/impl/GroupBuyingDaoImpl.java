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

}
