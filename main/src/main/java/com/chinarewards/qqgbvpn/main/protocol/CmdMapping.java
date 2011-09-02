/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol;

import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.ICommandCodec;

/**
 * Defines the command codec mapping.
 * 
 * @author Cyril
 * @since 0.1.0
 */
public interface CmdMapping {

	/**
	 * Add a mapping between the command ID and the class.
	 * 
	 * @param commandId
	 *            the command ID to map
	 * @param clazz
	 *            the corresponding service handler class to be used for this
	 *            mapping.
	 */
	public void addMapping(long commandId,
			Class<? extends ICommandCodec> clazz);

	/**
	 * Returns the command handler with matching command ID. If no mapping
	 * exists for the specified command ID, <code>null</code> will be returned.
	 * 
	 * @param commandId
	 *            the command ID.
	 * @return the codec class which extends <code>IBodyMessageCoder</code>, or
	 *         <code>null</code> if none is found.
	 */
	public Object getMapping(long commandId);

}
