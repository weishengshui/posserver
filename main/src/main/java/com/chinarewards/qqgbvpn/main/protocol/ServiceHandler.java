/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol;

/**
 * Define the interface of a service handler. A service handler executes a
 * request.
 * 
 * @author Cyril
 * @since 0.1.0
 */
public interface ServiceHandler {

	/**
	 * Execute the specified request.
	 * 
	 * @param request
	 * @param response
	 */
	public void execute(ServiceRequest request, ServiceResponse response);

}
