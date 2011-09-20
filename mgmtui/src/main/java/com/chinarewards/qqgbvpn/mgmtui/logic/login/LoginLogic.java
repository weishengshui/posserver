/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.logic.login;

/**
 * 
 * 
 * @author cream
 * @since 0.1.0
 */
public interface LoginLogic {

	/**
	 * Check to login.
	 * 
	 * @param username
	 * @param password
	 * @param ipAddr
	 * @return
	 */
	public boolean checkLogin(String username, String password, String ipAddr);
}
