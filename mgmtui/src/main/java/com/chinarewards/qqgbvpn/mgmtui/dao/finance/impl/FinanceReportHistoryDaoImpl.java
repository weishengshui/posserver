package com.chinarewards.qqgbvpn.mgmtui.dao.finance.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.chinarewards.qqgbvpn.core.BaseDao;
import com.chinarewards.qqgbvpn.domain.FinanceReportHistory;
import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.domain.status.FinanceReportHistoryStatus;
import com.chinarewards.qqgbvpn.mgmtui.dao.finance.FinanceReportHistoryDao;
import com.chinarewards.qqgbvpn.mgmtui.vo.FinanceReportSearchVO;

public class FinanceReportHistoryDaoImpl extends BaseDao implements FinanceReportHistoryDao  {

	@Override
	public PageInfo<FinanceReportHistory> searchFinanceReportHistory(
			FinanceReportSearchVO searchVO, PageInfo pageInfo) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer("FROM FinanceReportHistory f order by f.createDate desc");
		StringBuffer countHql = new StringBuffer("select count(f.id) FROM FinanceReportHistory f order by f.createDate desc");
//		if (searchVO != null) {
//			if (!StringUtil.isEmptyString(searchVO.getAgentId())) {
//				hql.append(" and v.agentId = :agentId");
//				paramMap.put("agentId", searchVO.getAgentId());
//			}
//			if (searchVO.getStartDate() != null) {
//				hql.append( " and v.ts >= :startDate");
//				paramMap.put("startDate", searchVO.getStartDate());
//			}
//			if (searchVO.getEndDate() != null) {
//				hql.append(" and v.ts <= :endDate");
//				paramMap.put("endDate", searchVO.getEndDate());
//			}
//		}
		pageInfo = this.findPageInfo(countHql.toString(), hql.toString(), paramMap, pageInfo);
		return pageInfo;
	}

	@Override
	public FinanceReportHistory createFinanceReportHistory(
			FinanceReportSearchVO searchVO) {
		FinanceReportHistory f = new FinanceReportHistory();
		f.setAgentId(searchVO.getAgentId());
		f.setStartDate(searchVO.getStartDate());
		f.setEndDate(searchVO.getEndDate());
		f.setCreateDate(new Date());
		f.setStatus(FinanceReportHistoryStatus.CREATING);
		getEm().persist(f);
		
		return f;
	}

}
