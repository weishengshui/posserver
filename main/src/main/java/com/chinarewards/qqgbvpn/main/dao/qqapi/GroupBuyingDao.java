package com.chinarewards.qqgbvpn.main.dao.qqapi;

import java.util.HashMap;

import org.codehaus.jackson.JsonGenerationException;

import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.main.exception.CopyPropertiesException;
import com.chinarewards.qqgbvpn.main.exception.SaveDBException;


public interface GroupBuyingDao {
	
	public void initGrouponCache(HashMap<String, Object> params) throws SaveDBException, JsonGenerationException, CopyPropertiesException;

	public HashMap<String, Object> handleGroupBuyingSearch(HashMap<String, String> params) throws SaveDBException, JsonGenerationException;
	
	public PageInfo getGrouponCachePagination(PageInfo pageInfo, String posId);
	
	public void handleGroupBuyingValidate(HashMap<String, Object> params) throws SaveDBException;
	
	/*public void handleGroupBuyingUnbind(HashMap<String, Object> params) throws SaveDBException, JsonGenerationException;*/
}
