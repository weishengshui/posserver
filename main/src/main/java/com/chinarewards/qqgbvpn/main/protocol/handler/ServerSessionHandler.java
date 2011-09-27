package com.chinarewards.qqgbvpn.main.protocol.handler;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.exception.PackageException;
import com.chinarewards.qqgbvpn.main.protocol.ServiceDispatcher;
import com.chinarewards.qqgbvpn.main.protocol.ServiceMapping;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.cmd.Message;
import com.chinarewards.qqgbvpn.main.protocol.filter.LoginFilter;
import com.chinarewards.qqgbvpn.main.protocol.impl.ServiceDispatcherException;
import com.chinarewards.qqgbvpn.main.protocol.impl.ServiceRequestImpl;
import com.chinarewards.qqgbvpn.main.protocol.impl.ServiceResponseImpl;
import com.chinarewards.qqgbvpn.main.protocol.impl.mina.MinaSession;
import com.google.inject.Injector;

/**
 * Server handler.
 * <p>
 * 
 * Message reaching this class shoudl be ready for dispatching to the
 * corresponding <code>ServiceHandler</code> class.
 * 
 * @author huangwei
 * @author cyril
 * @since 0.1.0
 */
public class ServerSessionHandler extends IoHandlerAdapter {

	private Logger log = LoggerFactory.getLogger(getClass());

	protected final ServiceDispatcher serviceDispatcher;

	protected final ServiceMapping serviceMapping;

	protected final ExecutorService exec;

	public ServerSessionHandler(Injector injector,
			ServiceDispatcher serviceDispatcher, ServiceMapping serviceMapping) {
		this.serviceDispatcher = serviceDispatcher;
		this.serviceMapping = serviceMapping;

		// XXX configurable option for thread size
		exec = Executors.newFixedThreadPool(20);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		// XXX is this OK to handle this exception in this way?
		log.error("An exception is caught in "
				+ this.getClass().getSimpleName(), cause);
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {

		log.trace("messageReceived() started");

		debugMessageReceived(session, message);

		// do the actual dispatch
		doDispatch(session, message);

		log.trace("messageReceived() done");
	}

	/**
	 * Print the debug information of any message received event. If logging is
	 * not configured at DEBUG level or lower, no task will be executed.
	 * 
	 * @param session
	 * @param message
	 */
	protected void debugMessageReceived(IoSession session, Object message) {

		if (!log.isDebugEnabled())
			return;

		String posId = getLoggedInPosId(session);

		// debug print the remote address (IP, port and POS ID)
		log.debug("messageReceived() from remote {}, identified POS ID: {}",
				buildAddressPortString(session), posId);

	}

	/**
	 * Returns the logged in POS ID, muting all exception.
	 * 
	 * @param session
	 * @return
	 */
	protected String getLoggedInPosId(IoSession session) {
		try {
			String posId = (String) session.getAttribute(LoginFilter.POS_ID);
			return posId;
		} catch (Throwable t) {
			log.error(
					"Internal program error: POS ID in Mina session cannot be retrieved",
					t);
		}
		return null;
	}

	/**
	 * Do the actual dispatch job, including reading the request, prepare the
	 * request and response environment, send the request to
	 * <code>ServiceHandler</code>, and write back the response.
	 * <p>
	 * 
	 * The request and response
	 * 
	 * @param session
	 * @param message
	 * @throws Exception
	 */
	protected void doDispatch(final IoSession session, final Object message)
			throws Exception {

		Callable<Void> callable = new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				// get the message
				Message msg = (Message) message;

				long cmdId = msg.getBodyMessage().getCmdId();

				// FIXME throw PackageException if no handler found for command
				// ID

				// build a request (for dispatcher)
				MinaSession serviceSession = new MinaSession(session);
				ServiceRequestImpl request = new ServiceRequestImpl(
						msg.getBodyMessage(), serviceSession);
				// build a response (for dispatcher)
				ServiceResponseImpl response = new ServiceResponseImpl();

				// dispatch the command to the corresponding service handler.
				try {
					serviceDispatcher.dispatch(serviceMapping, request,
							response);
				} catch (ServiceDispatcherException e) {
					throw new PackageException(
							"No mapping found for command ID " + cmdId, e);
				}

				// grep the response, and write back to the channel.
				ICommand responseMsgBody = (ICommand) response.getResponse();
				msg.setBodyMessage(responseMsgBody);
				session.write(msg);

				return null;
			}

		};

		exec.submit(callable);

	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		log.trace("Socket client idle ({} count: {})", status,
				session.getIdleCount(status));
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		super.sessionOpened(session);

		printRemoteSocketAddress(session);

	}

	/**
	 * Prints the remote address and port information.
	 * 
	 * @param session
	 *            the session to print.
	 */
	protected void printRemoteSocketAddress(IoSession session) {

		SocketAddress addr = session.getRemoteAddress();
		if (addr == null || !(addr instanceof InetSocketAddress)) {
			return;
		}

		// print it
		log.info("Incoming connection from remote address: "
				+ buildAddressPortString(session));
	}

	/**
	 * Prints the number of managed session.
	 * 
	 * @param service
	 */
	protected void printManagedSessions(IoService service) {

		log.info("Number of managed sessions: {}"
				+ service.getManagedSessionCount());

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
