package com.chinarewards.qqgbvpn.mgmtui.dao.finance.impl;

import java.util.HashMap;
import java.util.Map;

import com.chinarewards.qqgbvpn.core.BaseDao;
import com.chinarewards.qqgbvpn.domain.Agent;
import com.chinarewards.qqgbvpn.domain.FinanceReportHistory;
import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.domain.status.FinanceReportHistoryStatus;
import com.chinarewards.qqgbvpn.mgmtui.dao.finance.FinanceReportHistoryDao;
import com.chinarewards.qqgbvpn.mgmtui.util.Tools;
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
				paramMap.put("endDate", Tools.addDate(searchVO.getEndDate(), 1));
			}
			if (!StringUtil.isEmptyString(searchVO.getFinanceReportHistoryStatus())) {
				hql.append(" and f.status = :status");
				countHql.append(" and f.status = :status");
				paramMap.put("status", FinanceReportHistoryStatus.valueOf(searchVO.getFinanceReportHistoryStatus()));
			}
		}
		hql.append(" order by f.createDate desc");
		pageInfo = this.findPageInfo(countHql.toString(), hql.toString(), paramMap, pageInfo);
		return pageInfo;
	}


	@Override
	public FinanceReportHistory getFinanceReportHistoryById(String id) {
		return getEm().find(FinanceReportHistory.class, id);
	}

	@Override
	public FinanceReportHistory saveFinanceReportHistory(
			FinanceReportHistory financeReportHistory) {
		getEm().persist(financeReportHistory);
		return financeReportHistory;
	}


	@Override
	public Agent getAgentById(String agentId) {
		return getEm().find(Agent.class, agentId);
	}
	
	public FinanceReportHistory createFinanceReportHistory(FinanceReportHistory financeReportHistory) {
		getEm().persist(financeReportHistory);
		return financeReportHistory;
	}

}
