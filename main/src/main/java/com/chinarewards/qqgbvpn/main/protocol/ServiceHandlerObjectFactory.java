/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public interface ServiceHandlerObjectFactory {

	/**
	 * Returns an instance of ServiceHandler with matching command ID.
	 * 
	 * @param commandId
	 *            the command ID
	 * @return the service hander, or <code>null</code> if no mapping is found
	 *         for the specified command ID.
	 */
	public ServiceHandler getHandler(long commandId);

}
