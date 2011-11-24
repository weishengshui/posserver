package com.chinarewards.qqgbvpn.main.management;


/**
 * 用了管理体现不同状态的连接数
 * 
 * @author dengrenwen
 *
 */
public interface PosConnectionMXBean {

	/**
	 * 关闭所有闲置连接
	 */
	public void closeIdleConnections();
	
	/**
	 * 关闭闲置了多少秒钟的连接
	 * @param seconds
	 */
	public void closeIdleConnections(long seconds);
	
	/**
	 * 活跃的连接数
	 * @return
	 */
	public long getActive();
	
	/**
	 * 得到接收了多少字节数
	 * @return
	 */
	public long getBytesReceived();
	
	/**
	 * 得到发送了多少个字节数
	 * @return
	 */
	public long getBytesSent();
	
	/**
	 * 已打开的连接数
	 * @return
	 */
	public long getConnectionCount();
	
	/**
	 * 闲置的连接数
	 * @return
	 */
	public long getIdle();
	
	/**
	 * 清空所有的统计数
	 */
	public void resetStatistics();
	
}
