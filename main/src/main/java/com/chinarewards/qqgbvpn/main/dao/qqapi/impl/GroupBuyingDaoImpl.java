package com.chinarewards.qqgbvpn.main.dao.qqapi.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.codehaus.jackson.map.ObjectMapper;

import com.chinarewards.qqgbvpn.domain.event.DomainEntity;
import com.chinarewards.qqgbvpn.domain.event.DomainEvent;
import com.chinarewards.qqgbvpn.domain.event.Journal;
import com.chinarewards.qqgbvpn.main.dao.qqapi.GroupBuyingDao;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class GroupBuyingDaoImpl implements GroupBuyingDao {

	private final Provider<EntityManager> em;

	@Inject
	public GroupBuyingDaoImpl(Provider<EntityManager> em) {
		this.em = em;
	}
	
	public void handleGroupBuyingSearch(Journal journal) throws Exception {
		em.get().getTransaction().begin();
		em.get().persist(journal);
		em.get().getTransaction().commit();
	}
	
	public void handleGroupBuyingValidate() throws Exception {
		if (em.get() != null) {
			System.out.println("注入成功");
		} else {
			System.out.println("注入不成功");
		}
		Journal j = new Journal();
		j.setTs(new Date());
		j.setEntity(DomainEntity.VALIDATION.toString());
		j.setEntityId("Validation_Id");
		j.setEvent(DomainEvent.POS_ORDER_VALIDATED_OK.toString());
		ObjectMapper mapper = new ObjectMapper();
		j.setEventDetail(mapper.writeValueAsString(j));
		em.get().getTransaction().begin();
		em.get().persist(j);
		em.get().getTransaction().commit();
	}
	
	public void handleGroupBuyingUnbind() throws Exception {
		
	}

}
