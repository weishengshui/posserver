/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.filter;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;

import com.chinarewards.qqgbvpn.main.Session;
import com.chinarewards.qqgbvpn.main.SessionStore;
import com.chinarewards.qqgbvpn.main.util.MinaUtil;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class AbstractFilter extends IoFilterAdapter {

	/**
	 * Returns the server session
	 * 
	 * @param session
	 * @return
	 */
	protected Session getServerSession(IoSession session, SessionStore sessionStore) {
		String sessionId = MinaUtil.getServerSessionId(session);
		if (sessionId == null) return null;
		return sessionStore.getSession(sessionId);
	}

}
