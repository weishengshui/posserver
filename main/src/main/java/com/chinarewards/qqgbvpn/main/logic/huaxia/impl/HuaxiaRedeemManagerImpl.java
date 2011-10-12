package com.chinarewards.qqgbvpn.main.logic.huaxia.impl;

import com.chinarewards.qqgbvpn.main.dao.huaxia.HuaxiaRedeemDao;
import com.chinarewards.qqgbvpn.main.logic.huaxia.HuaxiaRedeemManager;
import com.chinarewards.qqgbvpn.main.vo.HuaxiaRedeemVO;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

public class HuaxiaRedeemManagerImpl implements HuaxiaRedeemManager {
	
	@Inject
	private Provider<HuaxiaRedeemDao> dao;
	
	/**
	 * 根据银行卡号后四位查询
	 * @param bankNum
	 * @return
	 */
	public HuaxiaRedeemVO huaxiaRedeemSearch(HuaxiaRedeemVO params) {
		return dao.get().huaxiaRedeemSearch(params);
	}
	
	/**
	 * 确认兑换
	 * @param bankNum
	 * @return
	 */
	@Transactional
	public HuaxiaRedeemVO huaxiaRedeemConfirm(HuaxiaRedeemVO params) {
		return dao.get().huaxiaRedeemConfirm(params);
	}
	
	/**
	 * ACK兑换
	 * @param bankNum
	 * @return
	 */
	@Transactional
	public HuaxiaRedeemVO huaxiaRedeemAck(HuaxiaRedeemVO params) {
		return dao.get().huaxiaRedeemAck(params);
	}
    
}
