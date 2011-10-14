package com.chinarewards.qqgpvn.main.huaxia;

import org.apache.commons.configuration.Configuration;
import org.junit.Test;

import com.chinarewards.qqgbpvn.main.CommonTestConfigModule;
import com.chinarewards.qqgbpvn.main.test.JpaGuiceTest;
import com.chinarewards.qqgbvpn.core.jpa.JpaPersistModuleBuilder;
import com.chinarewards.qqgbvpn.main.guice.AppModule;
import com.chinarewards.qqgbvpn.main.logic.huaxia.HuaxiaRedeemManager;
import com.chinarewards.qqgbvpn.main.vo.HuaxiaRedeemVO;
import com.google.inject.Module;
import com.google.inject.persist.jpa.JpaPersistModule;

public class HuaxiaTest extends JpaGuiceTest {
	
	@Override
	protected Module[] getModules() {

		CommonTestConfigModule confModule = new CommonTestConfigModule();
		Configuration configuration = confModule.getConfiguration();

		JpaPersistModule jpaModule = new JpaPersistModule("posnet");
		JpaPersistModuleBuilder builder = new JpaPersistModuleBuilder();
		builder.configModule(jpaModule, configuration, "db");

		return new Module[] { confModule, jpaModule, new AppModule() };
	}

	@Test
	public void testHuaxiaRedeemSearch() {
		HuaxiaRedeemVO params = new HuaxiaRedeemVO();
		params.setCardNum("5767");
		
		HuaxiaRedeemManager gbm = getInjector().getInstance(HuaxiaRedeemManager.class);
		HuaxiaRedeemVO redeemCount = gbm.huaxiaRedeemSearch(params);
		log.debug("redeemCount : {}",redeemCount.getRedeemCount());
	}
	
	@Test
	public void testHuaxiaRedeemConfirm() {
		HuaxiaRedeemVO params = new HuaxiaRedeemVO();
		params.setCardNum("5767");
		params.setPosId("REWARDS-0001");
		
		HuaxiaRedeemManager gbm = getInjector().getInstance(HuaxiaRedeemManager.class);
		int result = gbm.huaxiaRedeemConfirm(params).getResult();
		log.debug("result : {}",result);
	}
	
	@Test
	public void testHuaxiaRedeemAck() {
		HuaxiaRedeemVO params = new HuaxiaRedeemVO();
		params.setCardNum("5767");
		params.setPosId("REWARDS-0001");
		params.setChanceId("7b27511c-f621-11e0-abbd-c61e41ebd879");
		params.setAckId("f37b573f-0379-4fbc-bade-832c574618d5");
		
		HuaxiaRedeemManager gbm = getInjector().getInstance(HuaxiaRedeemManager.class);
		int result = gbm.huaxiaRedeemAck(params).getResult();
		log.debug("result : {}",result);
	}


}
