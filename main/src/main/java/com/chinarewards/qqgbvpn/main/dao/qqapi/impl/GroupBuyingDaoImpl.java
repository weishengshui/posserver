package com.chinarewards.qqgbvpn.main.dao.qqapi.impl;

import java.util.Date;

import javax.persistence.EntityManager;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.domain.event.DomainEntity;
import com.chinarewards.qqgbvpn.domain.event.DomainEvent;
import com.chinarewards.qqgbvpn.domain.event.Journal;
import com.chinarewards.qqgbvpn.main.dao.qqapi.GroupBuyingDao;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class GroupBuyingDaoImpl implements GroupBuyingDao {

	Logger log = LoggerFactory.getLogger(getClass());
	
	@Inject
	private Provider<EntityManager> em;

	public void handleGroupBuyingSearch(Journal journal) throws Exception {
		try {
			em.get().getTransaction().begin();
			saveJournal(journal);
			em.get().getTransaction().commit();
		} catch (Exception e) {
			log.error("group buying search save journal error");
			log.error("ts: " + journal.getTs());
			log.error("entity: " + journal.getEntity());
			log.error("entityId: " + journal.getEntityId());
			log.error("event: " + journal.getEvent());
			log.error("eventDetail: " + journal.getEventDetail());
		}
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
	
	private void saveJournal(Journal journal) {
		em.get().persist(journal);
	}

}
