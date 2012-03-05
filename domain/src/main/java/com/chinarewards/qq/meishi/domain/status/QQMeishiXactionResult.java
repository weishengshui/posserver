/**
 * 
 */
package com.chinarewards.qq.meishi.domain.status;

/**
 * Contains the definition of QQ Meishi Q-Mi transaction result code.
 * 
 * @author Cyril
 * @since 0.2.0
 */
public class QQMeishiXactionResult {

	/**
	 * The transaction is completed successfully.
	 */
	public static final int OK = 0;

	/**
	 * The transaction password present to the server is bad.
	 */
	public static final int BAD_XACTION_PASSWORD = 1;

	/**
	 * The user token is invalid.
	 */
	public static final int BAD_USER_TOKEN = 2;

	/**
	 * The user is illegal.
	 */
	public static final int ILLEGAL_USER = 3;

	/**
	 * The consumption amount is invalid.
	 */
	public static final int INVALID_CONSUMPTION_AMOUNT = 4;

	/**
	 * Transaction password is required.
	 */
	public static final int XACTION_PASSWORD_REQUIRED = 5;

}
