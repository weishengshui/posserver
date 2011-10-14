package com.chinarewards.qqgbvpn.main.logic.huaxia.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.common.DateTimeProvider;
import com.chinarewards.qqgbvpn.domain.Agent;
import com.chinarewards.qqgbvpn.domain.HuaxiaRedeem;
import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.event.DomainEntity;
import com.chinarewards.qqgbvpn.domain.event.DomainEvent;
import com.chinarewards.qqgbvpn.domain.status.RedeemStatus;
import com.chinarewards.qqgbvpn.logic.journal.JournalLogic;
import com.chinarewards.qqgbvpn.main.dao.huaxia.HuaxiaRedeemDao;
import com.chinarewards.qqgbvpn.main.logic.huaxia.HuaxiaRedeemManager;
import com.chinarewards.qqgbvpn.main.util.Util;
import com.chinarewards.qqgbvpn.main.vo.HuaxiaRedeemVO;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

public class HuaxiaRedeemManagerImpl implements HuaxiaRedeemManager {
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	@Inject
	private Provider<HuaxiaRedeemDao> dao;
	
	@Inject
	private JournalLogic journalLogic;
	
	@Inject
	private DateTimeProvider dtProvider;
	
	/**
	 * 根据银行卡号后四位查询
	 * @param bankNum
	 * @return
	 */
	public HuaxiaRedeemVO huaxiaRedeemSearch(HuaxiaRedeemVO params) {
		if (params == null) {
			HuaxiaRedeemVO vo = new HuaxiaRedeemVO();
			vo.setRedeemCount(0);
			return vo;
		}
		String cardNum = params.getCardNum();
		int redeemCount = dao.get().searchValidRedeemCountByCardNum(cardNum);
		if (redeemCount > HuaxiaRedeemVO.REDEEM_MAXCOUNT) {
			redeemCount = HuaxiaRedeemVO.REDEEM_MAXCOUNT;
		}
		params.setRedeemCount(redeemCount);
		if (redeemCount > 0) {
			params.setResult(HuaxiaRedeemVO.REDEEM_RESULT_SUCCESS);
		} else {
			params.setResult(HuaxiaRedeemVO.REDEEM_RESULT_NONE);
		}
		
		try {
			HuaxiaRedeemVO huaxiaRedeemVO = new HuaxiaRedeemVO();
			huaxiaRedeemVO.setPosId(params.getPosId());
			huaxiaRedeemVO.setCardNum(cardNum);
			huaxiaRedeemVO.setRedeemCount(params.getRedeemCount());
			
			// Add journal.
			ObjectMapper mapper = new ObjectMapper();
			String eventDetail = null;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("posId", params.getPosId());
			map.put("cardNum", cardNum);
			map.put("redeemCount", params.getRedeemCount());
			map.put("result", params.getResult());
			try {
				eventDetail = mapper.writeValueAsString(map);
			} catch (Exception e) {
				log.error("convert map to json error.", e);
				eventDetail = e.toString();
			}
			journalLogic.logEvent(DomainEvent.HUAXIA_REDEEM_QUERY.toString(),
					DomainEntity.HUAXIA_REDEEM.toString(), huaxiaRedeemVO.getPosId(), eventDetail);
		} catch (Exception e) {
			log.error("huaxia Redeem Search save journal error", e);
		}
		
		return params;
	}
	
