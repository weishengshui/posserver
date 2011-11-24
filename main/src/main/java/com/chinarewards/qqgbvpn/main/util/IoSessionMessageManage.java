package com.chinarewards.qqgbvpn.main.util;

import org.apache.mina.core.session.IoSession;

/**
 * 用了管理，需要控制的iosession 的属性
 * 
 * @author harry
 * 
 */
public class IoSessionMessageManage {

	// IoSession连接 state
	private String state;

	private IoSession session;
	// 总共接收的bytes
	private long receiveBytes;
	// iosession 上一次请求的接收总bytes
	private long lastSessionReaderBytes;
	// 总共发送的bytes
	private long sendBytes;
	// iosession 上一次请求的发送总bytes
	private long lastSessionWriteBytes;

	// 闲置开始的时间
	private long idleTime;

	public long getIdleTime() {
		return idleTime;
	}

	public void setIdleTime(long idleTime) {
		this.idleTime = idleTime;
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

	public long getReceiveBytes() {
		return receiveBytes;
	}

	public void setReceiveBytes(long receiveBytes) {
		if (receiveBytes > 0) {
			this.receiveBytes += receiveBytes - lastSessionReaderBytes;
			this.lastSessionReaderBytes = receiveBytes;
		} else {
			this.receiveBytes = 0;
		}
	}

	public long getSendBytes() {
		return sendBytes;
	}

	public void setSendBytes(long sendBytes) {
		if (sendBytes > 0) {
			this.sendBytes += sendBytes - lastSessionWriteBytes;
			this.lastSessionWriteBytes = sendBytes;
		} else {
			this.sendBytes = 0;
		}
	}
}
