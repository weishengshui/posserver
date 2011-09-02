/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.struts.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.mgmtui.struts.SessionConstant;
import com.chinarewards.qqgbvpn.mgmtui.struts.UserSession;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 * This Struts interceptor checks if the user has logged in to the web system.
 * If the user has not logged in, it will set the result code to
 * {@link Action#INPUT}. User should configure the struts.xml to handle this
 * result code.
 * <p>
 * 
 * This interceptor also supports bringing back the user to the previous page
 * which requires authentication but user did not authenticate itself. It will
 * store the path and query string e.g.
 * <code>/foo/bar.action?name=peter#loc</code> to the value stack with name
 * <code>backUrl</code>. Developer can use this value to redirect the user back
 * to the previous page which user tried to access before authenticating itself.
 * However, only HTTP GET request method is handled.
 * 
 * @author cyril
 * @author Cream move from tiger2.
 * @since 1.0.0 2011-09-02
 */
public class LoginCheckInterceptor implements Interceptor {

	private static final long serialVersionUID = -6461545629203973991L;

	private Logger logger = LoggerFactory.getLogger(getClass());

	public void destroy() {
	}

	public void init() {
	}

	public String intercept(ActionInvocation invocation) throws Exception {

		logger.debug("login intercept invoke");
		
		ActionContext context = invocation.getInvocationContext();
		Map<String, Object> session = context.getSession();

		boolean hasLogin = false;

		// check the UserSession object in the HTTP session.
		UserSession userSession = (UserSession) session
				.get(SessionConstant.USER_SESSION);
		if (userSession == null) {
			// object not present, definitely no Struts action created and
			// no authentication is done by user.
			logger.trace("UserSession not present in ActionInvocation.getInvocationContext().getSession()");
			hasLogin = false;
		} else {
			hasLogin = userSession.isUserLoggedIn();
			logger.trace("UserSession.isUserLoggedIn: {}", hasLogin);
		}

		if (!hasLogin) {

			// user has no logged in. Returns a result of LOGIN.

			// we do more job. If it is a GET request, we provide the requested
			// URL such that if the user has successfully logged in, it will
			// bring back to the original page.
			if (isGetRequest()) {
				String url = getRequestUrl();
				logger.trace("requestUrl={}", url);
				if (url != null) {
					// String encodedUrl = URLEncoder.encode(url, "UTF-8");
					String encodedUrl = url;
					context.getValueStack().set("backUrl", encodedUrl);
				}
			}

			return Action.LOGIN;

		} else {
			// invoke the action.
			return invocation.invoke();
		}
	}

	/**
	 * This method checks if the HTTP request is a GET request.
	 * 
	 * @return <code>true</code> if it is a GET request, <code>false</code>
	 *         otherwise.
	 */
	private boolean isGetRequest() {
		String method = ServletActionContext.getRequest().getMethod();
		return "GET".equals(method);
	}

	/**
	 * This method returns the request URL. This method only returns the path,
	 * query string and the anchor part e.g.
	 * <code>/foo/bar.action?name=peter#loc</code>.
	 * <p>
	 * 
	 * This method will strip out the context path component.
	 * 
	 * @return the request URL
	 */
	private String getRequestUrl() {

		HttpServletRequest r = ServletActionContext.getRequest();
		String url = r.getRequestURI();

		// query string
		if (r.getQueryString() != null) {
			url += "?" + r.getQueryString();
		}
		// strip out context path
		if (url.startsWith(r.getContextPath())) {
			url = url.substring(r.getContextPath().length());
		}
		// TODO obtain the anchor part.
		return url;
	}

}
