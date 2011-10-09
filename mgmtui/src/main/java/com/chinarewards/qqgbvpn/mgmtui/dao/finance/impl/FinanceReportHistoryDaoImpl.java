package com.chinarewards.qqgbvpn.mgmtui.dao.finance.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import com.chinarewards.qqgbvpn.core.BaseDao;
import com.chinarewards.qqgbvpn.domain.FinanceReportHistory;
import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.domain.event.DomainEntity;
import com.chinarewards.qqgbvpn.domain.event.DomainEvent;
import com.chinarewards.qqgbvpn.domain.event.Journal;
import com.chinarewards.qqgbvpn.domain.status.FinanceReportHistoryStatus;
import com.chinarewards.qqgbvpn.mgmtui.dao.finance.FinanceReportHistoryDao;
import com.chinarewards.qqgbvpn.mgmtui.vo.FinanceReportSearchVO;
import com.chinarewards.utils.StringUtil;

public class FinanceReportHistoryDaoImpl extends BaseDao implements FinanceReportHistoryDao  {

	@Override
	public PageInfo<FinanceReportHistory> searchFinanceReportHistory(
			FinanceReportSearchVO searchVO, PageInfo pageInfo) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer("FROM FinanceReportHistory f where 1=1 ");
		StringBuffer countHql = new StringBuffer("select count(f.id) FROM FinanceReportHistory f where 1=1 ");
		if (searchVO != null) {
			if (!StringUtil.isEmptyString(searchVO.getAgentId())) {
				hql.append(" and f.agentId = :agentId");
				countHql.append(" and f.agentId = :agentId");
				paramMap.put("agentId", searchVO.getAgentId());
			}
			if (searchVO.getStartDate() != null) {
				hql.append( " and f.createDate >= :startDate");
				countHql.append( " and f.createDate >= :startDate");
				paramMap.put("startDate", searchVO.getStartDate());
			}
			if (searchVO.getEndDate() != null) {
				hql.append(" and f.createDate <= :endDate");
				countHql.append(" and f.createDate <= :endDate");
				paramMap.put("endDate", searchVO.getEndDate());
			}
		}
		hql.append(" order by f.createDate desc");
		pageInfo = this.findPageInfo(countHql.toString(), hql.toString(), paramMap, pageInfo);
		return pageInfo;
	}

	@Override
	public FinanceReportHistory createFinanceReportHistory(
			FinanceReportSearchVO searchVO) {
		FinanceReportHistory f = new FinanceReportHistory();
		f.setAgentId(searchVO.getAgentId());
		f.setAgentName(searchVO.getAgentName());
		f.setStartDate(searchVO.getStartDate());
		if(searchVO.getEndDate() == null){
			f.setEndDate(new Date());
		}else{
			f.setEndDate(searchVO.getEndDate());
		}
		f.setCreateDate(new Date());
		f.setStatus(FinanceReportHistoryStatus.CREATING);
		getEm().persist(f);
		
		return f;
	}

	@Override
	public FinanceReportHistory getFinanceReportHistoryById(String id) {
		return getEm().find(FinanceReportHistory.class, id);
	}

	@Override
	public FinanceReportHistory saveFinanceReportHistory(
			FinanceReportHistory financeReportHistory) {
		getEm().persist(financeReportHistory);
		ObjectMapper mapper = new ObjectMapper();
		String eventDetail = null;
		try {
			eventDetail = mapper.writeValueAsString(financeReportHistory);
		} catch (Exception e) {
			e.printStackTrace();
			eventDetail = e.getMessage();
		}
		Journal journal = new Journal();
		journal.setTs(new Date());
		journal.setEntity(DomainEntity.FINANCE_REPORT_HISTORY.toString());
		journal.setEntityId(financeReportHistory.getId());
		journal.setEvent(DomainEvent.FINANCE_REPORT_HISTORY_MODIFY.toString());
		journal.setEventDetail(eventDetail);
		getEm().persist(journal);
		return financeReportHistory;
	}

}
