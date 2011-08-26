/**
 * 
 */
package com.chinarewards.qqgbvpn.main.logic.login;

import com.chinarewards.qqgbvpn.main.protocol.cmd.init.InitRequest;
import com.chinarewards.qqgbvpn.main.protocol.cmd.init.InitResponse;
import com.chinarewards.qqgbvpn.main.protocol.cmd.login.LoginRequest;
import com.chinarewards.qqgbvpn.main.protocol.cmd.login.LoginResponse;

/**
 * Defined Login logic.
 * 
 * @author cream
 * @since 1.0.0 2011-08-25
 */
public interface LoginManager {

	/**
	 * Returns a random number.
	 * 
	 * @param req
	 * @return
	 */
	public InitResponse init(InitRequest req);

	/**
	 * 
	 * @param req
	 * @return
	 */
	public LoginResponse login(LoginRequest req);

}
