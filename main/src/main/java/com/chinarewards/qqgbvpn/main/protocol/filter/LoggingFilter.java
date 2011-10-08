package com.chinarewards.qqgbvpn.main.protocol.filter;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides better business-oriented logging.
 * <p>
 * Current this filter can log the client IP, client port and any identified POS
 * ID (if POS machine has logged in to this server before).
 * <p>
 * The best position to place this filter is after the message has been decoded,
 * before any business related filter.
 * 
 * @author cyril
 * @since 0.1.0
 */
public class LoggingFilter extends IoFilterAdapter {

	Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void exceptionCaught(NextFilter nextFilter, IoSession session,
			Throwable cause) throws Exception {

		logError(session, cause);

		exceptionCaught(nextFilter, session, cause);
	}

	/**
	 * Log the following information to report that a command cannot be
	 * processed.
	 * <p>
	 * Note that one should not assume the following information must exists.
	 * 
	 * <ol>
	 * <li>Source IP</li>
	 * <li>Source port</li>
	 * <li>POS ID</li>
	 * <li>TODO: Command ID</li>
	 * <li>TODO: Command content</li>
	 * </ol>
	 */
	protected void logError(IoSession session, Throwable cause) {

		// return immediately if insufficient level is given.
		if (!log.isDebugEnabled())
			return;

		// prepare the logging data
		String addr = buildAddressPortString(session);
		String posId = getPosIdFromSession(session);

		log.debug(
				"An exception is caught when handling command. Detailed information: "
						+ " client address={}:{}, POS ID={}", new Object[] {
						addr, posId });

	}

	/**
	 * Returns the POS ID stored in the Mina session.
	 * 
	 * @param session
	 * @return
	 */
	protected String getPosIdFromSession(IoSession session) {
		if (session.containsAttribute(LoginFilter.POS_ID)) {
			return (String) session.getAttribute(LoginFilter.POS_ID);
		}
		return null;
	}

	protected String buildAddressPortString(IoSession session) {
		SocketAddress addr = session.getRemoteAddress();
		if (addr == null || !(addr instanceof InetSocketAddress)) {
			return null;
		}

		InetSocketAddress sAddr = (InetSocketAddress) addr;
		return sAddr.getAddress().getHostAddress() + ":" + sAddr.getPort();
	}

}
