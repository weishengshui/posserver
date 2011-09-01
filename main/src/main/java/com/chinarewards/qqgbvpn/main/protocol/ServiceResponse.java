/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol;

/**
 * Defines the interface of a response.
 * 
 * @author Cyril
 * @since 0.1.0
 */
public interface ServiceResponse {

	/**
	 * Write the response object. This method should only be called once.
	 * 
	 * @param object
	 *            the response to write.
	 */
	public void writeResponse(Object object);

}
