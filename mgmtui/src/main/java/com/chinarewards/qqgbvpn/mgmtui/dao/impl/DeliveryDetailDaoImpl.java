/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;

import com.chinarewards.qqgbvpn.domain.DeliveryNote;
import com.chinarewards.qqgbvpn.domain.DeliveryNoteDetail;
import com.chinarewards.qqgbvpn.mgmtui.dao.DeliveryDetailDao;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * DB operation for delivery note detail.
 * 
 * @author cream
 * @since 1.0.0 2011-09-05
 */
public class DeliveryDetailDaoImpl implements DeliveryDetailDao {

	@Inject
	Provider<EntityManager> emp;

	public EntityManager getEm() {
		return emp.get();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeliveryNoteDetail> fetchDeliveryNoteDetailByNOte(
			DeliveryNote note) {
		return getEm().createQuery("FROM DeliveryNoteDetail WHERE dn=:dn")
				.setParameter("dn", note).getResultList();
	}

}
