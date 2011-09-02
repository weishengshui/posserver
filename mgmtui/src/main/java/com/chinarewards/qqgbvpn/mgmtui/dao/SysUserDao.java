/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.dao;

import java.util.List;

import com.chinarewards.qqgbvpn.domain.SysUser;

/**
 * @author cream
 * 
 */
public interface SysUserDao {

	boolean checkLogin(String username, String password);

	SysUser fetchUserById(String id);

	List<SysUser> fetchUserByUsername(String username);
}
