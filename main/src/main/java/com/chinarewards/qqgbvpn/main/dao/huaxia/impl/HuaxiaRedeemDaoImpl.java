package com.chinarewards.qqgbvpn.main.dao.huaxia.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Query;

import org.codehaus.jackson.map.ObjectMapper;

import com.chinarewards.qqgbvpn.core.BaseDao;
import com.chinarewards.qqgbvpn.domain.Agent;
import com.chinarewards.qqgbvpn.domain.HuaxiaRedeem;
import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.event.DomainEntity;
import com.chinarewards.qqgbvpn.domain.event.DomainEvent;
import com.chinarewards.qqgbvpn.domain.event.Journal;
import com.chinarewards.qqgbvpn.domain.status.RedeemStatus;
import com.chinarewards.qqgbvpn.main.dao.huaxia.HuaxiaRedeemDao;
import com.chinarewards.qqgbvpn.main.util.Util;
import com.chinarewards.qqgbvpn.main.vo.HuaxiaRedeemVO;
import com.chinarewards.utils.StringUtil;

public class HuaxiaRedeemDaoImpl extends BaseDao implements HuaxiaRedeemDao {

	@Override
	public HuaxiaRedeemVO huaxiaRedeemSearch(HuaxiaRedeemVO params) {
		if (params == null) {
			HuaxiaRedeemVO vo = new HuaxiaRedeemVO();
			vo.setRedeemCount(0);
			return vo;
		}
		String cardNum = params.getCardNum();
		params.setRedeemCount(searchCalidRedeemCountByCardNum(cardNum));
		return params;
	}

