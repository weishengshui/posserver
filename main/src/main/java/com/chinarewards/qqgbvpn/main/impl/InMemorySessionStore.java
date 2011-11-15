/**
 * 
 */
package com.chinarewards.qqgbvpn.main.impl;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.ConfigKey;
import com.chinarewards.qqgbvpn.main.Session;
import com.chinarewards.qqgbvpn.main.SessionStore;
import com.chinarewards.qqgbvpn.main.protocol.filter.SessionKeyMessageFilter;
import com.google.inject.Inject;

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

	public static final int DEFAULT_EXPIRED_TIME = 1800;

	protected final Configuration configuration;

	protected Logger log = LoggerFactory.getLogger(InMemorySessionStore.class);

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
		public void setAttribute(Object key, Object value) {
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

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.chinarewards.qqgbvpn.main.Session#containsAttribute(java.lang
		 * .String)
		 */
		@Override
		public boolean containsAttribute(String key) {
			return store.containsKey(key);
		}

	}

	/**
	 * The backing store saving the session ID and value pair.
	 */
	ConcurrentHashMap<String /* session ID */, Session /* value */> store;

	/**
	 * 
	 */
	@Inject
	public InMemorySessionStore(Configuration configuration) {
		super();
		this.configuration = configuration;
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

	@Override
	public void expiredSession(String sessionId) {
		log.debug("parameter sessionid={}", sessionId);
		long expiredTime = configuration
				.getLong(ConfigKey.SERVER_EXPIRED_SESSION_KEY_TIME,
						DEFAULT_EXPIRED_TIME);
		log.debug("config_expired_time={} second!", expiredTime);
		long expiredMillis = expiredTime * 1000;
		long currentTime = System.currentTimeMillis();
		long lastAccessTime = 0;
		Session serverSession = store.get(sessionId);
		log.debug("befor clear store size count={}", store.size());
		if (serverSession != null
				&& serverSession
						.containsAttribute(SessionKeyMessageFilter.LAST_ACCESS_TIME)) {
			lastAccessTime = Long.valueOf(serverSession.getAttribute(
					SessionKeyMessageFilter.LAST_ACCESS_TIME).toString());
			if (currentTime - lastAccessTime >= expiredMillis) {
				store.remove(sessionId);
			}
		}
		log.debug("after clear store size count={}", store.size());
	}

	@Override
	public void expiredSession() {
		long expiredTime = configuration
				.getLong(ConfigKey.SERVER_EXPIRED_SESSION_KEY_TIME,
						DEFAULT_EXPIRED_TIME);
		log.debug("config_expired_time={} second!", expiredTime);
		clearStore(expiredTime);
	}

	@Override
	public void expiredSession(long expiredTime) {
		log.debug("parameter_expired_time={} second!", expiredTime);
		clearStore(expiredTime);
	}

	@SuppressWarnings("rawtypes")
	private void clearStore(long expiredSecond) {

		long expiredMillis = expiredSecond * 1000;
		log.debug("befor clear store size count={}", store.size());
		if (store != null && expiredMillis > 0 && store.size() > 0) {
			Session serverSession = null;
			long currentTime = System.currentTimeMillis();
			long lastAccessTime = 0;
			Iterator it = store.keySet().iterator();
			while (it.hasNext()) {
				String sessionId = it.next().toString();
				serverSession = store.get(sessionId);
				if (serverSession != null
						&& serverSession
								.containsAttribute(SessionKeyMessageFilter.LAST_ACCESS_TIME)) {
					lastAccessTime = Long.valueOf(serverSession.getAttribute(
							SessionKeyMessageFilter.LAST_ACCESS_TIME)
							.toString());
					if (currentTime - lastAccessTime >= expiredMillis) {
						store.remove(sessionId);
					}
				}
			}
		}
		log.debug("after clear store size count={}", store.size());
	}

}
