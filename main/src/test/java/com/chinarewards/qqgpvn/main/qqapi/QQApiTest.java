package com.chinarewards.qqgpvn.main.qqapi;

import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.junit.Test;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.ServletHolder;

import com.chinarewards.qqgbvpn.config.DatabaseProperties;
import com.chinarewards.qqgbvpn.main.QQApiModule;
import com.chinarewards.qqgbvpn.main.exception.SaveDBException;
import com.chinarewards.qqgbvpn.main.logic.qqapi.GroupBuyingManager;
import com.chinarewards.qqgbvpn.main.logic.qqapi.impl.HardCodedServlet;
import com.chinarewards.qqgbvpn.qqapi.exception.MD5Exception;
import com.chinarewards.qqgbvpn.qqapi.exception.ParseXMLException;
import com.chinarewards.qqgbvpn.qqapi.exception.SendPostTimeOutException;
import com.chinarewards.qqgbvpn.qqapi.util.GroupBuyingUtil;
import com.chinarewards.qqgbvpn.qqapi.vo.GroupBuyingSearchListVO;
import com.chinarewards.qqgbvpn.qqapi.vo.GroupBuyingUnbindVO;
import com.chinarewards.qqgbvpn.qqapi.vo.GroupBuyingValidateResultVO;
import com.chinarewards.qqgpvn.main.test.JpaGuiceTest;
import com.google.inject.Module;
import com.google.inject.persist.jpa.JpaPersistModule;

public class QQApiTest extends JpaGuiceTest {

	@Override
	protected Module[] getModules() {
		return new Module[] {
				new QQApiModule(),
				new JpaPersistModule("posnet")
						.properties(new DatabaseProperties().getProperties()) };
	}
	
	

	@Test
	public void testGroupBuyingSearch() {
		//build test server start
		try {
			Server server = new Server(0);

			HardCodedServlet s = new HardCodedServlet();
			s.init();
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"GBK\" ?>");
			sb.append("<tuan>");
			sb.append("<resultCode>0</resultCode>");
			sb.append("<groupon>");
			sb.append("<item>");
			sb.append("<grouponId>132127</grouponId>");
			sb.append("<grouponName>400.01元套餐POST</grouponName>");
			sb.append("<mercName>三人行骨头王ggggg</mercName>");
			sb.append("<listName>40元套餐\r\n        (132127)</listName>");
			sb.append("<detailName>1111111111111140元套餐(132127)</detailName>");
			sb.append("</item>");
			sb.append("<item>");
			sb.append("<grouponId>132123</grouponId>");
			sb.append("<grouponName>400.01元套餐</grouponName>");
			sb.append("<mercName>三人行骨头王</mercName>");
			sb.append("<listName>400.01元套餐\r\n        (132123)</listName>");
			sb.append("<detailName>400.01元套餐</detailName>");
			sb.append("</item>");
			sb.append("</groupon>");
			sb.append("</tuan>");
			s.setResponse(new String(sb.toString().getBytes("utf-8"), "iso-8859-1"));
			
			ServletHolder h = new ServletHolder();
			h.setServlet(s);

			String servletPath = "/qqapi";
			ServletHandler scHandler = new ServletHandler();
			scHandler.addServletWithMapping(h, servletPath);
			
			// add handler to server
			server.addHandler(scHandler);
			server.getConnectors()[0].setPort(8086);
			server.start();
		} catch (Exception e) {
			System.err.println("build test server failed");
		}
		//build test server end
		
