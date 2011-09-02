/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.struts;

import com.chinarewards.qqgbvpn.mgmtui.struts.formbean.UserForm;

/**
 * Defines the user session in the web application. For every user-agent (aka
 * web browser) visiting the web application, there will be one unique user
 * session associated with the visitor.
 * <p>
 * Although the class name is called user session, this does not mean that the
 * user has logged in to the system. To obtain the logged in user information,
 * use {#link {@link #isUserLoggedIn()}.
 * 
 * @author Cream
 * @since 1.0.0 2011-09-02
 */
public interface UserSession {

	public UserForm getUser();

	public boolean isUserLoggedIn();

}
