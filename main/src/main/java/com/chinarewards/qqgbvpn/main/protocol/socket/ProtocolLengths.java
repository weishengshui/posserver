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
	public static final int PACKET = 4;

	/**
	 * 
	 */
	public static final int POS_SERIAL = 4;

	/**
	 * 
	 */
	public static final int COMMAND = 4;
	
	public static final int POS_ID = 12;
	
	public static final int HEAD = 16;//add by tommy 
	
	public static final int CHALLENGE_RESPONSE = 16;
	
	public static final int RESULT = 2;
	
	public static final int VALIDATE_COUNT  = 2;
	
	public static final int QQWS_RESULTCODE   = 4;
	
	public static final int QQVALIDATE_RESULTSTATUS  = 4;
	
	public static final int CR_DATE_LENGTH = 11;
	
	public static final int FIRST_VALIDATE_TIME   = CR_DATE_LENGTH;
	
	public static final int PREV_VALIDATE_TIME  = CR_DATE_LENGTH;
	
	public static final int CHALLENGE = 8;
	
	public static final int PAGE = 2;
	
	public static final int SIZE = 2;
	
	public static final int FIRMWARE_OFFSET = 4;
	
	public static final int FIRMWARE_LENTH = 4;
	
	public static final int REDEEM_COUNT = 2;
	
	public static final int AMOUNT = 8; 
	
	public static final int USERTOKEN = 16;
	
	public static final int POSNETSTRLEN = 2;
	
	public static final int QQMEISHI_SERVER_ERROR = 4;
	
	public static final int QQMEISHI_QQWS_ERROR = 4;
	
	public static final int QQMEISHI_RESULT = 4;
	
	public static final int FORCE_PWD_NEXT_ACTION = 1;
	
	public static final int ECHO_COMMAND_RESULT = 1;

}
