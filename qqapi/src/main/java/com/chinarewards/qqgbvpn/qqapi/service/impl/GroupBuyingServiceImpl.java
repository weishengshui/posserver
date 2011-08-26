package com.chinarewards.qqgbvpn.qqapi.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import com.chinarewards.qqgbvpn.qqapi.exception.MD5Exception;
import com.chinarewards.qqgbvpn.qqapi.exception.ParseXMLException;
import com.chinarewards.qqgbvpn.qqapi.exception.SendPostTimeOutException;
import com.chinarewards.qqgbvpn.qqapi.service.GroupBuyingService;
import com.chinarewards.qqgbvpn.qqapi.util.GroupBuyingUtil;
import com.chinarewards.qqgbvpn.qqapi.vo.GroupBuyingSearchListVO;
import com.chinarewards.qqgbvpn.qqapi.vo.GroupBuyingUnbindVO;
import com.chinarewards.qqgbvpn.qqapi.vo.GroupBuyingValidateResultVO;

public class GroupBuyingServiceImpl implements GroupBuyingService {

	Properties config;

	/**
	 * 
	 */
	public GroupBuyingServiceImpl() {
		this(new Properties());
	}

	/**
	 * 
	 * @param config
	 */
	public GroupBuyingServiceImpl(Properties config) {
		if (config == null)
			throw new NullPointerException();
		this.config = config;
	}
	
	/**
	 * 团购查询
	 * 
	 * @author iori
	 * @param params
	 *            map中key必须包括:posId,key
	 * @return
	 * @throws MD5Exception 
	 * @throws SendPostTimeOutException 
	 * @throws ParseXMLException 
	 * @throws Exception
	 */
	public HashMap<String, Object> groupBuyingSearch(
			HashMap<String, String> params) throws MD5Exception, ParseXMLException, SendPostTimeOutException {
		// 根据QQ接口需要 ,封装POST参数
		HashMap<String, Object> postParams = new HashMap<String, Object>();
		// post参数:posId
		postParams.put("posId", params.get("posId"));
		StringBuffer sb = new StringBuffer("posId=");
		sb.append(params.get("posId"));
		sb.append("&key=");
		sb.append(params.get("key"));
		// post参数中,sign需要MD5加密
		postParams.put("sign", GroupBuyingUtil.MD5(sb.toString()));
		// 发送POST请求，并得到返回数据
		String url = config.getProperty("qq.groupbuy.url.groupBuyingSearchGroupon");
		System.out.println(url);
		HashMap<String, Object> searchResult = GroupBuyingUtil.parseXML(
				GroupBuyingUtil.sendPost(url, postParams), "//groupon/item",
				GroupBuyingSearchListVO.class);
		return searchResult;
	}

	/**
	 * 团购验证
	 * 
	 * @author iori
	 * @param params
	 *            map中key必须包括:posId,grouponId,token,key
	 * @return
	 * @throws MD5Exception 
	 * @throws SendPostTimeOutException 
	 * @throws ParseXMLException 
	 */
	public HashMap<String, Object> groupBuyingValidate(
			HashMap<String, String> params) throws MD5Exception, ParseXMLException, SendPostTimeOutException {
		// 根据QQ接口需要 ,封装POST参数
		HashMap<String, Object> postParams = new HashMap<String, Object>();
		// post参数:posId
		postParams.put("posId", params.get("posId"));
		// post参数:grouponId
		postParams.put("grouponId", params.get("grouponId"));
		// post参数:token
		postParams.put("token", params.get("token"));
		StringBuffer sb = new StringBuffer("posId=");
		sb.append(params.get("posId"));
		sb.append("&grouponId=");
		sb.append(params.get("grouponId"));
		sb.append("&token=");
		sb.append(params.get("token"));
		sb.append("&key=");
		sb.append(params.get("key"));
		// post参数中,sign需要MD5加密
		postParams.put("sign", GroupBuyingUtil.MD5(sb.toString()));
		// 发送POST请求，并得到返回数据
		String url = config.getProperty("qq.groupbuy.url.groupBuyingValidationUrl");
		HashMap<String, Object> validateResult = GroupBuyingUtil.parseXML(
				GroupBuyingUtil.sendPost(url, postParams), "//groupon",
				GroupBuyingValidateResultVO.class);
		return validateResult;
	}

	/**
	 * 团购取消绑定
	 * 
	 * @author iori
	 * @param params
	 *            map中key必须包括:posId[],key.
	 *            posId是字符串数组类型
	 * @return
	 * @throws MD5Exception 
	 * @throws SendPostTimeOutException 
	 * @throws ParseXMLException 
	 */
	public HashMap<String, Object> groupBuyingUnbind(
			HashMap<String, Object> params) throws MD5Exception, ParseXMLException, SendPostTimeOutException {
		// 根据QQ接口需要 ,封装POST参数
		HashMap<String, Object> postParams = new HashMap<String, Object>();
		String[] posIds = (String[]) params.get("posId");
		StringBuffer posId = new StringBuffer();
		List<String> posIdList = new ArrayList<String>();
		if (posIds != null && posIds.length > 0) {
			for (String s : posIds) {
				posId.append("&posId[]=");
				posId.append(s);
				posIdList.add(s);
			}
		}
		String posIdStr = posId.length() > 1 ? posId.toString().substring(1) : "";
		// post参数:posId
		postParams.put("posId[]", posIdList);
		StringBuffer sb = new StringBuffer(posIdStr);
		sb.append("&key=");
		sb.append(params.get("key"));
		// post参数中,sign需要MD5加密
		postParams.put("sign", GroupBuyingUtil.MD5(sb.toString()));
		// 发送POST请求，并得到返回数据
		String url = config.getProperty("qq.groupbuy.url.groupBuyingUnbindPosUrl");
		HashMap<String, Object> unbindResult = GroupBuyingUtil.parseXML(
				GroupBuyingUtil.sendPost(url, postParams), "//groupon/item",
				GroupBuyingUnbindVO.class);
		return unbindResult;
	}
}
