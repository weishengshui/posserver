/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.logic.pos.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.domain.DeliveryNote;
import com.chinarewards.qqgbvpn.domain.DeliveryNoteDetail;
import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.logic.journal.JournalLogic;
import com.chinarewards.qqgbvpn.mgmtui.dao.DeliveryDao;
import com.chinarewards.qqgbvpn.mgmtui.dao.DeliveryDetailDao;
import com.chinarewards.qqgbvpn.mgmtui.logic.pos.DeliveryLogic;
import com.chinarewards.qqgbvpn.mgmtui.model.agent.AgentVO;
import com.chinarewards.qqgbvpn.mgmtui.model.pos.PosVO;
import com.chinarewards.qqgbvpn.mgmtui.model.util.PaginationTools;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author cream
 * 
 */
public class DeliveryLogicImpl implements DeliveryLogic {

	Logger log = LoggerFactory.getLogger(DeliveryLogicImpl.class);

	@Inject
	Provider<DeliveryDao> deliveryDao;

	@Inject
	Provider<DeliveryDetailDao> deliveryDetailDao;

	@Inject
	Provider<JournalLogic> journalLogic;

	protected DeliveryDao getDeliveryDao() {
		return deliveryDao.get();
	}

	protected DeliveryDetailDao getDetailDao() {
		return deliveryDetailDao.get();
	}

	@Override
	public List<DeliveryNote> fetchAllDelivery() {
		return getDeliveryDao().fetchAllDelivery();
	}

	@Override
	public PageInfo<DeliveryNote> fetchDeliveryList(PaginationTools pagination) {
		log.debug(
				" Process in method fetchDeliveryList, start-{}, limit-{}",
				new Object[] { pagination.getStartIndex(),
						pagination.getCountOnEachPage() });

		long count = getDeliveryDao().countDelivertList();

		log.debug("result.count:{}", count);

		List<DeliveryNote> list = getDeliveryDao().fetchDeliveryList(
				pagination.getStartIndex(), pagination.getCountOnEachPage());

		log.debug("result.list.size:{}", list == null ? "null" : list.size());

		PageInfo<DeliveryNote> result = new PageInfo<DeliveryNote>();
		result.setItems(list);
		if (count > Integer.MAX_VALUE) {
			result.setRecordCount(Integer.MAX_VALUE);
		}
		result.setRecordCount((int) count);

		return result;
	}

	@Override
	public List<DeliveryNoteDetail> fetchDeliveryNoteDetailList(
			DeliveryNote note) {

		log.debug(" Process in method fetchDeliveryNoteDetailList, input:{}",
				note);
		List<DeliveryNoteDetail> resultList = getDetailDao()
				.fetchDeliveryNoteDetailByNOte(note);
		log.debug("result.size:{}",
				resultList == null ? "null" : resultList.size());
		return resultList;
	}

	@Override
	public List<PosVO> delivery(DeliveryNote note, AgentVO agent,
			List<PosVO> posList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void confirmDelivery(DeliveryNote note) {
		// TODO Auto-generated method stub

	}

	@Override
	public void printDelivery(DeliveryNote note) {
		// TODO Auto-generated method stub

	}

}
