/**
 * 
 */
package com.chinarewards.qq.meishi.domain.status;

/**
 * Contains the definition of QQ Meishi Q-Mi transaction error code.
 * 
 * @author Harry
 * @since 0.2.0
 */
public class QQMeishiXactionErrorCode {

	/**
	 * 成功  
	 */
	public static final int OK = 0;
	/**
	 * 必需参数未传递
	 */
	public static final int QQWS_ERROR_MISSINGPARA	= 1;

	/**
	 * 方法内部执行错误，如系统繁忙
	 */
	public static final int QQWS_ERROR_INTERNAL		= 2;

	/**
	 * 参数无效，请求方法的参数不合要求
	 */
	public static final int QQWS_ERROR_INVALIDPARA	 = 3;

	/**
	 * sig 校验出错
	 */
	public static final int QQWS_ERROR_SIG			 = 4;

	/**
	 * 没有权限操作
	 */
	public static final int QQWS_ERROR_NOPERMISSION	  = 5;
	/**
	 * 逻辑性错误
	 */
	public static final int QQWS_ERROR_LOGIC	       = 6; 
	/**
	 * 非法的 client （尚未申请）
	 */
	public static final int QQWS_ERROR_ILLEGALCLIENT    = 1001;
}
