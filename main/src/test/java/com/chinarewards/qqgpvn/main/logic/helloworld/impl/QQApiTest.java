package com.chinarewards.qqgpvn.main.logic.helloworld.impl;

import java.util.Properties;

import org.junit.Test;

import com.chinarewards.qqgbvpn.config.DatabaseProperties;
import com.chinarewards.qqgbvpn.main.ApplicationModule;
import com.chinarewards.qqgbvpn.main.logic.qqapi.GroupBuyingManager;
import com.chinarewards.qqgpvn.main.test.GuiceTest;
import com.google.inject.Module;
import com.google.inject.persist.jpa.JpaPersistModule;

public class QQApiTest extends GuiceTest {

	@Override
	protected Module[] getModules() {
		JpaPersistModule jpaModule = new JpaPersistModule("posnet");
		DatabaseProperties p = new DatabaseProperties();
		Properties props = p.getProperties();
		jpaModule.properties(props);
		return new Module[] { new ApplicationModule(), jpaModule};
	}

	@Test
	public void testValidate() throws Exception {
		GroupBuyingManager g = getInjector().getInstance(
				GroupBuyingManager.class);
		g.groupBuyingSearch(null);
	}
}
