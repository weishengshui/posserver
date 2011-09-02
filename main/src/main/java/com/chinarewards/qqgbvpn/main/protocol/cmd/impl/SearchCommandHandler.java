package com.chinarewards.qqgbvpn.main.protocol.cmd.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.domain.GrouponCache;
import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.main.exception.SaveDBException;
import com.chinarewards.qqgbvpn.main.logic.qqapi.GroupBuyingManager;
import com.chinarewards.qqgbvpn.main.protocol.ServiceHandler;
import com.chinarewards.qqgbvpn.main.protocol.ServiceRequest;
import com.chinarewards.qqgbvpn.main.protocol.ServiceResponse;
import com.chinarewards.qqgbvpn.main.protocol.ServiceSession;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
import com.chinarewards.qqgbvpn.main.protocol.filter.LoginFilter;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.SearchRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.SearchResponseDetail;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.SearchResponseMessage;
import com.chinarewards.qqgbvpn.qqapi.exception.MD5Exception;
import com.chinarewards.qqgbvpn.qqapi.exception.ParseXMLException;
import com.chinarewards.qqgbvpn.qqapi.exception.SendPostTimeOutException;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class SearchCommandHandler implements ServiceHandler {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	private final int ERROR_CODE_OTHER = 1; 
	
	private final int SUCCESS_CODE = 0; 
	
	@Inject
	public Provider<GroupBuyingManager> gbm;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(ServiceRequest request, ServiceResponse response) {
		
		SearchRequestMessage bodyMessage = (SearchRequestMessage) request.getParameter();
		
		ServiceSession session = request.getSession();
		
		// TODO Auto-generated method stub
		log.debug("SearchCommandHandler======execute==bodyMessage=:"+bodyMessage);
		SearchRequestMessage searchRequestMessage = (SearchRequestMessage) bodyMessage;
		SearchResponseMessage searchResponseMessage = new SearchResponseMessage();

		log.debug("SearchCommandHandler======execute==posId=:"+String.valueOf(session.getAttribute(LoginFilter.POS_ID)));
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("posId", String.valueOf(session.getAttribute(LoginFilter.POS_ID)));
		params.put("curpage", String.valueOf(searchRequestMessage.getPage()));
		params.put("pageSize", String.valueOf(searchRequestMessage.getSize()));
		try {
			HashMap<String,Object> resultMap = gbm.get().groupBuyingSearch(params);
			String resultCode = (String) resultMap.get("resultCode");
			
			PageInfo<GrouponCache> pageInfo = (PageInfo<GrouponCache>) resultMap.get("pageInfo");
			List<GrouponCache> grouponCacheList = pageInfo.getItems();
			List<SearchResponseDetail> detailList = new ArrayList<SearchResponseDetail>();
			
			if (SUCCESS_CODE == Integer.valueOf(resultCode)) {
				searchResponseMessage.setCurpage(pageInfo.getPageId());
				searchResponseMessage.setTotalnum(pageInfo.getRecordCount());
				searchResponseMessage.setTotalpage(pageInfo.getPageCount());
				
				if(grouponCacheList!= null){
					searchResponseMessage.setCurnum(grouponCacheList.size());
					for(GrouponCache grouponCache :grouponCacheList){
						SearchResponseDetail detail = new SearchResponseDetail();
						detail.setDetailName(grouponCache.getDetailName());
						detail.setGrouponId(grouponCache.getGrouponId());
						detail.setGrouponName(grouponCache.getGrouponName());
						detail.setListName(grouponCache.getListName());
						detail.setMercName(grouponCache.getMercName());
						detailList.add(detail);
					}
				}else{
					searchResponseMessage.setCurnum(0);
				}
				searchResponseMessage.setResult(SUCCESS_CODE);
			} else {
				searchResponseMessage.setResult(Integer.valueOf(resultCode));
			}
			searchResponseMessage.setDetail(detailList);
		} catch (JsonGenerationException e) {
			searchResponseMessage.setResult(ERROR_CODE_OTHER);
		} catch (MD5Exception e) {
			searchResponseMessage.setResult(ERROR_CODE_OTHER);
		} catch (ParseXMLException e) {
			searchResponseMessage.setResult(ERROR_CODE_OTHER);
		} catch (SendPostTimeOutException e) {
			searchResponseMessage.setResult(ERROR_CODE_OTHER);
		} catch (SaveDBException e) {
			searchResponseMessage.setResult(ERROR_CODE_OTHER);
		}catch (Throwable e) {
			searchResponseMessage.setResult(ERROR_CODE_OTHER);
		}
		
		searchResponseMessage.setCmdId(CmdConstant.SEARCH_CMD_ID_RESPONSE);
		
		response.writeResponse(searchResponseMessage);
		
	}

}
