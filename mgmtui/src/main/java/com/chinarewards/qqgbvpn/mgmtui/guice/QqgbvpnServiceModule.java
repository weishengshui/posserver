/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.guice;

import com.chinarewards.qqgbvpn.mgmtui.dao.pos.PosDao;
import com.chinarewards.qqgbvpn.mgmtui.dao.pos.impl.PosDaoImpl;
import com.chinarewards.qqgbvpn.mgmtui.logic.pos.PosLogic;
import com.chinarewards.qqgbvpn.mgmtui.logic.pos.impl.PosLogicImpl;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

/**
 * 
 * 
 * @author huangwei
 *
 */
public class QqgbvpnServiceModule extends AbstractModule {

	protected void configure() {


		bind(PosDao.class).to(PosDaoImpl.class).in(Singleton.class);
		bind(PosLogic.class).to(PosLogicImpl.class).in(Singleton.class);
	}

}
