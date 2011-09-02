/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.struts.login;

import java.util.Map;

import com.chinarewards.qqgbvpn.mgmtui.struts.BaseAction;
import com.chinarewards.qqgbvpn.mgmtui.struts.SessionConstant;
import com.chinarewards.utils.StringUtil;
import com.opensymphony.xwork2.ActionContext;

/**
 * Login operation.
 * 
 * @author cream
 * @since 1.0.0 2011-09-02
 */
public class LoginAction extends BaseAction {

	private static final long serialVersionUID = 6257809328181428130L;

	private String username;
	private String password;
	private String backUrl;

	public String login() {
		if (StringUtil.isEmptyString(username)
				|| StringUtil.isEmptyString(password)) {
			log.debug("username or password is empty");
			return INPUT;
		}

		boolean validePass = username.equals("cream")
				&& password.equals("cream");

		if (validePass) {
			Map<String, Object> session = ActionContext.getContext()
					.getSession();
			session.put(SessionConstant.USER_SESSION, true);
			log.debug("login success");
			return SUCCESS;
		} else {
			log.debug("login failed");
			return INPUT;
		}
	}

	public String logout() {
		return SUCCESS;
	}

	// ---- getter & setter ----
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBackUrl() {
		return backUrl;
	}

	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}

}
