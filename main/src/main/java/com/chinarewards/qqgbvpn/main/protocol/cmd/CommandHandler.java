/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.cmd;

import org.apache.mina.core.session.IoSession;

import com.chinarewards.qqgbvpn.main.protocol.socket.message.IBodyMessage;

/**
 * Defines the interface which can properly handles the execution of an command.
 * 
 * @author Cyril
 * @since 0.1.0
 */
public interface CommandHandler {

	/**
	 * Execute cmd
	 * 
	 * @param session
	 * @param bodyMessage
	 * @return
	 */
	public IBodyMessage execute(IoSession session,IBodyMessage bodyMessage);

}
