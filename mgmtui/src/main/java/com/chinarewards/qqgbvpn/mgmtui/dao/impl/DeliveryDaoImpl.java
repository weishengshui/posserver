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
import com.chinarewards.qqgbvpn.domain.PageInfo;
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
	public PageInfo<DeliveryNoteVO> fetchDeliverys(DeliverySearchVO searchVO) {
		Map<String, Object> paramMap = new HashMap<String, Object>();

		StringBuffer searchSql = new StringBuffer();
		StringBuffer countSql = new StringBuffer();
		
		searchSql.append("SELECT dn FROM DeliveryNote dn WHERE 1=1");
		countSql.append("SELECT COUNT(dn.id) FROM DeliveryNote dn WHERE 1=1");

		if (!Tools.isEmptyString(searchVO.getAgentName())) {
			searchSql.append(" AND UPPER(dn.agentName) LIKE :agentName");
			countSql.append(" AND UPPER(dn.agentName) LIKE :agentName");
			paramMap.put("agentName", "%" + searchVO.getAgentName().toUpperCase()
					+ "%");
		}
		if (!Tools.isEmptyString(searchVO.getAgentId())) {
			searchSql.append(" AND dn.agent.id=:agentId");
			countSql.append(" AND dn.agent.id=:agentId");
			paramMap.put("agentId", searchVO.getAgentId());
		}
		if (searchVO.getCreateDateFrom() != null) {
			Calendar firstDate = getFirstDate(searchVO.getCreateDateFrom());
			searchSql.append(" AND dn.createDate > :createDateFrom");
			countSql.append(" AND dn.createDate > :createDateFrom");
			paramMap.put("createDateFrom", firstDate);
		}
		if (searchVO.getCreateDateTo() != null) {
			Calendar lastDate = getLastDate(searchVO.getCreateDateTo());
			searchSql.append(" AND dn.createDate < :createDateTo");
			countSql.append(" AND dn.createDate < :createDateTo");
			paramMap.put("createDateTo", lastDate);
		}

		if (!Tools.isEmptyString(searchVO.getDnNumber())) {
			searchSql.append(" AND UPPER(dn.dnNumber) LIKE :dnNumber");
			countSql.append(" AND UPPER(dn.dnNumber) LIKE :dnNumber");
			paramMap.put("dnNumber", "%" + searchVO.getDnNumber().toUpperCase()
					+ "%");
		}
		if (!Tools.isEmptyString(searchVO.getStatus())) {
			DeliveryNoteStatus status = DeliveryNoteStatus.valueOf(searchVO
					.getStatus());
			searchSql.append(" AND dn.status=:status");
			countSql.append(" AND dn.status=:status");
			paramMap.put("status", status);
		}

		// order by create date
		searchSql.append(" ORDER BY dn.createDate DESC");
		
		PageInfo pageInfo = this.findPageInfo(countSql.toString(), searchSql.toString(), 
				paramMap, searchVO.getPage(), searchVO.getSize());
		
		
		List<DeliveryNote> list = pageInfo.getItems();
		List<DeliveryNoteVO> voList = deliveryNoteAdapter.get().convertToVO(list);
		
		pageInfo.setItems(voList);
		
		
		return pageInfo;
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
