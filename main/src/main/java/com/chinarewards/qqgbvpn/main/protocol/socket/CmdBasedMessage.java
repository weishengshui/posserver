/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.socket;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class CmdBasedMessage extends SequentialMessage {

	public static final long LENGTH = SequentialMessage.LENGTH + 2;

	private int commandId;

	public CmdBasedMessage(long length, long sequence, int commandId) {
		super(length, sequence);
		this.commandId = commandId;
	}

	/**
	 * @return the commandId
	 */
	public int getCommandId() {
		return commandId;
	}

}