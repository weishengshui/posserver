/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.cmd;

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
	 * @param cmd
	 */
	public void execute(Object cmd);

}
