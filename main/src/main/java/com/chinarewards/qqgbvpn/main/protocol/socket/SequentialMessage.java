/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.socket;

/**
 * Sequential message.
 * 
 * @author Cyril
 * @since 0.1.0
 * @see http://darksleep.com/player/JavaAndUnsignedTypes.html
 */
public class SequentialMessage extends TransportMessage {

	public static final long LENGTH = TransportMessage.LENGTH + 4;

	private long sequence;

	public SequentialMessage(long length, long sequence) {
		super(length);
		this.sequence = sequence;
	}

	/**
	 * @return the sequence
	 */
	public long getSequence() {
		return sequence;
	}

}
