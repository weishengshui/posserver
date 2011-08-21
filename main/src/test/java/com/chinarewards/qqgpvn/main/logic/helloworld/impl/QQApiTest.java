package com.chinarewards.qqgpvn.main.logic.helloworld.impl;

import org.junit.Test;

import com.chinarewards.qqgbvpn.main.ApplicationModule;
import com.chinarewards.qqgbvpn.main.logic.qqapi.GroupBuyingManager;
import com.chinarewards.qqgpvn.main.test.GuiceTest;
import com.google.inject.Module;

public class QQApiTest extends GuiceTest {

	@Override
	protected Module[] getModules() {
		return new Module[] { new ApplicationModule() };
	}

	@Test
	public void testValidate() throws Exception {
		GroupBuyingManager g = getInjector().getInstance(
				GroupBuyingManager.class);
		g.groupBuyingSearch(null);
	}
}
