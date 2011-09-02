/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.impl;

import com.chinarewards.qqgbvpn.main.protocol.ServiceDispatcher;
import com.chinarewards.qqgbvpn.main.protocol.ServiceHandler;
import com.chinarewards.qqgbvpn.main.protocol.ServiceHandlerObjectFactory;
import com.chinarewards.qqgbvpn.main.protocol.ServiceMapping;
import com.chinarewards.qqgbvpn.main.protocol.ServiceRequest;
import com.chinarewards.qqgbvpn.main.protocol.ServiceResponse;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.google.inject.Inject;

/**
 * Simple implementation of <code>ServiceDispatcher</code>.
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class SimpleServiceDispatcher implements ServiceDispatcher {

	protected final ServiceHandlerObjectFactory objFactory;

	/**
	 * 
	 * @param objFactory
	 */
	@Inject
	public SimpleServiceDispatcher(ServiceHandlerObjectFactory objFactory) {
		this.objFactory = objFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.chinarewards.qqgbvpn.main.protocol.ServiceDispatcher#dispatch(com
	 * .chinarewards.qqgbvpn.main.protocol.ServiceMapping,
	 * com.chinarewards.qqgbvpn.main.protocol.ServiceRequest,
	 * com.chinarewards.qqgbvpn.main.protocol.ServiceResponse)
	 */
	@Override
	public void dispatch(ServiceMapping mapping, ServiceRequest request,
			ServiceResponse response) {

		ICommand cmd = (ICommand) request.getParameter();
		long commandId = cmd.getCmdId();

		ServiceHandler handler = objFactory.getHandler(commandId);

		handler.execute(request, response);

	}

}
