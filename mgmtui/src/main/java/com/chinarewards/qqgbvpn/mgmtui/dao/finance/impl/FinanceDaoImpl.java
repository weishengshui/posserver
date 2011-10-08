package com.chinarewards.qqgbvpn.mgmtui.dao.finance.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Query;

import com.chinarewards.qqgbvpn.core.BaseDao;
import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.domain.status.ValidationStatus;
import com.chinarewards.qqgbvpn.mgmtui.dao.finance.FinanceDao;
import com.chinarewards.qqgbvpn.mgmtui.vo.FinanceReportSearchVO;
import com.chinarewards.qqgbvpn.mgmtui.vo.FinanceReportVO;
import com.chinarewards.utils.StringUtil;

public class FinanceDaoImpl extends BaseDao implements FinanceDao {
	
	public List<FinanceReportVO> searchFinanceReport(FinanceReportSearchVO searchVO) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String hql = "select new com.chinarewards.qqgbvpn.mgmtui.vo.FinanceReportVO(" +
				"DATE_FORMAT(v.ts, '%Y-%m'),v.agentName,v.posId," +
				FinanceReportVO.BASE_AMOUNT + " AS baseAmount" +
				",COUNT(v.id) AS actuallyValCount" +
				", (CASE WHEN (COUNT(v.id) - " + FinanceReportVO.BASE_AMOUNT + ") > 0 " +
						"THEN (COUNT(v.id) - " + FinanceReportVO.BASE_AMOUNT + ") ELSE 0 END) AS beyondValCount" +
				", " + FinanceReportVO.UNIT_PRICE + " AS unitPrice" +
				", (CASE WHEN (COUNT(v.id) - " + FinanceReportVO.BASE_AMOUNT + ") > 0 " +
						"THEN (COUNT(v.id) - " + FinanceReportVO.BASE_AMOUNT + ") ELSE 0 END) " +
								"* " + FinanceReportVO.UNIT_PRICE + " AS beyondAmount" +
				", (" + FinanceReportVO.BASE_AMOUNT +
						" + ((CASE WHEN (COUNT(v.id) - " + FinanceReportVO.BASE_AMOUNT + ") > 0 " +
								"THEN (COUNT(v.id) - " + FinanceReportVO.BASE_AMOUNT + ") ELSE 0 END) " +
										"* " + FinanceReportVO.UNIT_PRICE + ")) AS amount" +
				") " +
				" from Validation v " +
				" where v.status = :status ";
		paramMap.put("status", ValidationStatus.SUCCESS);
		if (searchVO != null) {
			if (!StringUtil.isEmptyString(searchVO.getAgentId())) {
				hql += " and v.agentId = :agentId";
				paramMap.put("agentId", searchVO.getAgentId());
			}
			if (searchVO.getStartDate() != null) {
				hql += " and v.ts >= :startDate";
				paramMap.put("startDate", searchVO.getStartDate());
			}
			if (searchVO.getEndDate() != null) {
				hql += " and v.ts <= :endDate";
				paramMap.put("endDate", searchVO.getEndDate());
			}
		}
		hql += " group by DATE_FORMAT(v.ts, '%Y-%m'),v.agentId,v.posId";
		Query jql = getEm().createQuery(hql);
		for (Entry<String, Object> entry : paramMap.entrySet()) {
			jql.setParameter(entry.getKey(), entry.getValue());
		}
		return jql.getResultList();
	}
	
	public PageInfo<FinanceReportVO> searchFinanceReport(FinanceReportSearchVO searchVO, PageInfo pageInfo) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Map<String, Object> countParamMap = new HashMap<String, Object>();
		String searchSql = "select new com.chinarewards.qqgbvpn.mgmtui.vo.FinanceReportVO(" +
				"DATE_FORMAT(v.ts, '%Y-%m'),v.agentName,v.posId," +
				FinanceReportVO.BASE_AMOUNT + " AS baseAmount" +
				",COUNT(v.id) AS actuallyValCount" +
				", (CASE WHEN (COUNT(v.id) - " + FinanceReportVO.BASE_AMOUNT + ") > 0 " +
						"THEN (COUNT(v.id) - " + FinanceReportVO.BASE_AMOUNT + ") ELSE 0 END) AS beyondValCount" +
				", " + FinanceReportVO.UNIT_PRICE + " AS unitPrice" +
				", (CASE WHEN (COUNT(v.id) - " + FinanceReportVO.BASE_AMOUNT + ") > 0 " +
						"THEN (COUNT(v.id) - " + FinanceReportVO.BASE_AMOUNT + ") ELSE 0 END) " +
								"* " + FinanceReportVO.UNIT_PRICE + " AS beyondAmount" +
				", (" + FinanceReportVO.BASE_AMOUNT +
						" + ((CASE WHEN (COUNT(v.id) - " + FinanceReportVO.BASE_AMOUNT + ") > 0 " +
								"THEN (COUNT(v.id) - " + FinanceReportVO.BASE_AMOUNT + ") ELSE 0 END) " +
										"* " + FinanceReportVO.UNIT_PRICE + ")) AS amount" +
				") " +
				"from Validation v " +
				"where v.status = :status ";
		String countSql = "SELECT COUNT(1) FROM (SELECT 1 from Validation v where v.status = :status ";
		paramMap.put("status", ValidationStatus.SUCCESS);
		countParamMap.put("status", ValidationStatus.SUCCESS.toString());
		if (searchVO != null) {
			if (!StringUtil.isEmptyString(searchVO.getAgentId())) {
				searchSql += " and v.agentId = :agentId";
				countSql += " and v.agentId = :agentId";
				paramMap.put("agentId", searchVO.getAgentId());
				countParamMap.put("agentId", searchVO.getAgentId());
			}
			if (searchVO.getStartDate() != null) {
				searchSql += " and v.ts >= :startDate";
				countSql += " and v.ts >= :startDate";
				paramMap.put("startDate", searchVO.getStartDate());
				countParamMap.put("startDate", searchVO.getStartDate());
			}
			if (searchVO.getEndDate() != null) {
				searchSql += " and v.ts <= :endDate";
				countSql += " and v.ts <= :endDate";
				paramMap.put("endDate", searchVO.getEndDate());
				countParamMap.put("endDate", searchVO.getEndDate());
			}
		}
		searchSql += " group by DATE_FORMAT(v.ts, '%Y-%m'),v.agentId,v.posId ";
		searchSql += " order by DATE_FORMAT(v.ts, '%Y-%m'),v.agentId,v.posId ";
		countSql += " group by DATE_FORMAT(v.ts, '%Y-%m'),v.agentId,v.posId) a ";
		PageInfo<FinanceReportVO> result = this.findPageInfoByNativeQuery(countSql, searchSql, countParamMap, paramMap, pageInfo);
		return result;
	}
	
}
