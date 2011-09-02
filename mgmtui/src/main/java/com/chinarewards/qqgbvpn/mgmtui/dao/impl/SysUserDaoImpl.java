/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;

import com.chinarewards.qqgbvpn.domain.SysUser;
import com.chinarewards.qqgbvpn.mgmtui.dao.SysUserDao;
import com.chinarewards.qqgbvpn.mgmtui.util.PasswordUtil;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author cream
 * 
 */
public class SysUserDaoImpl implements SysUserDao {

	@Inject
	Provider<EntityManager> emp;

	@SuppressWarnings("unchecked")
	@Override
	public boolean checkLogin(String username, String password) {
		String sec = PasswordUtil.encodePassword(password);
		List<SysUser> list = emp
				.get()
				.createQuery(
						"FROM SysUser WHERE username=:username AND password=:password")
				.setParameter("username", username)
				.setParameter("password", sec).getResultList();

		if (list == null || list.isEmpty()) {
			return false;
		}
		return true;
	}

	@Override
	public SysUser fetchUserById(String id) {
		return emp.get().find(SysUser.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SysUser> fetchUserByUsername(String username) {
		return emp.get().createQuery("FROM SysUser WHERE username=:username")
				.setParameter("username", username).getResultList();
	}

}
