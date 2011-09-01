/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.cmd;

import org.apache.mina.core.session.IoSession;

/**
 * Defines the interface which can properly handles the execution of an command.
 * 
 * @author Cyril
 * @since 0.1.0
 */
public interface CommandHandler {

	/**
	 * Execute the specified command.
	 * 
	 * @param session
	 * @param bodyMessage
	 * @return
	 */
	public ICommand execute(IoSession session, ICommand bodyMessage);

}
