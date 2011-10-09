package com.chinarewards.qqgbvpn.main.MXBeans;

/**
 * 用了管理体现不同状态的连接数
 * @author dengrenwen
 *
 */
public interface ManageConnectMXBean {

	/**
	 * 已打开的连接数
	 * @return
	 */
	public long getOpenConnectCount();
	/**
	 * 活跃的连接数
	 * @return
	 */
	public long getActivityConnectCount();
	/**
	 * 闲置的连接数
	 * @return
	 */
	public long getIdleConnectCount();
	
	/**
	 * 得到接收了多少字节数
	 * @return
	 */
	public long getReceiveBytesCount();
	
	/**
	 * 得到发送了多少个字节数
	 * @return
	 */
	public long getSendBytesCount();
	
	/**
	 * 关闭所有闲置连接
	 */
	public void closeAllIdleConnect();
	
	/**
	 * 清空所有的统计数
	 */
	public void clearAllStatistic();
	
	/**
	 * 关闭闲置了多少分钟的连接
	 * @param minute
	 */
	public void closeIdleConnect(long minute);
	
}
