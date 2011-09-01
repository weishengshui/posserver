package com.chinarewards.qqgbvpn.mgmtui.logic;

import java.util.HashMap;

import org.codehaus.jackson.JsonGenerationException;

import com.chinarewards.qqgbvpn.mgmtui.exception.SaveDBException;
import com.chinarewards.qqgbvpn.qqapi.exception.MD5Exception;
import com.chinarewards.qqgbvpn.qqapi.exception.ParseXMLException;
import com.chinarewards.qqgbvpn.qqapi.exception.SendPostTimeOutException;

public interface GroupBuyingUnbindManager {
	
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
