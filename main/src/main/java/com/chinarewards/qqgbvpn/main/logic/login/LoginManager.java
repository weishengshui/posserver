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
	 * <ul>
	 * <li>Check POS ID first</li>
	 * <li>Check POS status.</li>
	 * <li>Check POS secret code. If not existed, create it.</li>
	 * </ul>
	 */
	public InitResponseMessage init(InitRequestMessage req);

	/**
	 * 
	 * @param req
	 * @return
	 */
	public LoginResponseMessage login(LoginRequestMessage req);

	/**
	 * Bind POS. The Message will be the same with Login. So reuse LoginMessage.
	 * 
	 * @param req
	 * @return
	 */
	LoginResponseMessage bind(LoginRequestMessage req);

}
