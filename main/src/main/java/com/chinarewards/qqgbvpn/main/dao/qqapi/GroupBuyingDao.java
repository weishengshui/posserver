package com.chinarewards.qqgbvpn.main.dao.qqapi;

import java.util.HashMap;

import org.codehaus.jackson.JsonGenerationException;

import com.chinarewards.qqgbvpn.main.exception.SaveDBException;


public interface GroupBuyingDao {

	public void handleGroupBuyingSearch(HashMap<String, Object> params) throws SaveDBException, JsonGenerationException;
	
	public void handleGroupBuyingValidate(HashMap<String, Object> params) throws SaveDBException;
	
	public void handleGroupBuyingUnbind(HashMap<String, Object> params) throws SaveDBException, JsonGenerationException;
}
