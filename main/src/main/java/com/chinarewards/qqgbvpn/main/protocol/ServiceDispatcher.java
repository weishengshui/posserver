/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol;

import com.chinarewards.qqgbvpn.main.protocol.impl.ServiceDispatcherException;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public interface ServiceDispatcher {

	/**
	 * Dispatch the request using the given mapping.
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 */
	public void dispatch(ServiceMapping mapping, ServiceRequest request,
			ServiceResponse response) throws ServiceDispatcherException;

}
