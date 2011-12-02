package com.chinarewards.qqgbvpn.main.protocol.cmd;

public class CmdConstant {

	/**
	 * 初始化
	 */
	public static final long INIT_CMD_ID = 5;

	/**
	 * 初始化返回
	 */
	public static final long INIT_CMD_ID_RESPONSE = 6;
	/**
	 * 登录请求
	 */
	public static final long LOGIN_CMD_ID = 7;
	/**
	 * 登录回复
	 */
	public static final long LOGIN_CMD_ID_RESPONSE = 8;
	/**
	 * 绑定请求
	 */
	public static final long BIND_CMD_ID = 9;
	/**
	 * 绑定回复
	 */
	public static final long BIND_CMD_ID_RESPONSE = 10;

	/**
	 * 固件更新请求
	 */
	public static final long FIRMWARE_UPGRADE_CMD_ID = 13;
	/**
	 * 顾健更新回复
	 */
	public static final long FIRMWARE_UPGRADE_CMD_ID_RESPONSE = 14;

	/**
	 * Request firmware fragment.
	 */
	public static final long GET_FIRMWARE_FRAGMENT_CMD_ID = 15;

	/**
	 * Response for firmware fragment request.
	 */
	public static final long GET_FIRMWARE_FRAGMENT_CMD_ID_RESPONSE = 16;

	/**
	 * 查询请求
	 */
	public static final long SEARCH_CMD_ID = 1;
	/**
	 * 查询回复
	 */
	public static final long SEARCH_CMD_ID_RESPONSE = 2;

	/**
	 * 验证请求
	 */
	public static final long VALIDATE_CMD_ID = 3;
	/**
	 * 验证回复
	 */
	public static final long VALIDATE_CMD_ID_RESPONSE = 4;

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
	 * 验证回调请求
	 */
	public static final long VAL_CALLBACK_CMD_ID = 11;
	/**
	 * 验证回调应答
	 */
	public static final long VAL_CALLBACK_CMD_ID_RESPONSE = 12;
	
	
	/**
	 * POS機更新固件成功请求
	 */
	public static final long FIRMWARE_UP_DONE_CMD_ID = 17;
	
	/**
	 * POS機更新固件成功應答 
	 */
	public static final long FIRMWARE_UP_DONE_CMD_ID_RESPONSE = 18;
	
	/**
	 * 华厦银行查询兑换品请求
	 */
	public static final long HUAXIA_BANK_REDEEM_SEARCH = 19;
	
	/**
	 * 华厦银行查询兑换品请求
	 */
	public static final long HUAXIA_BANK_REDEEM_SEARCH_RESPONSE = 20;
	
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
	
}
