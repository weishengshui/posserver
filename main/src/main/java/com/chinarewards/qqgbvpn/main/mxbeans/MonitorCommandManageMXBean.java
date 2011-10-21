package com.chinarewards.qqgbvpn.main.mxbeans;


/**
 * 用了管理体现不同状态的连接数
 * @author dengrenwen
 *
 */
public interface MonitorCommandManageMXBean {
	/**
	 * 各个指令的接收数目
	 * @return
	 */
	public String getAllCommandReceiveMessage();
	
	public void clearAllStatistic();
}
