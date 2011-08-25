package com.chinarewards.qqgpvn.main.logic.helloworld.impl;

import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import com.chinarewards.qqgbvpn.main.QQApiModule;
import com.chinarewards.qqgbvpn.main.logic.qqapi.GroupBuyingManager;
import com.chinarewards.qqgbvpn.qqapi.vo.GroupBuyingSearchListVO;
import com.chinarewards.qqgbvpn.qqapi.vo.GroupBuyingUnbindVO;
import com.chinarewards.qqgbvpn.qqapi.vo.GroupBuyingValidateResultVO;
import com.chinarewards.qqgpvn.main.test.GuiceTest;
import com.google.inject.Module;

public class QQApiTest extends GuiceTest {

	@Override
	protected Module[] getModules() {
		return new Module[] { new QQApiModule()};
	}

	@Test
	public void testGroupBuyingSearch() throws Exception {
		GroupBuyingManager gbm = getInjector().getInstance(
				GroupBuyingManager.class);
		HashMap<String,String> params = new HashMap<String,String>();
    	params.put("posId", "rewards-0001");
    	params.put("key", "456789000");
    	HashMap<String,Object> result = gbm.groupBuyingSearch(params);
    	String resultCode = (String) result.get("resultCode");
    	System.out.println("resultCode->" + resultCode);
    	if ("0".equals(resultCode)) {
    		List<GroupBuyingSearchListVO> items = (List<GroupBuyingSearchListVO>) result.get("items");
    		for (GroupBuyingSearchListVO item : items) {
    			System.out.println(item.getGrouponId());
    			System.out.println(item.getGrouponName());
    			System.out.println(item.getMercName());
    			System.out.println(item.getListName());
    			System.out.println(item.getDetailName());
    		}
    	} else {
    		switch (Integer.valueOf(resultCode)) {
	    		case -1 :
	    			System.out.println("服务器繁忙");
	    			break;
	    		case -2 :
	    			System.out.println("md5校验失败");
	    			break;
	    		case -3 :
	    			System.out.println("没有权限");
	    			break;
	    		default :
	    			System.out.println("未知错误");
	    			break;
    		}
    	}
	}
	
	@Test
	public void testGroupBuyingValidate() throws Exception {
    	GroupBuyingManager gbm = getInjector().getInstance(
				GroupBuyingManager.class);
    	HashMap<String,String> params = new HashMap<String,String>();
    	params.put("posId", "rewards-0001");
    	params.put("grouponId", "456789");
    	params.put("token", "4567890");
    	params.put("key", "456789000");
    	HashMap<String,Object> result = gbm.groupBuyingValidate(params);
    	String resultCode = (String) result.get("resultCode");
    	System.out.println("resultCode->" + resultCode);
    	if ("0".equals(resultCode)) {
    		List<GroupBuyingValidateResultVO> items = (List<GroupBuyingValidateResultVO>) result.get("items");
        	for (GroupBuyingValidateResultVO item : items) {
        		System.out.println(item.getResultName());
    			System.out.println(item.getResultExplain());
    			System.out.println(item.getCurrentTime());
    			System.out.println(item.getUseTime());
    			System.out.println(item.getValidTime());
    			System.out.println(item.getRefundTime());
    		}
    	} else {
    		switch (Integer.valueOf(resultCode)) {
	    		case -1 :
	    			System.out.println("服务器繁忙");
	    			break;
	    		case -2 :
	    			System.out.println("md5校验失败");
	    			break;
	    		case -3 :
	    			System.out.println("没有权限");
	    			break;
	    		default :
	    			System.out.println("未知错误");
	    			break;
    		}
    	}
	}
	
	@Test
	public void testGroupBuyingUnbind() throws Exception {
    	GroupBuyingManager gbm = getInjector().getInstance(
				GroupBuyingManager.class);
    	HashMap<String,Object> params = new HashMap<String,Object>();
    	params.put("posId", new String[]{"rewards-0001", "rewards-0002"});
    	params.put("key", "456789000");
    	HashMap<String,Object> result = gbm.groupBuyingUnbind(params);
    	String resultCode = (String) result.get("resultCode");
    	System.out.println("resultCode->" + resultCode);
    	if ("0".equals(resultCode)) {
    		List<GroupBuyingUnbindVO> items = (List<GroupBuyingUnbindVO>) result.get("items");
        	for (GroupBuyingUnbindVO item : items) {
        		System.out.println(item.getPosId());
    			System.out.println(item.getResultStatus());
    		}
    	} else {
    		switch (Integer.valueOf(resultCode)) {
	    		case -1 :
	    			System.out.println("服务器繁忙");
	    			break;
	    		case -2 :
	    			System.out.println("md5校验失败");
	    			break;
	    		case -3 :
	    			System.out.println("没有权限");
	    			break;
	    		default :
	    			System.out.println("未知错误");
	    			break;
    		}
    	}
	}
}
