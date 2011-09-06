/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.dao.impl;

import java.util.List;

import com.chinarewards.qqgbvpn.core.BaseDao;
import com.chinarewards.qqgbvpn.domain.SysUser;
import com.chinarewards.qqgbvpn.mgmtui.dao.SysUserDao;
import com.chinarewards.qqgbvpn.mgmtui.util.PasswordUtil;

/**
 * @author cream
 * 
 */
public class SysUserDaoImpl extends BaseDao implements SysUserDao {

	@SuppressWarnings("unchecked")
	@Override
	public boolean checkLogin(String username, String password) {
		String sec = PasswordUtil.encodePassword(password);
		List<SysUser> list = getEm()
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
		return getEm().find(SysUser.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SysUser> fetchUserByUsername(String username) {
		return getEm().createQuery("FROM SysUser WHERE username=:username")
				.setParameter("username", username).getResultList();
	}

}
