/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.socket;

/**
 * Basic message format for a message.
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 * @see http://darksleep.com/player/JavaAndUnsignedTypes.html
 */
public class MsgV2 {

	public static final long LENGTH = 16;

	private long seq;

	private long ack;

	private int flags;

	private int checksum;

	private long size;

	public MsgV2(long seq, long ack, int flags, int checksum, long size) {
		super();
		this.seq = seq;
		this.ack = ack;
		this.flags = flags;
		this.checksum = checksum;
		this.size = size;
	}

	/**
	 * @return the length
	 */
	public long getLength() {
		return size;
	}

	/**
	 * @return the seq
	 */
	public long getSeq() {
		return seq;
	}

	/**
	 * @return the ack
	 */
	public long getAck() {
		return ack;
	}

	/**
	 * @return the flags
	 */
	public int getFlags() {
		return flags;
	}

	/**
	 * @return the checksum
	 */
	public int getChecksum() {
		return checksum;
	}

	/**
	 * @return the size
	 */
	public long getSize() {
		return size;
	}

}
