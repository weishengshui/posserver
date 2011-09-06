/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;

import com.chinarewards.qqgbvpn.domain.DeliveryNote;
import com.chinarewards.qqgbvpn.domain.DeliveryNoteDetail;
import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.mgmtui.adapter.delivery.DeliveryNoteDetailAdapter;
import com.chinarewards.qqgbvpn.mgmtui.adapter.pos.PosAdapter;
import com.chinarewards.qqgbvpn.mgmtui.dao.DeliveryDetailDao;
import com.chinarewards.qqgbvpn.mgmtui.model.delivery.DeliveryNoteDetailVO;
import com.chinarewards.qqgbvpn.mgmtui.model.pos.PosVO;
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

	@Inject
	Provider<DeliveryNoteDetailAdapter> deliveryNoteDetailAdapter;

	@Inject
	Provider<PosAdapter> posAdapter;

	public EntityManager getEm() {
		return emp.get();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeliveryNoteDetailVO> fetchDetailListByNoteId(String noteId) {
		return deliveryNoteDetailAdapter.get().convertToVO(
				getEm().createQuery(
						"FROM DeliveryNoteDetail dnd WHERE dnd.dn.id=:dnId")
						.setParameter("dnId", noteId).getResultList());
	}

	@Override
	public PosVO fetchPosByDetailId(String detailId) {
		Pos pos = (Pos) getEm()
				.createQuery(
						"SELECT p FROM DeliveryNoteDetail dnd, Pos p WHERE dnd.posId = p.posId AND dnd.id=:dndId")
				.setParameter("dndId", detailId).getSingleResult();
		return posAdapter.get().convertToPosVO(pos);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PosVO> fetchPosByDetailIds(List<String> detailIds) {
		StringBuffer hql = new StringBuffer();
		hql.append("SELECT p ");
		hql.append(" FROM DeliveryNoteDetail dnd, Pos p ");
		hql.append(" WHERE dnd.posId = p.posId ");
		hql.append(" AND dnd.id IN (:detailIds)");
		List<Pos> resultList = getEm().createQuery(hql.toString())
				.setParameter("detailIds", detailIds).getResultList();
		return posAdapter.get().convertToPosVO(resultList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PosVO> fetchPosByNoteId(String noteId) {
		StringBuffer hql = new StringBuffer();
		hql.append("SELECT p ");
		hql.append(" FROM DeliveryNoteDetail dnd, Pos p ");
		hql.append(" WHERE dnd.posId = p.posId ");
		hql.append(" AND dnd.dn.id = :dnId");
		List<Pos> resultList = getEm().createQuery(hql.toString())
				.setParameter("dnId", noteId).getResultList();
		return posAdapter.get().convertToPosVO(resultList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public DeliveryNoteDetailVO fetchByPosId(String posId) {
		List<DeliveryNoteDetail> dnList = getEm()
				.createQuery(
						"FROM DeliveryNoteDetail dnd WHERE dnd.posId=:posId")
				.setParameter("posId", posId).getResultList();

		if (dnList == null || dnList.isEmpty()) {
			return null;
		}

		return deliveryNoteDetailAdapter.get().convertToVO(dnList.get(0));
	}

	@Override
	public DeliveryNoteDetailVO create(String noteId, String posId) {
		DeliveryNote dn = getEm().find(DeliveryNote.class, noteId);
		Pos p = (Pos) getEm().createQuery("FROM Pos WHERE posId=:posId")
				.setParameter("posId", posId).getSingleResult();

		DeliveryNoteDetail detail = new DeliveryNoteDetail();

		detail.setDn(dn);
		detail.setPosId(posId);
		detail.setModel(p.getModel());
		detail.setSimPhoneNo(p.getSimPhoneNo());
		detail.setSn(p.getSn());
		getEm().persist(detail);

		return deliveryNoteDetailAdapter.get().convertToVO(detail);
	}

	@Override
	public void delete(String detailId) {
		DeliveryNoteDetail dnd = getEm().find(DeliveryNoteDetail.class,
				detailId);
		if (dnd != null) {
			getEm().remove(dnd);
		}
	}
}
