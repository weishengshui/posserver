/**
 * 
 */
package com.chinarewards.qqgbvpn.main.session.v1;

import com.chinarewards.qqgbvpn.main.session.ISessionKey;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class V1SessionKey implements ISessionKey {

	String key;

	public V1SessionKey(String key) {
		this.key = key;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "key=" + key;
	}

	/* (non-Javadoc)
	 * @see com.chinarewards.qqgbvpn.main.session.ISessionKey#getVersion()
	 */
	@Override
	public int getVersion() {
		return 1;
	}

	/* (non-Javadoc)
	 * @see com.chinarewards.qqgbvpn.main.session.ISessionKey#getSessionId()
	 */
	@Override
	public String getSessionId() {
		return key;
	}

	

}
