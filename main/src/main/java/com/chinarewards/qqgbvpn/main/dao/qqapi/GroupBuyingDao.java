package com.chinarewards.qqgbvpn.main.dao.qqapi;

import java.util.HashMap;


public interface GroupBuyingDao {

	public void handleGroupBuyingSearch(HashMap<String, Object> params) throws Exception;
	
	public void handleGroupBuyingValidate(HashMap<String, Object> params) throws Exception;
	
	public void handleGroupBuyingUnbind(HashMap<String, Object> params) throws Exception;
}
