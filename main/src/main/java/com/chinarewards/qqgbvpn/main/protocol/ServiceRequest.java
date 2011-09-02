/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol;

/**
 * Service request.
 * 
 * @author Cyril
 * @since 0.1.0
 */
public interface ServiceRequest {

	/**
	 * Returns the request parameter.
	 * 
	 * @return the request parameter.
	 */
	public Object getParameter();

	/**
	 * 
	 * @return
	 */
	public ServiceSession getSession();
	
}
