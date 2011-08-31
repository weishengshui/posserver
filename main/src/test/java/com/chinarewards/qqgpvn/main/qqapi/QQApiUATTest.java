package com.chinarewards.qqgpvn.main.qqapi;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Query;

import org.codehaus.jackson.JsonGenerationException;
import org.junit.Test;

import com.chinarewards.qqgbpvn.main.test.JpaGuiceTest;
import com.chinarewards.qqgbvpn.config.DatabaseProperties;
import com.chinarewards.qqgbvpn.config.PosNetworkProperties;
import com.chinarewards.qqgbvpn.domain.GrouponCache;
import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.domain.event.DomainEvent;
import com.chinarewards.qqgbvpn.main.dao.qqapi.GroupBuyingDao;
import com.chinarewards.qqgbvpn.main.exception.CopyPropertiesException;
import com.chinarewards.qqgbvpn.main.exception.SaveDBException;
import com.chinarewards.qqgbvpn.main.guice.AppModule;
import com.chinarewards.qqgbvpn.main.logic.qqapi.GroupBuyingManager;
import com.chinarewards.qqgbvpn.qqapi.exception.MD5Exception;
import com.chinarewards.qqgbvpn.qqapi.exception.ParseXMLException;
import com.chinarewards.qqgbvpn.qqapi.exception.SendPostTimeOutException;
import com.chinarewards.qqgbvpn.qqapi.util.GroupBuyingUtil;
import com.chinarewards.qqgbvpn.qqapi.vo.GroupBuyingSearchListVO;
import com.chinarewards.qqgbvpn.qqapi.vo.GroupBuyingUnbindVO;
import com.chinarewards.qqgbvpn.qqapi.vo.GroupBuyingValidateResultVO;
import com.google.inject.Module;
import com.google.inject.persist.jpa.JpaPersistModule;

public class QQApiUATTest extends JpaGuiceTest {
	
	@Override
	protected Module[] getModules() {
		return new Module[] {
				new AppModule(),
				new JpaPersistModule("posnet")
						.properties(new DatabaseProperties().getProperties()) };
	}
	

	//@Test
	public void testSendPostSuccess() throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		String posId = "REWARDS-0001";
		params.put("posId", posId);
		params.put("key", new PosNetworkProperties().getTxServerKey());
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
		
		System.out.println("posId-->" + postParams.get("posId"));
		System.out.println("key-->" + params.get("key"));
		System.out.println("sign-->" + postParams.get("sign"));
		
		String url = "http://121.14.96.114/api/pos/query";
		
