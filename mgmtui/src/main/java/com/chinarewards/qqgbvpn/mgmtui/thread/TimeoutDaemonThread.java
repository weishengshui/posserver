package com.chinarewards.qqgbvpn.mgmtui.thread;

import com.chinarewards.qqgbvpn.mgmtui.exception.TimeoutException;

/**
 * 控制超时的守护线程
 * @author iori
 *
 */
public class TimeoutDaemonThread extends Thread {
	
	/**
	 * 超时时间
	 */
	private long timeout;
	
	/**
	 * 计时是否被取消
	 */
	private boolean isCanceled = false;
	
	public TimeoutDaemonThread(long timeout) {
		super();
		this.timeout = timeout;
		// 设置本线程为守护线程
		this.setDaemon(true);
	}
	
	public boolean getIsCanceled() {
		return this.isCanceled;
	}

	/**
	 * 　取消计时 　　
	 */
	public synchronized void cancel() {
		isCanceled = true;
	}

	/**
	 * 　启动超时计时器 　　
	 */
	public void run() {
		try {
			Thread.sleep(timeout);
			if (!isCanceled) {
				throw new TimeoutException("time out!");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
