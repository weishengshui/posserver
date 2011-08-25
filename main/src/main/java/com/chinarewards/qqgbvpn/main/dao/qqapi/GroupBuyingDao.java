package com.chinarewards.qqgbvpn.main.dao.qqapi;

import com.chinarewards.qqgbvpn.domain.event.Journal;


public interface GroupBuyingDao {

	public void handleGroupBuyingSearch(Journal journal) throws Exception;
	
	public void handleGroupBuyingValidate() throws Exception;
	
	public void handleGroupBuyingUnbind() throws Exception;
}
