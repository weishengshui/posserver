/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.guice;

import com.chinarewards.qqgbvpn.mgmtui.adapter.pos.PosAdapter;
import com.chinarewards.qqgbvpn.mgmtui.dao.DeliveryDao;
import com.chinarewards.qqgbvpn.mgmtui.dao.DeliveryDetailDao;
import com.chinarewards.qqgbvpn.mgmtui.dao.SysUserDao;
import com.chinarewards.qqgbvpn.mgmtui.dao.agent.AgentDao;
import com.chinarewards.qqgbvpn.mgmtui.dao.agent.impl.AgentDaoImpl;
import com.chinarewards.qqgbvpn.mgmtui.dao.impl.DeliveryDaoImpl;
import com.chinarewards.qqgbvpn.mgmtui.dao.impl.DeliveryDetailDaoImpl;
import com.chinarewards.qqgbvpn.mgmtui.dao.impl.SysUserDaoImpl;
import com.chinarewards.qqgbvpn.mgmtui.dao.pos.PosDao;
import com.chinarewards.qqgbvpn.mgmtui.dao.pos.impl.PosDaoImpl;
import com.chinarewards.qqgbvpn.mgmtui.logic.agent.AgentLogic;
import com.chinarewards.qqgbvpn.mgmtui.logic.agent.impl.AgentLogicImpl;
import com.chinarewards.qqgbvpn.mgmtui.logic.login.LoginLogic;
import com.chinarewards.qqgbvpn.mgmtui.logic.login.impl.LoginLogicImpl;
import com.chinarewards.qqgbvpn.mgmtui.logic.pos.DeliveryLogic;
import com.chinarewards.qqgbvpn.mgmtui.logic.pos.PosLogic;
import com.chinarewards.qqgbvpn.mgmtui.logic.pos.impl.DeliveryLogicImpl;
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
		bind(PosAdapter.class).in(Singleton.class);

		bind(LoginLogic.class).to(LoginLogicImpl.class);
		bind(SysUserDao.class).to(SysUserDaoImpl.class);

		bind(AgentDao.class).to(AgentDaoImpl.class).in(Singleton.class);
		bind(AgentLogic.class).to(AgentLogicImpl.class).in(Singleton.class);

		bind(DeliveryLogic.class).to(DeliveryLogicImpl.class);
		bind(DeliveryDao.class).to(DeliveryDaoImpl.class);
		bind(DeliveryDetailDao.class).to(DeliveryDetailDaoImpl.class);
		
		
	}

}
