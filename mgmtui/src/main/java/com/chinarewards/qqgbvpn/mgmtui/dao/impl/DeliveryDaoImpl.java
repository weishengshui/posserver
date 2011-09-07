/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.chinarewards.qqgbvpn.core.BaseDao;
import com.chinarewards.qqgbvpn.domain.DeliveryNote;
import com.chinarewards.qqgbvpn.mgmtui.adapter.delivery.DeliveryNoteAdapter;
import com.chinarewards.qqgbvpn.mgmtui.dao.DeliveryDao;
import com.chinarewards.qqgbvpn.mgmtui.model.delivery.DeliveryNoteVO;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author cream
 * 
 */
public class DeliveryDaoImpl extends BaseDao implements DeliveryDao {

	@Inject
	Provider<DeliveryNoteAdapter> deliveryNoteAdapter;

	@SuppressWarnings("unchecked")
	@Override
	public List<DeliveryNoteVO> fetchAllDelivery() {
		return deliveryNoteAdapter.get().convertToVO(
				getEm().createQuery("FROM DeliveryNote").getResultList());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeliveryNoteVO> fetchDeliveryList(int start, int limit) {
		Query query = getEm().createQuery("FROM DeliveryNote");
		query.setFirstResult(start);
		query.setMaxResults(limit);

		return deliveryNoteAdapter.get().convertToVO(query.getResultList());
	}

	@Override
	public long countDelivertList() {
		return (Long) getEm().createQuery(
				"SELECT COUNT(dn.id) FROM DeliveryNote dn").getSingleResult();
	}

	@Override
	public DeliveryNoteVO fetchDeliveryById(String id) {
		return deliveryNoteAdapter.get().convertToVO(
				getEm().find(DeliveryNote.class, id));
	}

	@Override
	public DeliveryNoteVO save(DeliveryNoteVO vo) {
		DeliveryNote dn = deliveryNoteAdapter.get().convertToEntity(vo);
		getEm().persist(dn);
		return deliveryNoteAdapter.get().convertToVO(dn);
	}

}
