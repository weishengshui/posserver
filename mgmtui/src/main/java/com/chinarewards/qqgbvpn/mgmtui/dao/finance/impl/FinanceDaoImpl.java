package com.chinarewards.qqgbvpn.mgmtui.dao.finance.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Query;

import com.chinarewards.qqgbvpn.core.BaseDao;
import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.domain.status.PosDeliveryStatus;
import com.chinarewards.qqgbvpn.domain.status.ValidationStatus;
import com.chinarewards.qqgbvpn.mgmtui.dao.finance.FinanceDao;
import com.chinarewards.qqgbvpn.mgmtui.util.Tools;
import com.chinarewards.qqgbvpn.mgmtui.vo.FinanceReportSearchVO;
import com.chinarewards.qqgbvpn.mgmtui.vo.FinanceReportVO;
import com.chinarewards.utils.StringUtil;

public class FinanceDaoImpl extends BaseDao implements FinanceDao {
	
	public List<FinanceReportVO> searchFinanceReport(FinanceReportSearchVO searchVO) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer searchSql = new StringBuffer("select * from (");
		//交易成功的数据
		searchSql.append(" select DATE_FORMAT(v.ts, '%Y-%m') as reportMonth,v.agentName,v.posId,");
		searchSql.append(FinanceReportVO.BASE_AMOUNT + " AS baseAmount");
		searchSql.append(",COUNT(v.id) AS actuallyValCount");
		searchSql.append(", (CASE WHEN (COUNT(v.id) - " + FinanceReportVO.BASE_LINE + ") > 0 ");
		searchSql.append("THEN (COUNT(v.id) - " + FinanceReportVO.BASE_LINE + ") ELSE 0 END) AS beyondValCount");
		searchSql.append(", " + FinanceReportVO.UNIT_PRICE + " AS unitPrice");
		searchSql.append(", (CASE WHEN (COUNT(v.id) - " + FinanceReportVO.BASE_LINE + ") > 0 ");
		searchSql.append("THEN (COUNT(v.id) - " + FinanceReportVO.BASE_LINE + ") ELSE 0 END) ");
		searchSql.append("* " + FinanceReportVO.UNIT_PRICE + " AS beyondAmount");
		searchSql.append(", (" + FinanceReportVO.BASE_AMOUNT);
		searchSql.append(" + ((CASE WHEN (COUNT(v.id) - " + FinanceReportVO.BASE_LINE + ") > 0 ");
		searchSql.append("THEN (COUNT(v.id) - " + FinanceReportVO.BASE_LINE + ") ELSE 0 END) ");
		searchSql.append("* " + FinanceReportVO.UNIT_PRICE + ")) AS amount ");
		searchSql.append(" from Validation v ");
		searchSql.append(" where v.status = :status ");
		paramMap.put("status", ValidationStatus.SUCCESS.toString());
		if (searchVO != null) {
			if (!StringUtil.isEmptyString(searchVO.getAgentId())) {
				searchSql.append(" and v.agentId = :agentId");
				paramMap.put("agentId", searchVO.getAgentId());
			}
			if (searchVO.getStartDate() != null) {
				searchSql.append(" and v.ts >= :startDate");
				paramMap.put("startDate", searchVO.getStartDate());
			}
			if (searchVO.getEndDate() != null) {
				searchSql.append(" and v.ts <= :endDate");
				paramMap.put("endDate", Tools.addDate(searchVO.getEndDate(), 1));
			}
		}
		searchSql.append(" group by DATE_FORMAT(v.ts, '%Y-%m'),v.agentId,v.posId ");
		//加入保底的数据(union all)
		if (searchVO.getStartDate() != null && searchVO.getEndDate() != null) {
			//所查询的月份，通过全连接算出笛卡尔积
			String monthsSql = getMonthsSql(searchVO.getStartDate(),searchVO.getEndDate());
			StringBuffer searchBaseSql = new StringBuffer(" UNION ALL ");
			searchBaseSql.append(" SELECT b.g AS reportMonth, a.name AS agentName, p.posid,");
			searchBaseSql.append(FinanceReportVO.BASE_AMOUNT + " AS baseAmount,");
			searchBaseSql.append("0 AS actuallyValCount,0 AS beyondValCount, ");
			searchBaseSql.append(FinanceReportVO.UNIT_PRICE + " AS unitPrice, 0 AS beyondAmount, ");
			searchBaseSql.append(FinanceReportVO.BASE_AMOUNT + " AS amount ");
			searchBaseSql.append(" from Pos p, PosAssignment pa, Agent a ");
			
			
			searchBaseSql.append(", (" + monthsSql + ") b ");
			
			searchBaseSql.append(" WHERE pa.pos_id = p.id AND pa.agent_id = a.id ");
			searchBaseSql.append(" AND p.dstatus = :dstatus ");
			
			
			if (!StringUtil.isEmptyString(searchVO.getAgentId())) {
				searchBaseSql.append(" AND a.id = :agentId ");
			}
			
			searchBaseSql.append(" AND NOT EXISTS(SELECT 1 FROM Validation v ");
			searchBaseSql.append(" WHERE v.posId = p.posid AND v.agentId = a.id ");
			searchBaseSql.append(" AND DATE_FORMAT(v.ts, '%Y-%m') = b.g ");
			if (!StringUtil.isEmptyString(searchVO.getAgentId())) {
				searchBaseSql.append(" AND v.agentId = :agentId ");
			}
			searchBaseSql.append(" AND v.status = :status) ");
			
			searchSql.append(searchBaseSql);
			
			paramMap.put("dstatus", PosDeliveryStatus.DELIVERED.toString());
		}
		searchSql.append(") d order by d.reportMonth desc,d.agentName,d.posId ");
		Query searchQuery = getEm().createNativeQuery(searchSql.toString());
		if (paramMap != null) {
			for (Entry<String, Object> entry : paramMap.entrySet()) {
				searchQuery.setParameter(entry.getKey(), entry.getValue());
			}
		}
		//resultSet to List<FinanceReportVO>
		return getFinanceReportVOList(searchQuery.getResultList());
	}
	
	public PageInfo<FinanceReportVO> searchFinanceReport(FinanceReportSearchVO searchVO, PageInfo pageInfo) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer searchSql = new StringBuffer("select * from (");
		//交易成功的数据
		searchSql.append(" select DATE_FORMAT(v.ts, '%Y-%m') as reportMonth,v.agentName,v.posId,");
		searchSql.append(FinanceReportVO.BASE_AMOUNT + " AS baseAmount");
		searchSql.append(",COUNT(v.id) AS actuallyValCount");
		searchSql.append(", (CASE WHEN (COUNT(v.id) - " + FinanceReportVO.BASE_LINE + ") > 0 ");
		searchSql.append("THEN (COUNT(v.id) - " + FinanceReportVO.BASE_LINE + ") ELSE 0 END) AS beyondValCount");
		searchSql.append(", " + FinanceReportVO.UNIT_PRICE + " AS unitPrice");
		searchSql.append(", (CASE WHEN (COUNT(v.id) - " + FinanceReportVO.BASE_LINE + ") > 0 ");
		searchSql.append("THEN (COUNT(v.id) - " + FinanceReportVO.BASE_LINE + ") ELSE 0 END) ");
		searchSql.append("* " + FinanceReportVO.UNIT_PRICE + " AS beyondAmount");
		searchSql.append(", (" + FinanceReportVO.BASE_AMOUNT);
		searchSql.append(" + ((CASE WHEN (COUNT(v.id) - " + FinanceReportVO.BASE_LINE + ") > 0 ");
		searchSql.append("THEN (COUNT(v.id) - " + FinanceReportVO.BASE_LINE + ") ELSE 0 END) ");
		searchSql.append("* " + FinanceReportVO.UNIT_PRICE + ")) AS amount ");
		searchSql.append(" from Validation v ");
		searchSql.append(" where v.status = :status ");
		StringBuffer countSql = new StringBuffer("SELECT COUNT(1) FROM (SELECT 1 from Validation v where v.status = :status ");
		paramMap.put("status", ValidationStatus.SUCCESS.toString());
		if (searchVO != null) {
			if (!StringUtil.isEmptyString(searchVO.getAgentId())) {
				searchSql.append(" and v.agentId = :agentId");
				countSql.append(" and v.agentId = :agentId");
				paramMap.put("agentId", searchVO.getAgentId());
			}
			if (searchVO.getStartDate() != null) {
				searchSql.append(" and v.ts >= :startDate");
				countSql.append(" and v.ts >= :startDate");
				paramMap.put("startDate", searchVO.getStartDate());
			}
			if (searchVO.getEndDate() != null) {
				searchSql.append(" and v.ts <= :endDate");
				countSql.append(" and v.ts <= :endDate");
				paramMap.put("endDate", Tools.addDate(searchVO.getEndDate(), 1));
			}
		}
		searchSql.append(" group by DATE_FORMAT(v.ts, '%Y-%m'),v.agentId,v.posId ");
		countSql.append(" group by DATE_FORMAT(v.ts, '%Y-%m'),v.agentId,v.posId ");
		//加入保底的数据(union all)
		if (searchVO.getStartDate() != null && searchVO.getEndDate() != null) {
			//所查询的月份，通过全连接算出笛卡尔积
			String monthsSql = getMonthsSql(searchVO.getStartDate(),searchVO.getEndDate());
			StringBuffer searchBaseSql = new StringBuffer(" UNION ALL ");
			searchBaseSql.append(" SELECT b.g AS reportMonth, a.name AS agentName, p.posid,");
			searchBaseSql.append(FinanceReportVO.BASE_AMOUNT + " AS baseAmount,");
			searchBaseSql.append("0 AS actuallyValCount,0 AS beyondValCount, ");
			searchBaseSql.append(FinanceReportVO.UNIT_PRICE + " AS unitPrice, 0 AS beyondAmount, ");
			searchBaseSql.append(FinanceReportVO.BASE_AMOUNT + " AS amount ");
			searchBaseSql.append(" from Pos p, PosAssignment pa, Agent a ");
			
			StringBuffer countBaseSql = new StringBuffer(" UNION ALL ");
			countBaseSql.append(" SELECT 1 FROM Pos p, PosAssignment pa, Agent a ");
			
			searchBaseSql.append(", (" + monthsSql + ") b ");
			countBaseSql.append(", (" + monthsSql + ") b ");
			
			searchBaseSql.append(" WHERE pa.pos_id = p.id AND pa.agent_id = a.id ");
			searchBaseSql.append(" AND p.dstatus = :dstatus ");
			
			countBaseSql.append(" WHERE pa.pos_id = p.id AND pa.agent_id = a.id ");
			countBaseSql.append(" AND p.dstatus = :dstatus ");
			
			if (!StringUtil.isEmptyString(searchVO.getAgentId())) {
				searchBaseSql.append(" AND a.id = :agentId ");
				countBaseSql.append(" AND a.id = :agentId ");
			}
			
			searchBaseSql.append(" AND NOT EXISTS(SELECT 1 FROM Validation v ");
			searchBaseSql.append(" WHERE v.posId = p.posid AND v.agentId = a.id ");
			searchBaseSql.append(" AND DATE_FORMAT(v.ts, '%Y-%m') = b.g ");
			if (!StringUtil.isEmptyString(searchVO.getAgentId())) {
				searchBaseSql.append(" AND v.agentId = :agentId ");
			}
			searchBaseSql.append(" AND v.status = :status) ");
			
			searchSql.append(searchBaseSql);
			
			countBaseSql.append(" AND NOT EXISTS(SELECT 1 FROM Validation v ");
			countBaseSql.append(" WHERE v.posId = p.posid AND v.agentId = a.id ");
			countBaseSql.append(" AND DATE_FORMAT(v.ts, '%Y-%m') = b.g ");
			if (!StringUtil.isEmptyString(searchVO.getAgentId())) {
				countBaseSql.append(" AND v.agentId = :agentId ");
			}
			countBaseSql.append(" AND v.status = :status) ");
			
			countSql.append(countBaseSql);
			
			paramMap.put("dstatus", PosDeliveryStatus.DELIVERED.toString());
		}
		countSql.append(" ) d ");
		searchSql.append(") d order by d.reportMonth desc,d.agentName,d.posId ");
		PageInfo<FinanceReportVO> result = this.findPageInfoByNativeSearch(countSql.toString(), searchSql.toString(), paramMap, pageInfo);
		//resultSet to List<FinanceReportVO>
		result.setItems(getFinanceReportVOList(result.getItems()));
		return result;
	}
	
	/**
	 * 根据开始结束时间拼装出月份的SQL(union all)
	 * @author iori
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private String getMonthsSql(Date startDate, Date endDate) {
		//时间不可以为空
		if (startDate == null || endDate == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String sd = sdf.format(startDate);
		String ed = sdf.format(endDate);
		//开始月份大于结束月份
		if (sd.compareTo(ed) > 0) {
			return "";
		}
		//开始月份等于结束月份
		if (sd.compareTo(ed) == 0) {
			return "SELECT '" + sd + "' AS g FROM DUAL";
		}
		
		StringBuffer sb = null;
		while (sd.compareTo(ed) <= 0) {
			if (sb == null) {
				sb = new StringBuffer();
				sb.append("SELECT '" + sd + "' AS g FROM DUAL");
			} else {
				sb.append(" UNION ALL ");
				sb.append("SELECT '" + sd + "' AS g FROM DUAL");
			}
			//取下个月
			sd = getNextMonthStr(sd);
		}
		
		return sb.toString();
	}
	
	/**
	 * 取一个月
	 * @author iori
	 * @param date
	 * @return
	 */
	private String getNextMonthStr(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		try {
			Date d = sdf.parse(date);
			return sdf.format(Tools.addMonth(d, 1));
		} catch (ParseException e) {
			return "";
		}
	}
	
	private List<FinanceReportVO> getFinanceReportVOList(List resultSet) {
		List<FinanceReportVO> voList = new ArrayList<FinanceReportVO>();
		if (resultSet != null && resultSet.size() > 0) {
			for (int i = 0; i < resultSet.size(); i++) {
				Object[] o = (Object[]) resultSet.get(i);
				FinanceReportVO vo = new FinanceReportVO();
				vo.setReportMonth((String) o[0]);
				vo.setAgentName((String) o[1]);
				vo.setPosId((String) o[2]);
				vo.setBaseAmount(((BigInteger) o[3]).intValue());
				vo.setActuallyValCount(((BigInteger) o[4]).longValue());
				vo.setBeyondValCount(((BigInteger) o[5]).longValue());
				vo.setUnitPrice(((BigDecimal) o[6]).doubleValue());
				vo.setBeyondAmount(((BigDecimal) o[7]).doubleValue());
				vo.setAmount(((BigDecimal) o[8]).doubleValue());
				
				voList.add(vo);
			}
		}
		return voList;
	}
	
}
