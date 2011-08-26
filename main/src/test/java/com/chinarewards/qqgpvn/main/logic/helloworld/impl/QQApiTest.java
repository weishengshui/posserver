package com.chinarewards.qqgpvn.main.logic.helloworld.impl;

import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.junit.Test;

import com.chinarewards.qqgbvpn.main.QQApiModule;
import com.chinarewards.qqgbvpn.main.exception.SaveDBException;
import com.chinarewards.qqgbvpn.main.logic.qqapi.GroupBuyingManager;
import com.chinarewards.qqgbvpn.qqapi.exception.MD5Exception;
import com.chinarewards.qqgbvpn.qqapi.exception.ParseXMLException;
import com.chinarewards.qqgbvpn.qqapi.exception.SendPostTimeOutException;
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
	public void testGroupBuyingSearch() {
		GroupBuyingManager gbm = getInjector().getInstance(
				GroupBuyingManager.class);
		HashMap<String,String> params = new HashMap<String,String>();
    	params.put("posId", "rewards-0001");
    	params.put("key", "456789000");
		try {
			HashMap<String, Object> result = gbm.groupBuyingSearch(params);
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
		} catch (JsonGenerationException e) {
			System.err.println("生成JSON对象出错");
			e.printStackTrace();
		} catch (MD5Exception e) {
			System.err.println("生成MD5校验位出错");
			e.printStackTrace();
		} catch (ParseXMLException e) {
			System.err.println("解析XML出错");
			e.printStackTrace();
		} catch (SendPostTimeOutException e) {
			System.err.println("POST连接出错");
			e.printStackTrace();
		} catch (SaveDBException e) {
			System.err.println("后台保存数据库出错");
			System.out.println("具体异常信息：" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGroupBuyingValidate() {
    	GroupBuyingManager gbm = getInjector().getInstance(
				GroupBuyingManager.class);
    	HashMap<String,String> params = new HashMap<String,String>();
    	params.put("posId", "rewards-0001");
    	params.put("grouponId", "456789");
    	params.put("token", "4567890");
    	params.put("key", "456789000");
		try {
			HashMap<String, Object> result = gbm.groupBuyingValidate(params);
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
		} catch (JsonGenerationException e) {
			System.err.println("生成JSON对象出错");
			e.printStackTrace();
		} catch (MD5Exception e) {
			System.err.println("生成MD5校验位出错");
			e.printStackTrace();
		} catch (ParseXMLException e) {
			System.err.println("解析XML出错");
			e.printStackTrace();
		} catch (SendPostTimeOutException e) {
			System.err.println("POST连接出错");
			e.printStackTrace();
		} catch (SaveDBException e) {
			System.err.println("后台保存数据库出错");
			System.out.println("具体异常信息：" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGroupBuyingUnbind() {
    	GroupBuyingManager gbm = getInjector().getInstance(
				GroupBuyingManager.class);
    	HashMap<String,Object> params = new HashMap<String,Object>();
    	params.put("posId", new String[]{"rewards-0001", "rewards-0002", "rewards-0002"});
    	params.put("key", "456789000");
		try {
			HashMap<String, Object> result = gbm.groupBuyingUnbind(params);
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
		} catch (JsonGenerationException e) {
			System.err.println("生成JSON对象出错");
			e.printStackTrace();
		} catch (MD5Exception e) {
			System.err.println("生成MD5校验位出错");
			e.printStackTrace();
		} catch (ParseXMLException e) {
			System.err.println("解析XML出错");
			e.printStackTrace();
		} catch (SendPostTimeOutException e) {
			System.err.println("POST连接出错");
			e.printStackTrace();
		} catch (SaveDBException e) {
			System.err.println("后台保存数据库出错");
			System.out.println("具体异常信息：" + e.getMessage());
			e.printStackTrace();
		}
	}
	
}
