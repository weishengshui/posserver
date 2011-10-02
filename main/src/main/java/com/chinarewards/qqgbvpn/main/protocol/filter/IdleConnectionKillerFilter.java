package com.chinarewards.qqgbvpn.main.protocol.filter;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.util.MinaUtil;

/**
 * Kills idle connections.
 * 
 * @author dengrenwen
 * @since 0.1.0
 */
public class IdleConnectionKillerFilter extends IoFilterAdapter {

	Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void sessionIdle(NextFilter nextFilter, IoSession session,
			IdleStatus status) throws Exception {

		if (log.isDebugEnabled()) {
			log.debug(
					"Connection idle too long, closing... (addr: {}, session ID: {}, POS ID: {})",
					new Object[] { MinaUtil.buildAddressPortString(session),
							session.getId(),
							MinaUtil.getPosIdFromSession(session) });
		}

		session.close(true);

		nextFilter.sessionIdle(session, status);
		
		log.trace("messageReceived() done");
	}

}
