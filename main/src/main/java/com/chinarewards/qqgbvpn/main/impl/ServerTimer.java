/**
 * 
 */
package com.chinarewards.qqgbvpn.main.impl;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * 
 * @author dengrenwen
 * @since 0.1.0
 */
public class ServerTimer {
	
	protected Logger log = LoggerFactory.getLogger(ServerTimer.class);

	Runnable task;
	ScheduledExecutorService timer;
	ScheduledFuture<?> future;

	long delay = 0;

	long period = 1000;

	public ServerTimer() {
		timer = Executors.newScheduledThreadPool(1);
	}

	public void start() {
		if (task != null) {
			future = timer.scheduleAtFixedRate(task, delay, period,
					TimeUnit.SECONDS);
		}
	}

	public void stop() {
		if (future != null && !future.isCancelled()) {
			future.cancel(true);
			timer.shutdown();
		}
	}

	/**
	 * 如果定时任务没有启动，就初始参数 如果定时任务已经启动，就更改参数并重启设定启动任务
	 * 
	 * @param delay
	 * @param period
	 * @param task   如果为null就不改变上次完成的任务
	 */
	public void scheduleAtFixedInterval(long delay, long period, Runnable task) {
		boolean cancel = false;
		
		if(this.delay == delay && this.period == period && task == null){
			return;
		}		
		if (delay >= 0 && period >= 0) {
			this.delay = delay;
			this.period = period;
		}
		if (task != null) {
			this.task = task;
		}
		
		if (this.future != null && !this.future.isCancelled()) {
			this.future.cancel(true);
			cancel = true;
		}
		if (cancel){
			this.future = timer.scheduleAtFixedRate(this.task, this.delay, this.period,
					TimeUnit.SECONDS);
		}

	}

}
