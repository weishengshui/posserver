/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.mina;

import org.apache.mina.core.session.IoSession;

import com.chinarewards.qqgbvpn.main.protocol.ServiceSession;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class MinaSession implements ServiceSession {
	
	protected final IoSession session;
	
	public MinaSession(IoSession session) {
		this.session = session;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.chinarewards.qqgbvpn.main.protocol.ServiceSession#get(java.lang.String
	 * )
	 */
	@Override
	public Object getAttribute(String key) {
		return session.getAttribute(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.chinarewards.qqgbvpn.main.protocol.ServiceSession#set(java.lang.String
	 * , java.lang.Object)
	 */
	@Override
	public void setAttribute(String key, Object value) {
		session.setAttribute(key, value);
	}

}
