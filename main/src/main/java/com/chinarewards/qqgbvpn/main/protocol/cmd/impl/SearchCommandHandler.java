package com.chinarewards.qqgbvpn.main.protocol.cmd.impl;

import java.util.HashMap;

import org.apache.mina.core.session.IoSession;
import org.codehaus.jackson.JsonGenerationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.main.exception.SaveDBException;
import com.chinarewards.qqgbvpn.main.logic.qqapi.GroupBuyingManager;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CommandHandler;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.IBodyMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.SearchRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.SearchResponseMessage;
import com.chinarewards.qqgbvpn.qqapi.exception.MD5Exception;
import com.chinarewards.qqgbvpn.qqapi.exception.ParseXMLException;
import com.chinarewards.qqgbvpn.qqapi.exception.SendPostTimeOutException;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class SearchCommandHandler implements CommandHandler {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Inject
	public Provider<GroupBuyingManager> gbm;
	
	@Override
	public IBodyMessage execute(IoSession session, IBodyMessage bodyMessage) {
		// TODO Auto-generated method stub
		log.debug("SearchCommandHandler======execute==bodyMessage=:"+bodyMessage);
		SearchRequestMessage searchRequestMessage = (SearchRequestMessage) bodyMessage;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("posId", "rewards-0001");
		params.put("curpage", "3");
		params.put("pageSize", "1");
		try {
			PageInfo pageInfo = gbm.get().groupBuyingSearch(params);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MD5Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseXMLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SendPostTimeOutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SaveDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//TODO 
		SearchResponseMessage searchResponseMessage = new SearchResponseMessage();
		
		searchResponseMessage.setCmdId(CmdConstant.SEARCH_CMD_ID_RESPONSE);
		return searchResponseMessage;
	}

}
