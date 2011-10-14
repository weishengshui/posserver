package com.chinarewards.qqgbvpn.main.dao.huaxia.impl;

import java.util.List;

import javax.persistence.Query;

import com.chinarewards.qqgbvpn.core.BaseDao;
import com.chinarewards.qqgbvpn.domain.Agent;
import com.chinarewards.qqgbvpn.domain.HuaxiaRedeem;
import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.status.RedeemStatus;
import com.chinarewards.qqgbvpn.main.dao.huaxia.HuaxiaRedeemDao;
import com.chinarewards.utils.StringUtil;

public class HuaxiaRedeemDaoImpl extends BaseDao implements HuaxiaRedeemDao {

	
	/**
	 * 查询相应卡号的可用次数
	 * @param cardNum
	 * @return
	 */
	public int searchValidRedeemCountByCardNum(String cardNum) {
		if (StringUtil.isEmptyString(cardNum)) {
			return 0;
		}
		String sql = "select count(hr.id) from HuaxiaRedeem hr where hr.status in ('" +
				RedeemStatus.UNUSED.toString() +
				"','" +
				RedeemStatus.PEND_FOR_ACK.toString() +
				"') and hr.cardNum = :cardNum";
		Query jql = getEm().createQuery(sql);
		jql.setParameter("cardNum", cardNum);
		return ((Long) jql.getSingleResult()).intValue();
	}
	
	/**
	 * 查询相应状态相应POS机的次数
	 * @param cardNum
	 * @return
	 */
	public int getRedeemCountByPosId(String posId,String cardNum,RedeemStatus status) {
		if (StringUtil.isEmptyString(posId) || StringUtil.isEmptyString(cardNum)) {
			return 0;
		}
		String sql = "select count(hr.id) from HuaxiaRedeem hr where hr.status = :status and hr.cardNum = :cardNum and hr.posId = :posId";
		Query jql = getEm().createQuery(sql);
		jql.setParameter("status", status);
		jql.setParameter("cardNum", cardNum);
		jql.setParameter("posId", posId);
		return ((Long) jql.getSingleResult()).intValue();
	}
	
	/**
	 * 查询相应状态相应POS机的对象
	 * @param cardNum
	 * @return
	 */
	public HuaxiaRedeem getHuaxiaRedeemByPosId(String posId,String cardNum,RedeemStatus status) {
		if (StringUtil.isEmptyString(posId) || StringUtil.isEmptyString(cardNum)) {
			return null;
		}
		String sql = "select hr from HuaxiaRedeem hr where hr.status = :status and hr.cardNum = :cardNum and hr.posId = :posId";
		Query jql = getEm().createQuery(sql);
		jql.setParameter("status", status);
		jql.setParameter("cardNum", cardNum);
		jql.setParameter("posId", posId);
		List<HuaxiaRedeem> list = jql.getResultList();
		return list != null && list.size() > 0 ? list.get(0) : null;
	}
	
	/**
	 * 查询相应状态相应POS机的次数
	 * @param cardNum
	 * @return
	 */
	public int getRedeemCountByAckId(String posId,String cardNum,String ackId, String chanceId,RedeemStatus status) {
		if (StringUtil.isEmptyString(posId) 
				|| StringUtil.isEmptyString(cardNum) 
				|| StringUtil.isEmptyString(ackId) 
				|| StringUtil.isEmptyString(chanceId)) {
			return 0;
		}
		String sql = "select count(hr.id) from HuaxiaRedeem hr " +
				"where hr.status = :status " +
				"and hr.cardNum = :cardNum " +
				"and hr.posId = :posId " +
				"and hr.id = :chanceId " +
				"and hr.ackId = :ackId ";
		Query jql = getEm().createQuery(sql);
		jql.setParameter("status", status);
		jql.setParameter("cardNum", cardNum);
		jql.setParameter("posId", posId);
		jql.setParameter("chanceId", chanceId);
		jql.setParameter("ackId", ackId);
		return ((Long) jql.getSingleResult()).intValue();
	}
	
	/**
	 * 查询相应状态相应POS机的次数
	 * @param cardNum
	 * @return
	 */
	public HuaxiaRedeem getRedeemByAckId(String posId,String cardNum,String ackId, String chanceId,RedeemStatus status) {
		if (StringUtil.isEmptyString(posId) 
				|| StringUtil.isEmptyString(cardNum) 
				|| StringUtil.isEmptyString(ackId) 
				|| StringUtil.isEmptyString(chanceId)) {
			return null;
		}
		String sql = "select hr from HuaxiaRedeem hr " +
				"where hr.status = :status " +
				"and hr.cardNum = :cardNum " +
				"and hr.posId = :posId " +
				"and hr.id = :chanceId " +
				"and hr.ackId = :ackId ";
		Query jql = getEm().createQuery(sql);
		jql.setParameter("status", status);
		jql.setParameter("cardNum", cardNum);
		jql.setParameter("posId", posId);
		jql.setParameter("chanceId", chanceId);
		jql.setParameter("ackId", ackId);
		List<HuaxiaRedeem> list = jql.getResultList();
		return list != null && list.size() > 0 ? list.get(0) : null;
	}
	
	/**
	 * 查询相应状态的次数
	 * @param cardNum
	 * @return
	 */
	public int getRedeemCountByCardNum(String cardNum,RedeemStatus status) {
		if (StringUtil.isEmptyString(cardNum)) {
			return 0;
		}
		String sql = "select count(hr.id) from HuaxiaRedeem hr where hr.status = :status and hr.cardNum = :cardNum";
		Query jql = getEm().createQuery(sql);
		jql.setParameter("status", status);
		jql.setParameter("cardNum", cardNum);
		return ((Long) jql.getSingleResult()).intValue();
	}
	
	/**
	 * 查一个相应状态的对象
	 * @param cardNum
	 * @return
	 */
	public HuaxiaRedeem getHuaxiaRedeemByCardNum(String cardNum,RedeemStatus status) {
		if (StringUtil.isEmptyString(cardNum)) {
			return null;
		}
		String sql = "select hr from HuaxiaRedeem hr where hr.status = :status and hr.cardNum = :cardNum";
		Query jql = getEm().createQuery(sql);
		jql.setParameter("status", status);
		jql.setParameter("cardNum", cardNum);
		List<HuaxiaRedeem> list = jql.getResultList();
		return list != null && list.size() > 0 ? list.get(0) : null;
	}
	
	public void saveHuaxiaRedeem(HuaxiaRedeem redeem) {
		getEm().persist(redeem);
	}
	
	public Pos getPosByPosId(String posId) {
		try {
			Query jql = getEm().createQuery("select p from Pos p where p.posId = ?1");
			jql.setParameter(1, posId);
			Pos p = (Pos) jql.getSingleResult();
			return p;
		} catch (Exception e) {
			return null;
		}
	}
	
	public Agent getAgentByPosId(String posId) {
		try {
			Query jql = getEm().createQuery("select a from PosAssignment pa,Pos p,Agent a " +
					"where pa.pos.id = p.id and pa.agent.id = a.id and p.posId = ?1");
			jql.setParameter(1, posId);
			Agent a = (Agent) jql.getSingleResult();
			return a;
		} catch (Exception e) {
			return null;
		}
	}
	
}
