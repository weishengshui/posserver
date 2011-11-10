/**
 * 
 */
package com.chinarewards.qqgbvpn.main;


/**
 * Define the interface of a POS server session store.
 * <p>
 * A session store is an object which stores stateful information of betwen
 * connection.
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public interface SessionStore {

	/**
	 * Retrieve a session with matching session ID
	 * 
	 * @param sessionId
	 * @return
	 * @throws IllegalArgumentException
	 *             if <code>sessionId</code> is <code>null</code>.
	 */
	Session getSession(String sessionId);

	/**
	 * Create a session using the given session ID. Any existing session will
	 * be erased.
	 * 
	 * @param sessionId
	 * @return
	 * @throws IllegalArgumentException
	 *             if <code>sessionId</code> is <code>null</code>.
	 */
	Session createSession(String sessionId);
	
	/**
	 * 在连接断开的时候移除session  store里面的存储
	 * @param sessionId
	 */
	Session removeSession(String sessionId);

}
