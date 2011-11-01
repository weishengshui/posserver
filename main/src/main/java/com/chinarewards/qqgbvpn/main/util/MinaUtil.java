/**
 * 
 */
package com.chinarewards.qqgbvpn.main.util;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.apache.mina.core.session.IoSession;

import com.chinarewards.qqgbvpn.main.Session;
import com.chinarewards.qqgbvpn.main.protocol.filter.LoginFilter;
import com.chinarewards.qqgbvpn.main.protocol.filter.SessionKeyMessageFilter;

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
	public static final String getPosIdFromSession(Session session) {
		if (session == null) return null;
		if (session.containsAttribute(LoginFilter.POS_ID)) {
			return (String) session.getAttribute(LoginFilter.POS_ID);
		}
		return null;
	}

	public static final String getServerSessionId(IoSession session) {
		return (String) session
				.getAttribute(SessionKeyMessageFilter.SESSION_ID);
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

	/**
	 * Handy method to build a string for showing common information interested
	 * in knowing the originating client.
	 * <p>
	 * 
	 * Current implementation shows the following information:
	 * <ol>
	 * <li>IP address and port in the format &lt;ip&gt:&lt;port&gt;</li>
	 * <li>Mina session ID</li>
	 * <li>Identified POS ID, if logged in</li>
	 * </ol>
	 * 
	 * @param session
	 * @return
	 */
	public static final String buildCommonClientAddressText(IoSession session, Session serverSession) {
		return "address " + buildAddressPortString(session)
				+ ", Mina session ID " + session.getId()
				+ ", identified POS ID " + getPosIdFromSession(serverSession);
	}

}
