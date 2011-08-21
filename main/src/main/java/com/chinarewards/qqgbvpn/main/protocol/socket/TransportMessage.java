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
public class TransportMessage {

	public static final long HEADER_LENGTH = 2;
	
	public static final long LENGTH = 6;

	private byte[] header = new byte[] { 0x0a, 0x0d };

	private long length;

	public TransportMessage(byte[] header, long length) {
		this.header = header;
		this.length = length;
	}

	public TransportMessage(long length) {
		this.length = length;
	}

	/**
	 * @return the header
	 */
	public byte[] getHeader() {
		return header;
	}

	/**
	 * @return the length
	 */
	public long getLength() {
		return length;
	}

}
