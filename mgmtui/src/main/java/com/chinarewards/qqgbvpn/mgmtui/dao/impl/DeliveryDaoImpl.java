/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.chinarewards.qqgbvpn.domain.DeliveryNote;
import com.chinarewards.qqgbvpn.mgmtui.dao.DeliveryDao;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author cream
 * 
 */
public class DeliveryDaoImpl implements DeliveryDao {

	@Inject
	Provider<EntityManager> emp;

	protected EntityManager getEm() {
		return emp.get();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeliveryNote> fetchAllDelivery() {
		return getEm().createQuery("FROM DeliveryNote").getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeliveryNote> fetchDeliveryList(int start, int limit) {
		Query query = getEm().createQuery("FROM DeliveryNote");
		query.setFirstResult(start);
		query.setMaxResults(limit);

		return query.getResultList();
	}

	@Override
	public long countDelivertList() {
		return (Long) getEm().createQuery(
				"SELECT COUNT(dn.id) FROM DeliveryNote dn").getSingleResult();
	}

}
