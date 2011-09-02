/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.struts.formbean;

/**
 * User information.
 * 
 * @author cream
 * @since 1.0.0 2011-09-02
 */
public class UserForm {

	private String userName;

	@Override
	public String toString() {
		return "UserForm [userName=" + userName + "]";
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
