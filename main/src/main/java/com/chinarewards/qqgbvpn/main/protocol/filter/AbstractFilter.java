/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.filter;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;

import com.chinarewards.qqgbvpn.main.Session;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class AbstractFilter extends IoFilterAdapter {

	protected Session getServerSession(IoSession session) {
		return (Session) session
				.getAttribute(SessionKeyMessageFilter.SESSION_ID);
	}

}
