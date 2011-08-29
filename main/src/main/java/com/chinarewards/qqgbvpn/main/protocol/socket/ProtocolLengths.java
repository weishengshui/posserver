/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.socket;

/**
 * Defines the byte lengths for various well-known messages.
 * 
 * @author Cyril
 * @since 0.1.0
 */
public abstract class ProtocolLengths {

	/**
	 * 
	 */
	public static final int HEADER = 2;

	/**
	 * 
	 */
	public static final int PACKET = 4;

	/**
	 * 
	 */
	public static final int POS_SERIAL = 4;

	/**
	 * 
	 */
	public static final int COMMAND = 2;
	
	public static final int POS_ID = 12;
	
	public static final int HEAD = 16;//add by tommy 
	
	public static final int CHALLEUGERESPONSE = 16;
	
	public static final int RESULT = 2;

}
