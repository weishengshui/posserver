/**
 * 
 */
package com.chinarewards.qqgbvpn.main.impl;

import java.util.concurrent.ConcurrentHashMap;

import com.chinarewards.qqgbvpn.main.Session;
import com.chinarewards.qqgbvpn.main.SessionStore;

/**
 * Concrete implementation of <code>SessionStore</code> backed by a in-memory
 * storage for sessions.
 * <p>
 * 
 * The session store used is a instance of <code>ConcurrentHashMap</code>.
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class InMemorySessionStore implements SessionStore {

	/**
	 * Concrete implementation of Session.
	 */
	private static class SimpleSession implements Session {

		/**
		 * The backing store saving the session ID and value pair.
		 */
		ConcurrentHashMap</* key */Object, /* value */Object> store = new ConcurrentHashMap<Object, Object>();

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.chinarewards.qqgbvpn.main.Session#putAttribute(java.lang.Object,
		 * java.lang.Object)
		 */
		@Override
		public void putAttribute(Object key, Object value) {
			store.put(key, value);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.chinarewards.qqgbvpn.main.Session#getAttribute(java.lang.Object)
		 */
		@Override
		public Object getAttribute(Object key) {
			return store.get(key);
		}

	}

	/**
	 * The backing store saving the session ID and value pair.
	 */
	ConcurrentHashMap<String /* session ID */, Session /* value */> store;

	/**
	 * 
	 */
	public InMemorySessionStore() {
		super();

		// create the internal store.
		this.store = new ConcurrentHashMap<String, Session>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.chinarewards.qqgbvpn.main.SessionStore#getSession(java.lang.String)
	 */
	@Override
	public Session getSession(String sessionId) {
		return store.get(sessionId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.chinarewards.qqgbvpn.main.SessionStore#createSession(java.lang.String
	 * )
	 */
	@Override
	public Session createSession(String sessionId) {
		Session session = new SimpleSession();
		store.put(sessionId, session);
		return session;
	}

}
