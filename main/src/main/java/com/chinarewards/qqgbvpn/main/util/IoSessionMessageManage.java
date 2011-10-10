package com.chinarewards.qqgbvpn.main.util;

import org.apache.mina.core.session.IoSession;

/**
 * 用了管理，需要控制的iosession 的属性
 * @author harry
 *
 */
public class IoSessionMessageManage {

	//IoSession连接  state
	private String state;
	
	private  IoSession session;
	
	//闲置开始的时间
	private long IdleTime;
	
	public long getIdleTime() {
		return IdleTime;
	}
	public void setIdleTime(long idleTime) {
		IdleTime = idleTime;
	}
	public IoSession getSession() {
		return session;
	}
	public void setSession(IoSession session) {
		this.session = session;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}