	/**
	 * 确认兑换
	 * @param bankNum
	 * @return
	 */
	@Transactional
	public HuaxiaRedeemVO huaxiaRedeemConfirm(HuaxiaRedeemVO params) {
		int result = HuaxiaRedeemVO.REDEEM_RESULT_FAIL;
		Date date = dtProvider.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (params != null) {
			String posId = params.getPosId();
			String cardNum = params.getCardNum();
			Pos pos = dao.get().getPosByPosId(posId);
			Agent agent = dao.get().getAgentByPosId(posId);
			if (pos != null && agent != null) {
				//先找自己的A
				int redeemCountPosA = dao.get().getRedeemCountByPosId(posId,cardNum,RedeemStatus.PEND_FOR_ACK);
				if (redeemCountPosA > 0) {
					HuaxiaRedeem redeem = dao.get().getHuaxiaRedeemByPosId(posId,cardNum,RedeemStatus.PEND_FOR_ACK);
					if (redeem != null) {
						try {
							//这种情况不改变验证时间
							//redeem.setConfirmDate(date);
							//更新ackId
							redeem.setAckId(Util.getUUID(true));
							
							dao.get().saveHuaxiaRedeem(redeem);
							
							// Add journal.
							ObjectMapper mapper = new ObjectMapper();
							String eventDetail = null;
							try {
								eventDetail = mapper.writeValueAsString(redeem);
							} catch (Exception e) {
								log.error("convert HuaxiaRedeem to json error.", e);
								eventDetail = e.toString();
							}
							journalLogic.logEvent(DomainEvent.HUAXIA_REDEEM_COMFIRM.toString(),
									DomainEntity.HUAXIA_REDEEM.toString(), redeem.getId(), eventDetail);
							
							result = HuaxiaRedeemVO.REDEEM_RESULT_SUCCESS;
						} catch (Exception e) {
							log.error("huaxia Redeem Confirm save error", e);
							result = HuaxiaRedeemVO.REDEEM_RESULT_FAIL_SAVE_EXCEPTION;
						}
						params.setResult(result);
						params.setChanceId(redeem.getId());
						params.setAckId(redeem.getAckId());
						params.setTxDate(sdf.format(redeem.getConfirmDate()));
						return params;
					}
				}
				//自己的A没有，则处理U
				int redeemCount = dao.get().getRedeemCountByCardNum(cardNum,RedeemStatus.UNUSED);
				if (redeemCount > 0) {
					HuaxiaRedeem redeem = dao.get().getHuaxiaRedeemByCardNum(cardNum,RedeemStatus.UNUSED);
					if (redeem != null) {
						try {
							redeem.setAgentId(agent.getId());
							redeem.setAgentName(agent.getName());
							redeem.setPosId(pos.getPosId());
							redeem.setPosModel(pos.getModel());
							redeem.setPosSimPhoneNo(pos.getSimPhoneNo());
							redeem.setConfirmDate(date);
							redeem.setStatus(RedeemStatus.PEND_FOR_ACK);
							redeem.setAckId(Util.getUUID(true));
							
							dao.get().saveHuaxiaRedeem(redeem);
							
							// Add journal.
							ObjectMapper mapper = new ObjectMapper();
							String eventDetail = null;
							try {
								eventDetail = mapper.writeValueAsString(redeem);
							} catch (Exception e) {
								log.error("convert HuaxiaRedeem to json error.", e);
								eventDetail = e.toString();
							}
							journalLogic.logEvent(DomainEvent.HUAXIA_REDEEM_COMFIRM.toString(),
									DomainEntity.HUAXIA_REDEEM.toString(), redeem.getId(), eventDetail);
							
							result = HuaxiaRedeemVO.REDEEM_RESULT_SUCCESS;
						} catch (Exception e) {
							log.error("huaxia Redeem Confirm save error", e);
							result = HuaxiaRedeemVO.REDEEM_RESULT_FAIL_SAVE_EXCEPTION;
						}
						params.setResult(result);
						params.setChanceId(redeem.getId());
						params.setAckId(redeem.getAckId());
						params.setTxDate(sdf.format(redeem.getConfirmDate()));
						return params;
					}
				}
				//处理其它POS机A状态的,如果也没有，则返回没有机会
				int redeemCountStatusA = dao.get().getRedeemCountByCardNum(cardNum,RedeemStatus.PEND_FOR_ACK);
				if (redeemCountStatusA > 0) {
					HuaxiaRedeem redeem = dao.get().getHuaxiaRedeemByCardNum(cardNum,RedeemStatus.PEND_FOR_ACK);
					if (redeem != null) {
						try {
							//这里要更新对象，是因为用户是换了POS做的兑换
							redeem.setAgentId(agent.getId());
							redeem.setAgentName(agent.getName());
							redeem.setPosId(pos.getPosId());
							redeem.setPosModel(pos.getModel());
							redeem.setPosSimPhoneNo(pos.getSimPhoneNo());
							redeem.setConfirmDate(date);
							redeem.setStatus(RedeemStatus.PEND_FOR_ACK);
							redeem.setAckId(Util.getUUID(true));
							
							dao.get().saveHuaxiaRedeem(redeem);
							
							// Add journal.
							ObjectMapper mapper = new ObjectMapper();
							String eventDetail = null;
							try {
								eventDetail = mapper.writeValueAsString(redeem);
							} catch (Exception e) {
								log.error("convert HuaxiaRedeem to json error.", e);
								eventDetail = e.toString();
							}
							journalLogic.logEvent(DomainEvent.HUAXIA_REDEEM_COMFIRM.toString(),
									DomainEntity.HUAXIA_REDEEM.toString(), redeem.getId(), eventDetail);
							
							result = HuaxiaRedeemVO.REDEEM_RESULT_SUCCESS;
						} catch (Exception e) {
							log.error("huaxia Redeem Confirm save error", e);
							result = HuaxiaRedeemVO.REDEEM_RESULT_FAIL_SAVE_EXCEPTION;
						}
						params.setResult(result);
						params.setChanceId(redeem.getId());
						params.setAckId(redeem.getAckId());
						params.setTxDate(sdf.format(redeem.getConfirmDate()));
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
	
	/**
	 * ACK兑换
	 * @param bankNum
	 * @return
	 */
	@Transactional
	public HuaxiaRedeemVO huaxiaRedeemAck(HuaxiaRedeemVO params) {
		int result = HuaxiaRedeemVO.REDEEM_RESULT_FAIL;
		if (params != null) {
			String posId = params.getPosId();
			String cardNum = params.getCardNum();
			String ackId = params.getAckId();
			String chanceId = params.getChanceId();
			Pos pos = dao.get().getPosByPosId(posId);
			Agent agent = dao.get().getAgentByPosId(posId);
			if (pos != null && agent != null) {
				int ackCount = dao.get().getRedeemCountByAckId(posId,cardNum,ackId,chanceId,RedeemStatus.DONE);
				if (ackCount > 0) {
					result = HuaxiaRedeemVO.REDEEM_RESULT_ALREADY_ACKED;
					params.setResult(result);
					return params;
				}
				int redeemCount = dao.get().getRedeemCountByAckId(posId,cardNum,ackId,chanceId,RedeemStatus.PEND_FOR_ACK);
				if (redeemCount > 0) {
					HuaxiaRedeem redeem = dao.get().getRedeemByAckId(posId,cardNum,ackId,chanceId,RedeemStatus.PEND_FOR_ACK);
					if (redeem != null) {
						try {
							Date date = dtProvider.getTime();
							redeem.setAgentId(agent.getId());
							redeem.setAgentName(agent.getName());
							redeem.setPosId(pos.getPosId());
							redeem.setPosModel(pos.getModel());
							redeem.setPosSimPhoneNo(pos.getSimPhoneNo());
							redeem.setAckDate(date);
							redeem.setStatus(RedeemStatus.DONE);
							
							dao.get().saveHuaxiaRedeem(redeem);
							
							// Add journal.
							ObjectMapper mapper = new ObjectMapper();
							String eventDetail = null;
							try {
								eventDetail = mapper.writeValueAsString(redeem);
							} catch (Exception e) {
								log.error("convert HuaxiaRedeem to json error.", e);
								eventDetail = e.toString();
							}
							journalLogic.logEvent(DomainEvent.HUAXIA_REDEEM_ACK.toString(),
									DomainEntity.HUAXIA_REDEEM.toString(), redeem.getId(), eventDetail);
							
							result = HuaxiaRedeemVO.REDEEM_RESULT_SUCCESS;
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							params.setTxDate(sdf.format(redeem.getAckDate()));
						} catch (Exception e) {
							log.error("huaxia Redeem Ack save error", e);
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
    
}
