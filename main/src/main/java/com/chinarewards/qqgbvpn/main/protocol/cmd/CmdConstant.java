package com.chinarewards.qqgbvpn.main.protocol.cmd;

public class CmdConstant {

	/**
	 * 分隔符
	 */
	public static final char SEPARATOR = '\0';

	/**
	 * 错误cmdId
	 */
	public static final long ERROR_CMD_ID = 4294967295L;

	/**
	 * SEQ错误
	 */
	public static final long ERROR_SEQ_CODE = 1;

	/**
	 * checksum 错误
	 */
	public static final long ERROR_CHECKSUM_CODE = 2;
	
	/**
	 * message size 错误
	 */
	public static final long ERROR_MESSAGE_SIZE_CODE = 3;

	/**
	 * message 错误
	 */
	public static final long ERROR_MESSAGE_CODE = 4;

	/**
	 * no login
	 */
	public static final long ERROR_NO_LOGIN_CODE = 7;

	/**
	 * Invalid command ID.
	 */
	public static final long ERROR_INVALID_CMD_ID = 9;

	/**
	 * enter
	 */
	public static final char ENTER = 13;
	
	/**
	 * 华厦银行确认兑换品请求
	 */
	public static final long HUAXIA_BANK_REDEEM_CONFIRM = 21;
	
	/**
	 * 华厦银行确认兑换品请求
	 */
	public static final long HUAXIA_BANK_REDEEM_CONFIRM_RESPONSE = 22;
	
	/**
	 * 华厦银行ACK兑换品请求
	 */
	public static final long HUAXIA_BANK_REDEEM_ACK = 23;
	
	/**
	 * 华厦银行ACK兑换品请求
	 */
	public static final long HUAXIA_BANK_REDEEM_ACK_RESPONSE = 24;
	
	/**
	 * pos echo 请求
	 */
	public static final long ECHO_CMD_ID = 25;
	
	/**
	 * pos echo 响应
	 */
	public static final long ECHO_CMD_ID_RESPONSE = 26;
	
	/**
	 * pos new validate request
	 */
	public static final long VALIDATE_2_CMD_ID = 27;
	
	/**
	 * pos new validate response
	 */
	public static final long VALIDATE_2_CMD_ID_RESPONSE = 28;
}
