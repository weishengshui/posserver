package com.chinarewards.qqgbvpn.main.protocol.hander;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.config.CmdProperties;
import com.chinarewards.qqgbvpn.main.exception.PackgeException;
import com.chinarewards.qqgbvpn.main.protocol.ServiceDispatcher;
import com.chinarewards.qqgbvpn.main.protocol.ServiceMapping;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CommandHandler;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.impl.ServiceRequestImpl;
import com.chinarewards.qqgbvpn.main.protocol.impl.ServiceResponseImpl;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.Message;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

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

	private Injector injector;

	protected final ServiceDispatcher serviceDispatcher;

	protected final ServiceMapping serviceMapping;

	public ServerSessionHandler(Injector injector,
			ServiceDispatcher serviceDispatcher, ServiceMapping serviceMapping) {
		this.injector = injector;
		this.serviceDispatcher = serviceDispatcher;
		this.serviceMapping = serviceMapping;
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		// cause.printStackTrace();
		log.error("An exception is detected in "
				+ this.getClass().getSimpleName(), cause);
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {

		log.debug("messageReceived() start");

		// get the message
		Message msg = (Message) message;

		long cmdId = msg.getBodyMessage().getCmdId();
		String cmdName = injector.getInstance(CmdProperties.class)
				.getCmdNameById(cmdId);
		if (cmdName == null || cmdName.length() == 0) {
			throw new PackgeException("cmd id is not exits,cmdId is :" + cmdId);
		}

		// Dispatcher
		CommandHandler commandHandler = injector.getInstance(Key.get(
				CommandHandler.class, Names.named(cmdName)));

		ICommand responseMsgBody = commandHandler.execute(session,
				msg.getBodyMessage());

		msg.setBodyMessage(responseMsgBody);
		session.write(msg);

		log.debug("messageReceived end");
		log.debug("Server written to client...");
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
	protected void doDispatch(IoSession session, Object message)
			throws Exception {

		// get the message
		Message msg = (Message) message;
		long cmdId = msg.getBodyMessage().getCmdId();

		// construct the request & response pair.
		ServiceRequestImpl request = new ServiceRequestImpl(
				msg.getBodyMessage());
		ServiceResponseImpl response = new ServiceResponseImpl();

		// dispatch it!
		serviceDispatcher.dispatch(serviceMapping, request, response);

		// and then encode the response.
		Object responseContent = response.getResponse();

		// and write back to the socket as response.
		if (responseContent != null) {
			session.write(responseContent);
		}

	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		log.debug("Socket client idle (} count: {})", status,
				session.getIdleCount(status));
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		super.sessionOpened(session);
	}

}
