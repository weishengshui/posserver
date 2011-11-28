package com.chinarewards.qqgbvpn.main.protocol.filter;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;
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

	// 闲置了idleKillerTime毫秒就关闭连接
	private long idleKillerTime;

	public IdleConnectionKillerFilter() {

	}

	public IdleConnectionKillerFilter(long idleKillerTime) {
		// time millisecond
		this.idleKillerTime = idleKillerTime * 1000;
	}

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

		// 第一次闲置触发就设置闲置开始时间
		if (session.getAttribute("startIdleTime") == null) {
			session.setAttribute("startIdleTime", System.currentTimeMillis());
		} else {
			// 判断闲置时间是否到了可以关闭的条件
			long startTime = Long.valueOf(session.getAttribute("startIdleTime")
					.toString());
			if (System.currentTimeMillis() - startTime >= idleKillerTime) {
				session.close(true);
				log.trace("sessionIdle() done");
			}
		}

		nextFilter.sessionIdle(session, status);

	}

	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session,
			Object message) throws Exception {

		if (session.getAttribute("startIdleTime") != null) {
			session.removeAttribute("startIdleTime");
		}

		nextFilter.messageReceived(session, message);
	}

	@Override
	public void messageSent(NextFilter nextFilter, IoSession session,
			WriteRequest writeRequest) throws Exception {

		if (session.getAttribute("startIdleTime") != null) {
			session.removeAttribute("startIdleTime");
		}

		nextFilter.messageSent(session, writeRequest);

	}

}
