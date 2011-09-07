/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.core.BaseDao;
import com.chinarewards.qqgbvpn.domain.DeliveryNote;
import com.chinarewards.qqgbvpn.domain.event.DomainEntity;
import com.chinarewards.qqgbvpn.domain.event.DomainEvent;
import com.chinarewards.qqgbvpn.logic.journal.JournalLogic;
import com.chinarewards.qqgbvpn.mgmtui.adapter.delivery.DeliveryNoteAdapter;
import com.chinarewards.qqgbvpn.mgmtui.dao.DeliveryDao;
import com.chinarewards.qqgbvpn.mgmtui.model.delivery.DeliveryNoteVO;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author cream
 * 
 */
/**
 * @author cream
 * 
 */
public class DeliveryDaoImpl extends BaseDao implements DeliveryDao {

	@Inject
	Provider<DeliveryNoteAdapter> deliveryNoteAdapter;

	@Inject
	Provider<JournalLogic> journalLogic;

	Logger log = LoggerFactory.getLogger(getClass());

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
	public DeliveryNoteVO create(DeliveryNoteVO vo) {
		DeliveryNote dn = deliveryNoteAdapter.get().convertToEntity(vo);
		getEm().persist(dn);
		return deliveryNoteAdapter.get().convertToVO(dn);
	}

	@Override
	public DeliveryNoteVO merge(DeliveryNoteVO vo) {
		DeliveryNote dn = deliveryNoteAdapter.get().convertToEntity(vo);
		getEm().merge(dn);
		return deliveryNoteAdapter.get().convertToVO(dn);
	}

	@Override
	public void deleteById(String id) {
		DeliveryNote dn = getEm().find(DeliveryNote.class, id);
		if (dn == null) {
			throw new IllegalArgumentException("DeliveryNote(id=" + id
					+ ") not found!");
		}

		// add journalLogic
		try {
			ObjectMapper map = new ObjectMapper();
			String eventDetail;
			eventDetail = map.writeValueAsString(dn);
			journalLogic.get().logEvent(
					DomainEvent.USER_REMOVED_DNOTE.toString(),
					DomainEntity.DELIVERY_NOTE.toString(), id, eventDetail);
		} catch (Exception e) {
			log.error("Error in parse to JSON", e);
		}

		int delNum = getEm()
				.createQuery(
						"DELETE DeliveryNoteDetail dnd WHERE dnd.dn.id=:dnId")
				.setParameter("dnId", id).executeUpdate();
		log.debug("{} details had been deleted. note ID={}", new Object[] {
				delNum, id });
		getEm().remove(dn);
	}
}
