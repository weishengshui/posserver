package com.chinarewards.qqgbvpn.mgmtui.helloworld;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.codehaus.jackson.JsonGenerationException;

import com.chinarewards.qqgbvpn.config.PosNetworkProperties;
import com.chinarewards.qqgbvpn.domain.Agent;
import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.ReturnNote;
import com.chinarewards.qqgbvpn.mgmtui.exception.SaveDBException;
import com.chinarewards.qqgbvpn.mgmtui.guice.HardCodedConfigModule;
import com.chinarewards.qqgbvpn.mgmtui.guice.QQApiModule;
import com.chinarewards.qqgbvpn.mgmtui.logic.GroupBuyingUnbindManager;
import com.chinarewards.qqgbvpn.qqapi.exception.MD5Exception;
import com.chinarewards.qqgbvpn.qqapi.exception.ParseXMLException;
import com.chinarewards.qqgbvpn.qqapi.exception.SendPostTimeOutException;
import com.chinarewards.qqgbvpn.qqapi.vo.GroupBuyingUnbindVO;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;

public class GroupBuyingUnbindTest {
	
	private Injector injector;
	
	public static void main(String[] args) {
		GroupBuyingUnbindTest t = new GroupBuyingUnbindTest();
		t.setInjector();
		//t.testGroupBuyingUnbind();
		//t.testGetPosByAgentId();
		//t.testGetAgentByName();
		//t.testGetPosByPosInfo();
		//t.testCreateReturnNoteByAgentId();
		//t.testConfirmReturnNote();
	}
	
	public void setInjector() {
		Module[] modules =new Module[] {new QQApiModule()
		,new JpaPersistModule("posnet").properties(getJPAProperties())
		,new HardCodedConfigModule()};
		injector = Guice.createInjector(modules);
		PersistService ps = injector.getInstance(PersistService.class);
		ps.start();
		
	}
	
	protected Properties getJPAProperties() {
		Properties properties = new Properties();
		properties.put("javax.persistence.transactionType", "RESOURCE_LOCAL");

		properties.put("hibernate.hbm2ddl.auto", "update");
		properties.put("hibernate.connection.driver_class",
				"com.mysql.jdbc.Driver");
		properties.put("hibernate.connection.username", "root");
		properties.put("hibernate.connection.password", "123456");
		properties.put("hibernate.connection.url",
				"jdbc:mysql://localhost:3306/qqapi");
		properties.put("hibernate.dialect",
				"org.hibernate.dialect.MySQL5Dialect");
		properties.put("hibernate.show_sql", "true");
		return properties;
	}
	
	public void testGroupBuyingUnbind() {
		
		GroupBuyingUnbindManager gbm = injector.getInstance(GroupBuyingUnbindManager.class);
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("posId", new String[] { "REWARDS-000112"});
		params.put("key", new PosNetworkProperties().getTxServerKey());
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
	
	public void testGetPosByAgentId() {
		GroupBuyingUnbindManager gbm = injector.getInstance(GroupBuyingUnbindManager.class);
		
		PageInfo pageInfo = new PageInfo();
		pageInfo.setPageId(1);
		pageInfo.setPageSize(10);
		String agentId = "40e6bda2-d37b-11e0-b051-f0095cb162d9";
		pageInfo = gbm.getPosByAgentId(pageInfo, agentId);
		System.out.println("pageCount: " + pageInfo.getPageCount());
		List<Pos> posList = pageInfo.getItems();
		if (posList != null && posList.size() > 0) {
			for (Pos p : posList) {
				System.out.println("PosId: " + p.getPosId());
			}
		} else {
			System.out.println("Pos not found!");
		}
	}
	
	public void testGetAgentByName() {
		GroupBuyingUnbindManager gbm = injector.getInstance(GroupBuyingUnbindManager.class);
		
		String agentName = "代理商A";
		Agent agent = gbm.getAgentByName(agentName);
		if (agent != null) {
			System.out.println("agentId: " + agent.getId());
			System.out.println("agentName: " + agent.getName());
		} else {
			System.out.println("Agent not found!");
		}
	}
	
	public void testGetPosByPosInfo() {
		GroupBuyingUnbindManager gbm = injector.getInstance(GroupBuyingUnbindManager.class);
		
		String info = "possn333333";
		List<Pos> posList = gbm.getPosByPosInfo(info);
		if (posList != null && posList.size() > 0) {
			for (Pos p : posList) {
				System.out.println("PosId: " + p.getPosId());
			}
		} else {
			System.out.println("Pos not found!");
		}
	}
	
	public void testCreateReturnNoteByAgentId() {
		GroupBuyingUnbindManager gbm = injector.getInstance(GroupBuyingUnbindManager.class);
		
		String agentId = "40e6bda2-d37b-11e0-b051-f0095cb162d9";
		try {
			ReturnNote rn = gbm.createReturnNoteByAgentId(agentId);
			if (rn != null) {
				System.out.println("ReturnNote Id: " + rn.getId());
				System.out.println("ReturnNote Status: " + rn.getStatus());
			} else {
				System.out.println("Agent not found!");
			}
		} catch (JsonGenerationException e) {
			System.err.println("生成JSON对象出错");
			e.printStackTrace();
		} catch (SaveDBException e) {
			System.err.println("后台保存数据库出错");
			System.out.println("具体异常信息：" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/*public void testConfirmReturnNote() {
		EntityManager em = injector.getInstance(EntityManager.class);
		GroupBuyingUnbindManager gbm = injector.getInstance(GroupBuyingUnbindManager.class);
		
		String agentId = "40eec751-d37b-11e0-b051-f0095cb162d9";
		String rnId = "40288402322931710132293173720001";
		List<Pos> posList = new ArrayList<Pos>();
		Pos p = em.find(Pos.class, "bb5222c9-d37c-11e0-b051-f0095cb162d9");
		posList.add(p);
		try {
			ReturnNote rn = gbm.confirmReturnNote(agentId,rnId,posList);
			if (rn != null) {
				System.out.println("ReturnNote Id: " + rn.getId());
				System.out.println("ReturnNote Status: " + rn.getStatus());
			} else {
				System.out.println("Agent not found!");
			}
		} catch (JsonGenerationException e) {
			System.err.println("生成JSON对象出错");
			e.printStackTrace();
		} catch (SaveDBException e) {
			System.err.println("后台保存数据库出错");
			System.out.println("具体异常信息：" + e.getMessage());
			e.printStackTrace();
		}
	}*/
}
