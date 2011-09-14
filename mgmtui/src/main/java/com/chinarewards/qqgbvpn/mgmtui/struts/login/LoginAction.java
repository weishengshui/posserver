/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.struts.login;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.chinarewards.qqgbvpn.mgmtui.logic.login.LoginLogic;
import com.chinarewards.qqgbvpn.mgmtui.struts.BaseAction;
import com.chinarewards.qqgbvpn.mgmtui.struts.SessionConstant;
import com.chinarewards.qqgbvpn.mgmtui.struts.SimpleUserSession;
import com.chinarewards.qqgbvpn.mgmtui.struts.formbean.UserForm;
import com.chinarewards.qqgbvpn.mgmtui.util.Tools;
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

	@Override
	public void validate() {
		if (StringUtil.isEmptyString(username)
				|| StringUtil.isEmptyString(password)) {
			addFieldError("loginError", "用户名或密码为空");
			log.debug("username or password is empty");
		}
	}

	@Override
	@SkipValidation
	public String execute() throws Exception {
		return SUCCESS;
	}

	public String login() {

		LoginLogic loginLogic = super.getInstance(LoginLogic.class);

		log.debug("User ({}) tries to login...", username);
		boolean validePass = loginLogic.checkLogin(username, password);

		if (validePass) {
			Map<String, Object> session = ActionContext.getContext()
					.getSession();
			UserForm userForm = new UserForm();
			userForm.setUserName(username);
			SimpleUserSession userSession = new SimpleUserSession(userForm,
					true);
			session.put(SessionConstant.USER_SESSION, userSession);

			log.debug("login success");
			if(!Tools.isEmptyString(backUrl)){
				HttpServletRequest request =  ServletActionContext.getRequest();
				StringBuffer callBack = new StringBuffer();
				callBack.append(request.getScheme()).append("://").append(request.getServerName()).append((request.getServerPort() == 80 ? "" : ":"
					+ request.getServerPort())).append(request.getContextPath());
				callBack.append(backUrl);
				try {
					ServletActionContext.getResponse().sendRedirect(callBack.toString());
				}catch (IOException e) {
					log.error(e.getMessage(),e);
					return SUCCESS;
				}
				return null;
			}
			
			return SUCCESS;
		} else {
			addFieldError("loginError", "用户名或密码错误");
			log.debug("login failed");
			return INPUT;
		}
	}

	@SkipValidation
	public String logout() {
		Map<String, Object> session = ActionContext.getContext()
		.getSession();
		session.remove(SessionConstant.USER_SESSION);
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
