/**
 * 
 */
package com.chinarewards.qqgbvpn.main;

/**
 * Defines a well known list of configuration keys.
 * 
 * @author cyril
 * @since 0.1.0
 */
public abstract class ConfigKey {

	/**
	 * The thread pool size which defines the number of threads created for
	 * handling client commands.
	 * 
	 * @since 0.1.0
	 */
	public static final String SERVER_SERVICEHANDLER_THREADPOOLSIZE = "server.service_handler.thread_pool_size";

	/**
	 * The thread pool size which defines the number of threads created for
	 * handling client commands.
	 * 
	 * @since 0.1.0
	 */
	public static final String SERVER_CLIENTMAXIDLETIME = "server.client_max_idle_time";
	
	/**
	 * 定时清理session store里面过期的session key 的信息（秒计 ）
	 * @since 0.1.0
	 */
	public static final String SERVER_CHECK_EXPIRED_SESSION_TIMER_DELAY = "server.check_expired_session_timer_delay";
	
	/**
	 * session key 的过期时间      （秒计 ）
	 */
	public static final String SERVER_EXPIRED_SESSION_KEY_TIME = "server.expired_session_key_time";

}
