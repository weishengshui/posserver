/**
 * 
 */
package com.chinarewards.qqgbvpn.main.util;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.apache.mina.core.session.IoSession;

import com.chinarewards.qqgbvpn.main.protocol.filter.LoginFilter;

/**
 * Contains a set of Mina related APIs.
 * 
 * @author cyril
 * @since 0.1.0
 */
public abstract class MinaUtil {

	/**
	 * Returns the POS ID stored in the Mina session.
	 * 
	 * @param session
	 * @return
	 */
	public static final String getPosIdFromSession(IoSession session) {
		if (session.containsAttribute(LoginFilter.POS_ID)) {
			return (String) session.getAttribute(LoginFilter.POS_ID);
		}
		return null;
	}

	/**
	 * Build the remote address and port information in the string format of
	 * &lt;ip&gt:&lt;port&gt;.
	 * 
	 * @param session
	 * @return
	 */
	public static final String buildAddressPortString(IoSession session) {
		SocketAddress addr = session.getRemoteAddress();
		if (addr == null || !(addr instanceof InetSocketAddress)) {
			return null;
		}

		InetSocketAddress sAddr = (InetSocketAddress) addr;
		return sAddr.getAddress().getHostAddress() + ":" + sAddr.getPort();
	}

}