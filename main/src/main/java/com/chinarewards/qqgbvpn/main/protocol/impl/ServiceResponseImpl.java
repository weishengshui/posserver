/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.impl;

import com.chinarewards.qqgbvpn.main.protocol.ServiceResponse;

/**
 * 
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class ServiceResponseImpl implements ServiceResponse {

	Object response;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.chinarewards.qqgbvpn.main.protocol.ServiceResponse#writeResponse(
	 * java.lang.Object)
	 */
	@Override
	public void writeResponse(Object response) {
		this.response = response;
	}

	/**
	 * @return the response
	 */
	public Object getResponse() {
		return response;
	}

}
