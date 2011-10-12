package com.chinarewards.qqgbvpn.main.dao.huaxia;

import com.chinarewards.qqgbvpn.main.vo.HuaxiaRedeemVO;



public interface HuaxiaRedeemDao {
	
	
	/**
	 * 根据银行卡号后四位查询
	 * @param bankNum
	 * @return
	 */
	public HuaxiaRedeemVO huaxiaRedeemSearch(HuaxiaRedeemVO params);
	
	/**
	 * 确认兑换
	 * @param bankNum
	 * @return
	 */
	public HuaxiaRedeemVO huaxiaRedeemConfirm(HuaxiaRedeemVO params);
	
	/**
	 * ACK兑换
	 * @param bankNum
	 * @return
	 */
	public HuaxiaRedeemVO huaxiaRedeemAck(HuaxiaRedeemVO params);
}
