/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.impl;

import com.chinarewards.qqgbvpn.main.protocol.ServiceRequest;
import com.chinarewards.qqgbvpn.main.protocol.ServiceSession;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class ServiceRequestImpl implements ServiceRequest {

	final Object parameter;
	
	final ServiceSession session;

	/**
	 * 
	 * @param parameter
	 */
	public ServiceRequestImpl(Object parameter, ServiceSession session) {
		this.parameter = parameter;
		this.session = session;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinarewards.qqgbvpn.main.protocol.ServiceRequest#getParameter()
	 */
	@Override
	public Object getParameter() {
		return parameter;
	}

	/* (non-Javadoc)
	 * @see com.chinarewards.qqgbvpn.main.protocol.ServiceRequest#getSession()
	 */
	@Override
	public ServiceSession getSession() {
		return session;
	}

}