	@Override
	public HuaxiaRedeemVO huaxiaRedeemConfirm(HuaxiaRedeemVO params) {
		int result = HuaxiaRedeemVO.REDEEM_RESULT_FAIL;
		if (params != null) {
			String posId = params.getPosId();
			String cardNum = params.getCardNum();
			Pos pos = getPosByPosId(posId);
			Agent agent = getAgentByPosId(posId);
			if (pos != null && agent != null) {
				//先找自己的A
				int redeemCountPosA = getRedeemCountByPosId(posId,cardNum,RedeemStatus.A);
				if (redeemCountPosA > 0) {
					HuaxiaRedeem redeem = getHuaxiaRedeemByPosId(posId,cardNum,RedeemStatus.A);
					if (redeem != null) {
						try {
							Date date = new Date();
							redeem.setConfirmDate(date);
							//更新ackId
							redeem.setAckId(Util.getUUID(true));
							
							saveHuaxiaRedeem(redeem);
							
							Journal journal = new Journal();
							journal.setTs(date);
							journal.setEntity(DomainEntity.HUAXIA_REDEEM.toString());
							journal.setEntityId(redeem.getId());
							journal.setEvent(DomainEvent.HUAXIA_REDEEM_COMFIRM.toString());
							ObjectMapper mapper = new ObjectMapper();
							journal.setEventDetail(mapper.writeValueAsString(redeem));
							saveJournal(journal);
							result = HuaxiaRedeemVO.REDEEM_RESULT_SUCCESS;
						} catch (Exception e) {
							e.printStackTrace();
							result = HuaxiaRedeemVO.REDEEM_RESULT_FAIL_SAVE_EXCEPTION;
						}
						params.setResult(result);
						params.setChanceId(redeem.getId());
						params.setAckId(redeem.getAckId());
						return params;
					}
				}
				//自己的A没有，则处理U
				int redeemCount = getRedeemCountByCardNum(cardNum,RedeemStatus.U);
				if (redeemCount > 0) {
					HuaxiaRedeem redeem = getHuaxiaRedeemByCardNum(cardNum,RedeemStatus.U);
					if (redeem != null) {
						try {
							Date date = new Date();
							redeem.setAgentId(agent.getId());
							redeem.setAgentName(agent.getName());
							redeem.setPosId(pos.getPosId());
							redeem.setPosModel(pos.getModel());
							redeem.setPosSimPhoneNo(pos.getSimPhoneNo());
							redeem.setConfirmDate(date);
							redeem.setStatus(RedeemStatus.A);
							redeem.setAckId(Util.getUUID(true));
							
							saveHuaxiaRedeem(redeem);
							
							Journal journal = new Journal();
							journal.setTs(date);
							journal.setEntity(DomainEntity.HUAXIA_REDEEM.toString());
							journal.setEntityId(redeem.getId());
							journal.setEvent(DomainEvent.HUAXIA_REDEEM_COMFIRM.toString());
							ObjectMapper mapper = new ObjectMapper();
							journal.setEventDetail(mapper.writeValueAsString(redeem));
							saveJournal(journal);
							result = HuaxiaRedeemVO.REDEEM_RESULT_SUCCESS;
						} catch (Exception e) {
							e.printStackTrace();
							result = HuaxiaRedeemVO.REDEEM_RESULT_FAIL_SAVE_EXCEPTION;
						}
						params.setResult(result);
						params.setChanceId(redeem.getId());
						params.setAckId(redeem.getAckId());
						return params;
					}
				}
				//处理其它POS机A状态的,如果也没有，则返回没有机会
				int redeemCountStatusA = getRedeemCountByCardNum(cardNum,RedeemStatus.A);
				if (redeemCountStatusA > 0) {
					HuaxiaRedeem redeem = getHuaxiaRedeemByCardNum(cardNum,RedeemStatus.A);
					if (redeem != null) {
						try {
							//这里要更新对象，是因为用户是换了POS做的兑换
							Date date = new Date();
							redeem.setAgentId(agent.getId());
							redeem.setAgentName(agent.getName());
							redeem.setPosId(pos.getPosId());
							redeem.setPosModel(pos.getModel());
							redeem.setPosSimPhoneNo(pos.getSimPhoneNo());
							redeem.setConfirmDate(date);
							redeem.setStatus(RedeemStatus.A);
							redeem.setAckId(Util.getUUID(true));
							
							saveHuaxiaRedeem(redeem);
							
							Journal journal = new Journal();
							journal.setTs(date);
							journal.setEntity(DomainEntity.HUAXIA_REDEEM.toString());
							journal.setEntityId(redeem.getId());
							journal.setEvent(DomainEvent.HUAXIA_REDEEM_COMFIRM.toString());
							ObjectMapper mapper = new ObjectMapper();
							journal.setEventDetail(mapper.writeValueAsString(redeem));
							saveJournal(journal);
							result = HuaxiaRedeemVO.REDEEM_RESULT_SUCCESS;
						} catch (Exception e) {
							e.printStackTrace();
							result = HuaxiaRedeemVO.REDEEM_RESULT_FAIL_SAVE_EXCEPTION;
						}
						params.setResult(result);
						params.setChanceId(redeem.getId());
						params.setAckId(redeem.getAckId());
						return params;
					} else {
						//没有机会
						result = HuaxiaRedeemVO.REDEEM_RESULT_NONE;
					}
				} else {
					//没有机会
					result = HuaxiaRedeemVO.REDEEM_RESULT_NONE;
				}
			} else {
				//POS机或代理商找不到
				result = HuaxiaRedeemVO.REDEEM_RESULT_FAIL_POS_NONE;
			}
		}
		params.setResult(result);
		return params;
	}

