/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.impl;

import com.chinarewards.qqgbvpn.main.protocol.ServiceRequest;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class ServiceRequestImpl implements ServiceRequest {

	final Object parameter;

	/**
	 * 
	 * @param parameter
	 */
	public ServiceRequestImpl(Object parameter) {
		this.parameter = parameter;
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

}