		GroupBuyingUtil.sendPost(url, postParams);
	}

	//@Test
	public void testGroupBuyingSearchParseXML() throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		String posId = "REWARDS-0001";
		params.put("posId", posId);
		params.put("key", new PosNetworkProperties().getTxServerKey());
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
		
		String url = "http://121.14.96.114/api/pos/query";
		
		HashMap<String, Object> result = GroupBuyingUtil.parseXML(
				GroupBuyingUtil.sendPost(url, postParams), "//groupon/item",
				GroupBuyingSearchListVO.class);
		String resultCode = (String) result.get("resultCode");
		System.out.println("resultCode->" + resultCode);
		if ("0".equals(resultCode)) {
			List<GroupBuyingSearchListVO> items = (List<GroupBuyingSearchListVO>) result
					.get("items");
			for (GroupBuyingSearchListVO item : items) {
				System.out.println("GrouponId: " + item.getGrouponId());
				System.out.println("GrouponName: " + item.getGrouponName());
				System.out.println("MercName: " + item.getMercName());
				System.out.println("ListName: " + item.getListName());
				System.out.println("DetailName: " + item.getDetailName());
			}
		} else {
			switch (Integer.valueOf(resultCode)) {
			case -1:
				System.out.println("服务器繁忙");
				break;
			case -2:
				System.out.println("md5校验失败");
				break;
			case -3:
				System.out.println("没有权限");
				break;
			default:
				System.out.println("未知错误");
				break;
			}
		}
	}
	
	//@Test
	public void testGroupBuyingValidateParseXML() throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("posId", "REWARDS-0001");
		params.put("grouponId", "136453");
		params.put("token", "4662451047");
		params.put("key", new PosNetworkProperties().getTxServerKey());
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
		
		String url = "http://121.14.96.114/api/pos/verify";
		
		HashMap<String, Object> result = GroupBuyingUtil.parseXML(
				GroupBuyingUtil.sendPost(url, postParams), "//groupon",
				GroupBuyingValidateResultVO.class);
		String resultCode = (String) result.get("resultCode");
		System.out.println("resultCode->" + resultCode);
		if ("0".equals(resultCode)) {
			List<GroupBuyingValidateResultVO> items = (List<GroupBuyingValidateResultVO>) result
					.get("items");
			for (GroupBuyingValidateResultVO item : items) {
				System.out.println("ResultStatus: " + item.getResultStatus());
				System.out.println("ResultName: " + item.getResultName());
				System.out.println("ResultExplain: " + item.getResultExplain());
				System.out.println("CurrentTime: " + item.getCurrentTime());
				System.out.println("UseTime: " + item.getUseTime());
				System.out.println("ValidTime: " + item.getValidTime());
				System.out.println("RefundTime: " + item.getRefundTime());
			}
		} else {
			switch (Integer.valueOf(resultCode)) {
			case -1:
				System.out.println("服务器繁忙");
				break;
			case -2:
				System.out.println("md5校验失败");
				break;
			case -3:
				System.out.println("没有权限");
				break;
			default:
				System.out.println("未知错误");
				break;
			}
		}
	}
	
	//@Test
	public void testGroupBuyingUnbindParseXML() throws Exception {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("posId", new String[] { "REWARDS-0001", "REWARDS-0002"});
		params.put("key", new PosNetworkProperties().getTxServerKey());
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
		
		String url = "http://121.14.96.114/api/pos/reset";
		
		HashMap<String, Object> result = GroupBuyingUtil.parseXML(
				GroupBuyingUtil.sendPost(url, postParams), "//groupon/item",
				GroupBuyingUnbindVO.class);
		String resultCode = (String) result.get("resultCode");
		System.out.println("resultCode->" + resultCode);
		if ("0".equals(resultCode)) {
			List<GroupBuyingUnbindVO> items = (List<GroupBuyingUnbindVO>) result
					.get("items");
			for (GroupBuyingUnbindVO item : items) {
				System.out.println("PosId: " + item.getPosId());
				System.out.println("ResultStatus: " + item.getResultStatus());
			}
		} else {
			switch (Integer.valueOf(resultCode)) {
			case -1:
				System.out.println("服务器繁忙");
				break;
			case -2:
				System.out.println("md5校验失败");
				break;
			case -3:
				System.out.println("没有权限");
				break;
			default:
				System.out.println("未知错误");
				break;
			}
		}
	}

	//@Test
	public void testInitGrouponCache() {
		
		GroupBuyingManager gbm = getInjector().getInstance(
				GroupBuyingManager.class);
		GroupBuyingDao dao = getInjector().getInstance(
				GroupBuyingDao.class);
		HashMap<String, String> params = new HashMap<String, String>();
		String posId = "REWARDS-0001";
		params.put("posId", posId);
		params.put("key", new PosNetworkProperties().getTxServerKey());
		System.out.println("key-->" + params.get("key"));
		try {
			String resultCode = gbm.initGrouponCache(params);
			System.out.println("resultCode--> " + resultCode);
			//search groupon cache
			PageInfo<GrouponCache> pageInfo = new PageInfo();
			pageInfo.setPageId(1);
			pageInfo.setPageSize(50);
			List<GrouponCache> cacheList = dao.getGrouponCachePagination(pageInfo, posId).getItems();
			if (cacheList != null && cacheList.size() > 0) {
				for (int i = 0; i < cacheList.size(); i++) {
					System.out.println("GrouponId-->" + cacheList.get(i).getGrouponId());
					System.out.println("GrouponName-->" + cacheList.get(i).getGrouponName());
					System.out.println("MercName-->" + cacheList.get(i).getMercName());
					System.out.println("ListName-->" + cacheList.get(i).getListName());
					System.out.println("DetailName-->" + cacheList.get(i).getDetailName());
				}
			}
			//check cache
			Query jql = emp.get().createQuery(
					"select j.eventDetail from Journal j where j.event = '"
							+ DomainEvent.GROUPON_CACHE_INIT.toString()
							+ "' and j.entityId = ?1 order by j.ts desc");
			jql.setParameter(1, posId);
			Object o = jql.getResultList().get(0);
			String result = o != null ? (String) o : "";
			System.out.println("result--->" + result);
			if ("0".equals(resultCode)) {
				assertTrue(result.startsWith("[") || "".equals(result));
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
		} catch (CopyPropertiesException e) {
			System.err.println("复制属性值出错");
			e.printStackTrace();
		}
	}

	//@Test
	public void testGroupBuyingSearch() {
		
		GroupBuyingManager gbm = getInjector().getInstance(
				GroupBuyingManager.class);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("posId", "REWARDS-0001");
		params.put("curpage", "6");
		params.put("pageSize", "1");
		try {
			HashMap<String,Object> resultMap = gbm.groupBuyingSearch(params);
			String resultCode = (String) resultMap.get("resultCode");
			System.out.println("resultCode->" + resultCode);
			PageInfo<GrouponCache> pageInfo = (PageInfo<GrouponCache>) resultMap.get("pageInfo");
			System.out.println("totalnum->" + pageInfo.getRecordCount());
			if (pageInfo.getItems() != null) {
				System.out.println("curnum->" + pageInfo.getItems().size());
			} else {
				System.out.println("curnum->" + 0);
			}
			System.out.println("curpage->" + pageInfo.getPageId());
			System.out.println("totalpage->" + pageInfo.getPageCount());
			if (pageInfo.getItems() != null && pageInfo.getItems().size() > 0) {
				for (GrouponCache item : pageInfo.getItems()) {
					System.out.println("GrouponId-->" + item.getGrouponId());
					System.out.println("GrouponName-->" + item.getGrouponName());
					System.out.println("MercName-->" + item.getMercName());
					System.out.println("ListName-->" + item.getListName());
					System.out.println("DetailName-->" + item.getDetailName());
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

	//@Test
	public void testGroupBuyingValidate() {
		
		GroupBuyingManager gbm = getInjector().getInstance(
				GroupBuyingManager.class);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("posId", "REWARDS-0001");
		params.put("grouponId", "136453");
		params.put("token", "6638856966");
		params.put("key", new PosNetworkProperties().getTxServerKey());
		try {
			HashMap<String, Object> result = gbm.groupBuyingValidate(params);
			String resultCode = (String) result.get("resultCode");
			System.out.println("resultCode->" + resultCode);
			if ("0".equals(resultCode)) {
				List<GroupBuyingValidateResultVO> items = (List<GroupBuyingValidateResultVO>) result
						.get("items");
				for (GroupBuyingValidateResultVO item : items) {
					System.out.println("ResultStatus: " + item.getResultStatus());
					System.out.println("ResultName: " + item.getResultName());
					System.out.println("ResultExplain: " + item.getResultExplain());
					System.out.println("CurrentTime: " + item.getCurrentTime());
					System.out.println("UseTime: " + item.getUseTime());
					System.out.println("ValidTime: " + item.getValidTime());
					System.out.println("RefundTime: " + item.getRefundTime());
				}
			} else {
				switch (Integer.valueOf(resultCode)) {
				case -1:
					System.out.println("服务器繁忙");
					break;
				case -2:
					System.out.println("md5校验失败");
					break;
				case -3:
					System.out.println("没有权限");
					break;
				default:
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

	//@Test
	public void testGroupBuyingUnbind() {
		
		GroupBuyingManager gbm = getInjector().getInstance(
				GroupBuyingManager.class);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("posId", new String[] { "rEWARDS-0001", "rEWARDS-0002"});
		params.put("key", new PosNetworkProperties().getTxServerKey());
		try {
			HashMap<String, Object> result = gbm.groupBuyingUnbind(params);
			String resultCode = (String) result.get("resultCode");
			System.out.println("resultCode->" + resultCode);
			if ("0".equals(resultCode)) {
				List<GroupBuyingUnbindVO> items = (List<GroupBuyingUnbindVO>) result
						.get("items");
				for (GroupBuyingUnbindVO item : items) {
					System.out.println("PosId: " + item.getPosId());
					System.out.println("ResultStatus: " + item.getResultStatus());
				}
			} else {
				switch (Integer.valueOf(resultCode)) {
				case -1:
					System.out.println("服务器繁忙");
					break;
				case -2:
					System.out.println("md5校验失败");
					break;
				case -3:
					System.out.println("没有权限");
					break;
				default:
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
