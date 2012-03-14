package com.chinarewards.qqgbvpn.main.dao.qqapi;

import com.chinarewards.qq.meishi.domain.QQMeishiXaction;



public interface QQMeishiDao {
	
	/**
	 * 保存交易数据
	 * @param qqmeishiXaction
	 */
	public void saveQQMeishiXaction(QQMeishiXaction qqmeishiXaction);
}
