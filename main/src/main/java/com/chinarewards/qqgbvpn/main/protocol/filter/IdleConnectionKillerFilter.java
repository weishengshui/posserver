package com.chinarewards.qqgbvpn.main.protocol.filter;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.SessionStore;
import com.chinarewards.qqgbvpn.main.util.MinaUtil;
import com.google.inject.Inject;

/**
 * Kills idle connections.
 * 
 * @author dengrenwen
 * @since 0.1.0
 */
public class IdleConnectionKillerFilter extends AbstractFilter {

	Logger log = LoggerFactory.getLogger(getClass());
	
	final SessionStore sessionStore;
	
	@Inject
	public IdleConnectionKillerFilter(SessionStore sessionStore) {
		this.sessionStore = sessionStore;
	}

	@Override
	public void sessionIdle(NextFilter nextFilter, IoSession session,
			IdleStatus status) throws Exception {

		if (log.isDebugEnabled()) {
			log.debug(
					"Connection idle too long, closing... (addr: {}, session ID: {}, POS ID: {})",
					new Object[] { MinaUtil.buildAddressPortString(session),
							session.getId(),
							MinaUtil.getPosIdFromSession(getServerSession(session, sessionStore)) });
		}
		
		if(session.containsAttribute(SessionKeyMessageFilter.SESSION_ID)){
			//在连线断开的时间情况session key的信息，当然是在session key过期的情况下
			sessionStore.expiredSession((String)session.getAttribute(SessionKeyMessageFilter.SESSION_ID));
			
		}

		session.close(true);

		nextFilter.sessionIdle(session, status);
		
		log.trace("sessionIdle() done");
	}

}
