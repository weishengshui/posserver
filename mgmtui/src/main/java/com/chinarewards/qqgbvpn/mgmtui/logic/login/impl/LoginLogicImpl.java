/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.logic.login.impl;

import com.chinarewards.qqgbvpn.mgmtui.dao.SysUserDao;
import com.chinarewards.qqgbvpn.mgmtui.logic.login.LoginLogic;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

/**
 * @author cream
 * 
 */
public class LoginLogicImpl implements LoginLogic {

	@Inject
	SysUserDao userDao;

	@Transactional
	@Override
	public boolean checkLogin(String username, String password) {
		return userDao.checkLogin(username, password);
	}

}
