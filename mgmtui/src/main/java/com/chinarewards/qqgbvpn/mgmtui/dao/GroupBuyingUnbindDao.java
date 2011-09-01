package com.chinarewards.qqgbvpn.mgmtui.dao;

import java.util.HashMap;

import org.codehaus.jackson.JsonGenerationException;

import com.chinarewards.qqgbvpn.mgmtui.exception.SaveDBException;


public interface GroupBuyingUnbindDao {
	
	public void handleGroupBuyingUnbind(HashMap<String, Object> params) throws SaveDBException, JsonGenerationException;
}