	@Override
	public HuaxiaRedeemVO huaxiaRedeemAck(HuaxiaRedeemVO params) {
		int result = HuaxiaRedeemVO.REDEEM_RESULT_FAIL;
		if (params != null) {
			String posId = params.getPosId();
			String cardNum = params.getCardNum();
			String ackId = params.getAckId();
			String chanceId = params.getChanceId();
			Pos pos = getPosByPosId(posId);
			Agent agent = getAgentByPosId(posId);
			if (pos != null && agent != null) {
				int ackCount = getRedeemCountByAckId(posId,cardNum,ackId,chanceId,RedeemStatus.R);
				if (ackCount > 0) {
					result = HuaxiaRedeemVO.REDEEM_RESULT_ALREADY_ACKED;
					params.setResult(result);
					return params;
				}
				int redeemCount = getRedeemCountByAckId(posId,cardNum,ackId,chanceId,RedeemStatus.A);
				if (redeemCount > 0) {
					HuaxiaRedeem redeem = getRedeemByAckId(posId,cardNum,ackId,chanceId,RedeemStatus.A);
					if (redeem != null) {
						try {
							Date date = new Date();
							redeem.setAgentId(agent.getId());
							redeem.setAgentName(agent.getName());
							redeem.setPosId(pos.getPosId());
							redeem.setPosModel(pos.getModel());
							redeem.setPosSimPhoneNo(pos.getSimPhoneNo());
							redeem.setAckDate(date);
							redeem.setStatus(RedeemStatus.R);
							
							saveHuaxiaRedeem(redeem);
							
							Journal journal = new Journal();
							journal.setTs(date);
							journal.setEntity(DomainEntity.HUAXIA_REDEEM.toString());
							journal.setEntityId(redeem.getId());
							journal.setEvent(DomainEvent.HUAXIA_REDEEM_ACK.toString());
							ObjectMapper mapper = new ObjectMapper();
							journal.setEventDetail(mapper.writeValueAsString(redeem));
							saveJournal(journal);
							result = HuaxiaRedeemVO.REDEEM_RESULT_SUCCESS;
						} catch (Exception e) {
							e.printStackTrace();
							result = HuaxiaRedeemVO.REDEEM_RESULT_FAIL_SAVE_EXCEPTION;
						}
					} else {
						//没有机会
						result = HuaxiaRedeemVO.REDEEM_RESULT_NONE;
					}
				} else {
					//没有机会
					result = HuaxiaRedeemVO.REDEEM_RESULT_NONE;
				}
			} else {
				//POS机或代理商找不到
				result = HuaxiaRedeemVO.REDEEM_RESULT_FAIL_POS_NONE;
			}
		}
		params.setResult(result);
		return params;
	}
	
	/**
	 * 查询相应卡号的可用次数
	 * @param cardNum
	 * @return
	 */
	private int searchCalidRedeemCountByCardNum(String cardNum) {
		if (StringUtil.isEmptyString(cardNum)) {
			return 0;
		}
		String sql = "select count(hr.id) from HuaxiaRedeem hr where hr.status in ('" +
				RedeemStatus.U.toString() +
				"','" +
				RedeemStatus.A.toString() +
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
	private int getRedeemCountByPosId(String posId,String cardNum,RedeemStatus status) {
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
	private HuaxiaRedeem getHuaxiaRedeemByPosId(String posId,String cardNum,RedeemStatus status) {
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
	private int getRedeemCountByAckId(String posId,String cardNum,String ackId, String chanceId,RedeemStatus status) {
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
	private HuaxiaRedeem getRedeemByAckId(String posId,String cardNum,String ackId, String chanceId,RedeemStatus status) {
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
	private int getRedeemCountByCardNum(String cardNum,RedeemStatus status) {
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
	private HuaxiaRedeem getHuaxiaRedeemByCardNum(String cardNum,RedeemStatus status) {
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
	
	private void saveHuaxiaRedeem(HuaxiaRedeem redeem) {
		getEm().persist(redeem);
	}
	
	private void saveJournal(Journal journal) {
		getEm().persist(journal);
	}
	
	private Pos getPosByPosId(String posId) {
		try {
			Query jql = getEm().createQuery("select p from Pos p where p.posId = ?1");
			jql.setParameter(1, posId);
			Pos p = (Pos) jql.getSingleResult();
			return p;
		} catch (Exception e) {
			return null;
		}
	}
	
	private Agent getAgentByPosId(String posId) {
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
