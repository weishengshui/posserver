/**
 * 
 */
package com.chinarewards.qqgbvpn.main.logic.login;

import com.chinarewards.qqgbvpn.main.protocol.socket.message.InitRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.InitResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.LoginRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.LoginResponseMessage;

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
	public InitResponseMessage init(InitRequestMessage req);

	/**
	 * 
	 * @param req
	 * @return
	 */
	public LoginResponseMessage login(LoginRequestMessage req);

}
