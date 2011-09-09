/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.dao.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.core.BaseDao;
import com.chinarewards.qqgbvpn.domain.DeliveryNote;
import com.chinarewards.qqgbvpn.domain.event.DomainEntity;
import com.chinarewards.qqgbvpn.domain.event.DomainEvent;
import com.chinarewards.qqgbvpn.domain.status.DeliveryNoteStatus;
import com.chinarewards.qqgbvpn.logic.journal.JournalLogic;
import com.chinarewards.qqgbvpn.mgmtui.adapter.delivery.DeliveryNoteAdapter;
import com.chinarewards.qqgbvpn.mgmtui.dao.DeliveryDao;
import com.chinarewards.qqgbvpn.mgmtui.model.delivery.DeliveryNoteVO;
import com.chinarewards.qqgbvpn.mgmtui.model.delivery.DeliverySearchVO;
import com.chinarewards.qqgbvpn.mgmtui.util.Tools;
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

	private final static String SEARCH = "search";
	private final static String COUNT = "count";

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

	@Override
	@SuppressWarnings("unchecked")
	public List<DeliveryNoteVO> fetchDeliverys(DeliverySearchVO criteria) {
		Query query = buildQuery(SEARCH, criteria);
		List<DeliveryNote> list = query.getResultList();
		return deliveryNoteAdapter.get().convertToVO(list);
	}

	@Override
	public int countDeliverys(DeliverySearchVO criteria) {
		Query query = buildQuery(COUNT, criteria);
		return (Integer) query.getSingleResult();
	}

	private Query buildQuery(String type, DeliverySearchVO criteria) {
		Map<String, Object> param = new HashMap<String, Object>();

		StringBuffer hql = new StringBuffer();
		if (SEARCH.equals(type)) {
			hql.append("SELECT * FROM DeliveryNote dn WHERE 1=1");
		} else if (COUNT.equals(type)) {
			hql.append("SELECT COUNT(dn.id) FROM DeliveryNote dn WHERE 1=1");
		}

		if (Tools.isEmptyString(criteria.getAgentName())) {
			hql.append(" AND UPPER(dn.agentName) LIKE :agentName");
			param.put("agentName", "%" + criteria.getAgentName().toUpperCase()
					+ "%");
		}
		if (Tools.isEmptyString(criteria.getAgentId())) {
			hql.append(" AND dn.agent.id=:agentId");
			param.put("agentId", criteria.getAgentId());
		}
		if (criteria.getCreateDateFrom() != null) {
			Calendar firstDate = getFirstDate(criteria.getCreateDateFrom());
			hql.append(" AND dn.createDate > :createDateFrom");
			param.put("createDateFrom", firstDate);
		}
		if (criteria.getCreateDateTo() != null) {
			Calendar lastDate = getLastDate(criteria.getCreateDateTo());
			hql.append(" AND dn.createDate < :createDateTo");
			param.put("createDateTo", lastDate);
		}

		if (Tools.isEmptyString(criteria.getDnNumber())) {
			hql.append(" AND UPPER(dn.dnNumber) LIKE :dnNumber");
			param.put("dnNumber", "%" + criteria.getDnNumber().toUpperCase()
					+ "%");
		}
		if (Tools.isEmptyString(criteria.getStatus())) {
			DeliveryNoteStatus status = DeliveryNoteStatus.valueOf(criteria
					.getStatus());
			hql.append(" AND dn.status=:status");
			param.put("status", status);
		}

		// build query
		Query query = getEm().createQuery(hql.toString());
		if (!param.isEmpty()) {
			for (String key : param.keySet()) {
				query.setParameter(key, param.get(key));
			}
		}

		if (SEARCH.equals(type)) {
			if (criteria.getPagination() != null) {
				int start = criteria.getPagination().getStartIndex();
				int limit = criteria.getPagination().getCountOnEachPage();
				log.debug("pagination: start - {}, limit - {}", new Object[] {
						start, limit });
				query.setFirstResult(start);
				query.setMaxResults(limit);
			}
		}
		return query;
	}

	/**
	 * @param createDateFrom
	 * @return
	 */
	private Calendar getFirstDate(Date createDateFrom) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(createDateFrom);

		Calendar firstDate = Calendar.getInstance();
		firstDate.set(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
		return firstDate;
	}

	private Calendar getLastDate(Date createDateFrom) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(createDateFrom);

		Calendar lastDate = Calendar.getInstance();
		lastDate.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DATE) + 1);
		lastDate.add(Calendar.MILLISECOND, -1);
		return lastDate;
	}
}
