package com.chinarewards.qqgbvpn.main.logic.qqapi;

import java.util.HashMap;

import org.codehaus.jackson.JsonGenerationException;

import com.chinarewards.qqgbvpn.main.exception.SaveDBException;
import com.chinarewards.qqgbvpn.qqapi.exception.MD5Exception;
import com.chinarewards.qqgbvpn.qqapi.exception.ParseXMLException;
import com.chinarewards.qqgbvpn.qqapi.exception.SendPostTimeOutException;

public interface GroupBuyingManager {

	/**
	 * 团购查询
	 * 
	 * @author iori
	 * @param params
	 * @return
	 */
	public HashMap<String, Object> groupBuyingSearch(
			HashMap<String, String> params) throws MD5Exception, ParseXMLException, SendPostTimeOutException, JsonGenerationException, SaveDBException;

	/**
	 * 团购验证
	 * 
	 * @author iori
	 * @param params
	 * @return
	 */
	public HashMap<String, Object> groupBuyingValidate(
			HashMap<String, String> params) throws MD5Exception, ParseXMLException, SendPostTimeOutException, JsonGenerationException, SaveDBException;

	/**
	 * 团购取消绑定
	 * 
	 * @author iori
	 * @param params
	 * @return
	 */
	public HashMap<String, Object> groupBuyingUnbind(
			HashMap<String, Object> params) throws MD5Exception, ParseXMLException, SendPostTimeOutException, JsonGenerationException, SaveDBException;
}
