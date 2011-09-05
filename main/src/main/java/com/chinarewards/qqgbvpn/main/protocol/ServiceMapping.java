/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol;

import java.util.Iterator;

/**
 * Defines the service mapping.
 * 
 * @author Cyril
 * @since 0.1.0
 */
public interface ServiceMapping {

	/**
	 * Add a mapping between the command ID and the class.
	 * 
	 * @param commandId
	 *            the command ID to map
	 * @param clazz
	 *            the corresponding service handler class to be used for this
	 *            mapping.
	 */
	public void addMapping(long commandId, Class<? extends ServiceHandler> clazz);

	/**
	 * Returns the command handler with matching command ID. If no mapping
	 * exists for the specified command ID, <code>null</code> will be returned.
	 * 
	 * @param commandId
	 *            the command ID.
	 * @return the command handler class, or <code>null</code> if none is found.
	 */
	public Object getMapping(long commandId);

	/**
	 * Returns all command IDs found in this mapping.
	 * 
	 * @return an iterator over all command IDs.
	 */
	public Iterator<Long> getCommandIds();

}
