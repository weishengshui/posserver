package com.chinarewards.qqgbvpn.main.MXBeans;

/**
 * 用了管理体现不同状态的连接数
 * @author dengrenwen
 *
 */
public interface ManageConnectMXBean {

	//已打开的连接数
	public long getOpenConnectCount();
	//活跃的连接数
	public long getActivityConnectCount();
	//闲置的连接数
	public long getIdleConnectCount();
}
