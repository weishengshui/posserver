package com.chinarewards.qqgbvpn.main.dao.huaxia;

import com.chinarewards.qqgbvpn.domain.Agent;
import com.chinarewards.qqgbvpn.domain.HuaxiaRedeem;
import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.status.RedeemStatus;




public interface HuaxiaRedeemDao {
	
	/**
	 * 查询相应卡号的可用次数
	 * @param cardNum
	 * @return
	 */
	public int searchValidRedeemCountByCardNum(String cardNum);
	
	/**
	 * 查询相应状态相应POS机的次数
	 * @param cardNum
	 * @return
	 */
	public int getRedeemCountByPosId(String posId,String cardNum,RedeemStatus status);
	
	/**
	 * 查询相应状态相应POS机的对象
	 * @param cardNum
	 * @return
	 */
	public HuaxiaRedeem getHuaxiaRedeemByPosId(String posId,String cardNum,RedeemStatus status);
	
	/**
	 * 查询相应状态的次数
	 * @param cardNum
	 * @return
	 */
	public int getRedeemCountByCardNum(String cardNum,RedeemStatus status);
	
	/**
	 * 查一个相应状态的对象
	 * @param cardNum
	 * @return
	 */
	public HuaxiaRedeem getHuaxiaRedeemByCardNum(String cardNum,RedeemStatus status);
	
	/**
	 * 查询相应状态相应POS机的次数
	 * @param cardNum
	 * @return
	 */
	public int getRedeemCountByAckId(String posId,String cardNum,String ackId, String chanceId,RedeemStatus status);
	
	/**
	 * 查询相应状态相应POS机的次数
	 * @param cardNum
	 * @return
	 */
	public HuaxiaRedeem getRedeemByAckId(String posId,String cardNum,String ackId, String chanceId,RedeemStatus status);
	
	public Pos getPosByPosId(String posId);
	
	public Agent getAgentByPosId(String posId);
	
	public void saveHuaxiaRedeem(HuaxiaRedeem redeem);
}