		GroupBuyingManager gbm = getInjector().getInstance(
				GroupBuyingManager.class);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("posId", "rewards-0001");
		params.put("key", "456789000");
		params.put("curpage", "1");
		try {
			HashMap<String, Object> result = gbm.groupBuyingSearch(params);
			String resultCode = (String) result.get("resultCode");
			String curpage = (String) result.get("curpage");
			String totalpage = String.valueOf(result.get("totalpage"));
			String totalnum = String.valueOf(result.get("totalnum"));
			String curnum = String.valueOf(result.get("curnum"));
			System.out.println("resultCode->" + resultCode);
			System.out.println("totalnum->" + totalnum);
			System.out.println("curnum->" + curnum);
			System.out.println("curpage->" + curpage);
			System.out.println("totalpage->" + totalpage);
			if ("0".equals(resultCode)) {
				List<GroupBuyingSearchListVO> items = (List<GroupBuyingSearchListVO>) result
						.get("items");
				for (GroupBuyingSearchListVO item : items) {
					System.out.println(item.getGrouponId());
					System.out.println(item.getGrouponName());
					System.out.println(item.getMercName());
					System.out.println(item.getListName());
					System.out.println(item.getDetailName());
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

	@Test
	public void testGroupBuyingValidate() {
		
		//build test server start
		try {
			Server server = new Server(0);

			HardCodedServlet s = new HardCodedServlet();
			s.init();
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"GBK\" ?>");
			sb.append("<tuan>");
			sb.append("<resultCode>0</resultCode>");
			sb.append("<groupon>");
			sb.append("<resultName>验证已成功</resultName>");
			sb.append("<resultExplain>验证成功于\r\n08.03 11:10:23</resultExplain>");
			sb.append("<currentTime>2011-08-03 11:10:23</currentTime>");
			sb.append("<useTime>2011-08-03 11:10:23</useTime>");
			sb.append("<validTime>2011-08-10</validTime>");
			sb.append("</groupon>");
			sb.append("<groupon>");
			sb.append("<resultName>已退款</resultName>");
			sb.append("<resultExplain>验证已退款于\r\n08.03 11:10:23</resultExplain>");
			sb.append("<currentTime>2011-08-03 11:10:23</currentTime>");
			sb.append("<validTime>2011-08-10</validTime>");
			sb.append("<refundTime>2011-08-03 11:10:23</refundTime>");
			sb.append("</groupon>");
			sb.append("</tuan>");
			s.setResponse(new String(sb.toString().getBytes("utf-8"), "iso-8859-1"));
			
			ServletHolder h = new ServletHolder();
			h.setServlet(s);

			String servletPath = "/qqapi";
			ServletHandler scHandler = new ServletHandler();
			scHandler.addServletWithMapping(h, servletPath);
			
			// add handler to server
			server.addHandler(scHandler);
			server.getConnectors()[0].setPort(8087);
			server.start();
		} catch (Exception e) {
			System.err.println("build test server failed");
		}
		//build test server end
		
		GroupBuyingManager gbm = getInjector().getInstance(
				GroupBuyingManager.class);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("posId", "rewards-0001");
		params.put("grouponId", "456789");
		params.put("token", "4567890");
		params.put("key", "456789000");
		try {
			HashMap<String, Object> result = gbm.groupBuyingValidate(params);
			String resultCode = (String) result.get("resultCode");
			System.out.println("resultCode->" + resultCode);
			if ("0".equals(resultCode)) {
				List<GroupBuyingValidateResultVO> items = (List<GroupBuyingValidateResultVO>) result
						.get("items");
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

	@Test
	public void testGroupBuyingUnbind() {
		
		//build test server start
		try {
			Server server = new Server(0);

			HardCodedServlet s = new HardCodedServlet();
			s.init();
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"GBK\" ?>");
			sb.append("<tuan>");
			sb.append("<resultCode>0</resultCode>");
			sb.append("<groupon>");
			sb.append("<item>");
			sb.append("<posId>1</posId>");
			sb.append("<resultStatus>0</resultStatus>");
			sb.append("</item>");
			sb.append("<item>");
			sb.append("<posId>2</posId>");
			sb.append("<resultStatus>1</resultStatus>");
			sb.append("</item>");
			sb.append("</groupon>");
			sb.append("</tuan>");
			s.setResponse(new String(sb.toString().getBytes("utf-8"), "iso-8859-1"));
			
			ServletHolder h = new ServletHolder();
			h.setServlet(s);

			String servletPath = "/qqapi";
			ServletHandler scHandler = new ServletHandler();
			scHandler.addServletWithMapping(h, servletPath);
			
			// add handler to server
			server.addHandler(scHandler);
			server.getConnectors()[0].setPort(8088);
			server.start();
		} catch (Exception e) {
			System.err.println("build test server failed");
		}
		//build test server end
		
		GroupBuyingManager gbm = getInjector().getInstance(
				GroupBuyingManager.class);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("posId", new String[] { "rewards-0001", "rewards-0002"});
		params.put("key", "456789000");
		try {
			HashMap<String, Object> result = gbm.groupBuyingUnbind(params);
			String resultCode = (String) result.get("resultCode");
			System.out.println("resultCode->" + resultCode);
			if ("0".equals(resultCode)) {
				List<GroupBuyingUnbindVO> items = (List<GroupBuyingUnbindVO>) result
						.get("items");
				for (GroupBuyingUnbindVO item : items) {
					System.out.println(item.getPosId());
					System.out.println(item.getResultStatus());
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
	
	@Test
	public void testSendPostSuccess() throws Exception {
		Server server = new Server(0);

		HardCodedServlet s = new HardCodedServlet();
		s.init();
		s.setResponse("correct url");
		
		ServletHolder h = new ServletHolder();
		h.setServlet(s);

		String servletPath = "/qqapi";
		ServletHandler scHandler = new ServletHandler();
		scHandler.addServletWithMapping(h, servletPath);
		
		// add handler to server
		server.addHandler(scHandler);
		server.start();
		
		String url = "http://localhost:" + server.getConnectors()[0].getLocalPort() + servletPath;
		
		GroupBuyingUtil.sendPost(url, null);
	}

	@Test
	public void testSendPostTimeOut() throws Exception {
		Server server = new Server(0);

		HardCodedServlet s = new HardCodedServlet();
		s.init();
		s.setResponse("error url");
		
		ServletHolder h = new ServletHolder();
		h.setServlet(s);

		String servletPath = "/qqapi";
		ServletHandler scHandler = new ServletHandler();
		scHandler.addServletWithMapping(h, servletPath);
		
		// add handler to server
		server.addHandler(scHandler);
		server.start();
		
		String url = "http://errorurl:" + server.getConnectors()[0].getLocalPort() + servletPath;
		
		try {
			GroupBuyingUtil.sendPost(url, null);
		} catch (SendPostTimeOutException e) {
			System.out.println("connection time out");
			return;
		}
		throw new Exception();
	}
	
	@Test
	public void testErrorXML() throws Exception {
		Server server = new Server(0);

		HardCodedServlet s = new HardCodedServlet();
		s.init();
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"GBK\" ?>");
		sb.append("<tuan>");
		sb.append("<resultCode>0</resultCode>");
		sb.append("<groupon>");
		sb.append("<item>");
		sb.append("<grouponId>132127</grouponId>");
		sb.append("<grouponName>400.01元套餐POST</grouponName>");
		sb.append("<mercName>三人行骨头王</mercName>");
		sb.append("<listName>40元套餐\r\n        (132127)</listName>");
		sb.append("<detailName>1111111111111140元套餐(132127)</detailName>");
		sb.append("</item>");
		sb.append("<item>");
		sb.append("<grouponId>132123</grouponId>");
		sb.append("<grouponName>400.01元套餐</grouponName>");
		sb.append("<mercName>三人行骨头王</mercName>");
		sb.append("<listName>400.01元套餐\r\n        (132123)</listName>");
		sb.append("<detailName>400.01元套餐</detailName>");
		sb.append("</item>");
		s.setResponse(new String(sb.toString().getBytes("utf-8"), "iso-8859-1"));
		
		ServletHolder h = new ServletHolder();
		h.setServlet(s);

		String servletPath = "/qqapi";
		ServletHandler scHandler = new ServletHandler();
		scHandler.addServletWithMapping(h, servletPath);
		
		// add handler to server
		server.addHandler(scHandler);
		server.start();
		
		String url = "http://localhost:" + server.getConnectors()[0].getLocalPort() + servletPath;
		
		try {
			HashMap<String, Object> result = GroupBuyingUtil.parseXML(
					GroupBuyingUtil.sendPost(url, null), "//groupon/item",
					GroupBuyingSearchListVO.class);
			String resultCode = (String) result.get("resultCode");
			System.out.println("resultCode->" + resultCode);
			if ("0".equals(resultCode)) {
				List<GroupBuyingSearchListVO> items = (List<GroupBuyingSearchListVO>) result
						.get("items");
				for (GroupBuyingSearchListVO item : items) {
					System.out.println(item.getGrouponId());
					System.out.println(item.getGrouponName());
					System.out.println(item.getMercName());
					System.out.println(item.getListName());
					System.out.println(item.getDetailName());
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
		} catch (ParseXMLException e) {
			System.out.println("xml contents format error");
			return;
		}
		throw new Exception();
		
	}
	
	@Test
	public void testGroupBuyingSearchParseXML() throws Exception {
		Server server = new Server(0);

		HardCodedServlet s = new HardCodedServlet();
		s.init();
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"GBK\" ?>");
		sb.append("<tuan>");
		sb.append("<resultCode>0</resultCode>");
		sb.append("<groupon>");
		sb.append("<item>");
		sb.append("<grouponId>132127</grouponId>");
		sb.append("<grouponName>400.01元套餐POST</grouponName>");
		sb.append("<mercName>三人行骨头王</mercName>");
		sb.append("<listName>40元套餐\r\n        (132127)</listName>");
		sb.append("<detailName>1111111111111140元套餐(132127)</detailName>");
		sb.append("</item>");
		sb.append("<item>");
		sb.append("<grouponId>132123</grouponId>");
		sb.append("<grouponName>400.01元套餐</grouponName>");
		sb.append("<mercName>三人行骨头王</mercName>");
		sb.append("<listName>400.01元套餐\r\n        (132123)</listName>");
		sb.append("<detailName>400.01元套餐</detailName>");
		sb.append("</item>");
		sb.append("</groupon>");
		sb.append("</tuan>");
		s.setResponse(new String(sb.toString().getBytes("utf-8"), "iso-8859-1"));
		
		ServletHolder h = new ServletHolder();
		h.setServlet(s);

		String servletPath = "/qqapi";
		ServletHandler scHandler = new ServletHandler();
		scHandler.addServletWithMapping(h, servletPath);
		
		// add handler to server
		server.addHandler(scHandler);
		server.start();
		
		String url = "http://localhost:" + server.getConnectors()[0].getLocalPort() + servletPath;
		
		HashMap<String, Object> result = GroupBuyingUtil.parseXML(
				GroupBuyingUtil.sendPost(url, null), "//groupon/item",
				GroupBuyingSearchListVO.class);
		String resultCode = (String) result.get("resultCode");
		System.out.println("resultCode->" + resultCode);
		if ("0".equals(resultCode)) {
			List<GroupBuyingSearchListVO> items = (List<GroupBuyingSearchListVO>) result
					.get("items");
			for (GroupBuyingSearchListVO item : items) {
				System.out.println(item.getGrouponId());
				System.out.println(item.getGrouponName());
				System.out.println(item.getMercName());
				System.out.println(item.getListName());
				System.out.println(item.getDetailName());
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
	
	@Test
	public void testGroupBuyingValidateParseXML() throws Exception {
		Server server = new Server(0);

		HardCodedServlet s = new HardCodedServlet();
		s.init();
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"GBK\" ?>");
		sb.append("<tuan>");
		sb.append("<resultCode>0</resultCode>");
		sb.append("<groupon>");
		sb.append("<resultName>验证已成功</resultName>");
		sb.append("<resultExplain>验证成功于\r\n08.03 11:10:23</resultExplain>");
		sb.append("<currentTime>2011-08-03 11:10:23</currentTime>");
		sb.append("<useTime>2011-08-03 11:10:23</useTime>");
		sb.append("<validTime>2011-08-10</validTime>");
		sb.append("</groupon>");
		sb.append("<groupon>");
		sb.append("<resultName>已退款</resultName>");
		sb.append("<resultExplain>验证已退款于\r\n08.03 11:10:23</resultExplain>");
		sb.append("<currentTime>2011-08-03 11:10:23</currentTime>");
		sb.append("<validTime>2011-08-10</validTime>");
		sb.append("<refundTime>2011-08-03 11:10:23</refundTime>");
		sb.append("</groupon>");
		sb.append("</tuan>");
		s.setResponse(new String(sb.toString().getBytes("utf-8"), "iso-8859-1"));
		
		ServletHolder h = new ServletHolder();
		h.setServlet(s);

		String servletPath = "/qqapi";
		ServletHandler scHandler = new ServletHandler();
		scHandler.addServletWithMapping(h, servletPath);
		
		// add handler to server
		server.addHandler(scHandler);
		//server.getConnectors()[0].setPort(8087);
		server.start();
		
		System.out.println("LocalPort: " + server.getConnectors()[0].getLocalPort());
		
		String url = "http://localhost:" + server.getConnectors()[0].getLocalPort() + servletPath;
		
		HashMap<String, Object> result = GroupBuyingUtil.parseXML(
				GroupBuyingUtil.sendPost(url, null), "//groupon",
				GroupBuyingValidateResultVO.class);
		String resultCode = (String) result.get("resultCode");
		System.out.println("resultCode->" + resultCode);
		if ("0".equals(resultCode)) {
			List<GroupBuyingValidateResultVO> items = (List<GroupBuyingValidateResultVO>) result
					.get("items");
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
	
	@Test
	public void testGroupBuyingUnbindParseXML() throws Exception {
		Server server = new Server(0);

		HardCodedServlet s = new HardCodedServlet();
		s.init();
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"GBK\" ?>");
		sb.append("<tuan>");
		sb.append("<resultCode>0</resultCode>");
		sb.append("<groupon>");
		sb.append("<item>");
		sb.append("<posId>1</posId>");
		sb.append("<resultStatus>0</resultStatus>");
		sb.append("</item>");
		sb.append("<item>");
		sb.append("<posId>2</posId>");
		sb.append("<resultStatus>1</resultStatus>");
		sb.append("</item>");
		sb.append("</groupon>");
		sb.append("</tuan>");
		s.setResponse(new String(sb.toString().getBytes("utf-8"), "iso-8859-1"));
		
		ServletHolder h = new ServletHolder();
		h.setServlet(s);

		String servletPath = "/qqapi";
		ServletHandler scHandler = new ServletHandler();
		scHandler.addServletWithMapping(h, servletPath);
		
		// add handler to server
		server.addHandler(scHandler);
		server.start();
		
		String url = "http://localhost:" + server.getConnectors()[0].getLocalPort() + servletPath;
		
		HashMap<String, Object> result = GroupBuyingUtil.parseXML(
				GroupBuyingUtil.sendPost(url, null), "//groupon/item",
				GroupBuyingUnbindVO.class);
		String resultCode = (String) result.get("resultCode");
		System.out.println("resultCode->" + resultCode);
		if ("0".equals(resultCode)) {
			List<GroupBuyingUnbindVO> items = (List<GroupBuyingUnbindVO>) result
					.get("items");
			for (GroupBuyingUnbindVO item : items) {
				System.out.println(item.getPosId());
				System.out.println(item.getResultStatus());
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

}
