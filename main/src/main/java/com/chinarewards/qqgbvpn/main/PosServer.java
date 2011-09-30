/**
 * 
 */
package com.chinarewards.qqgbvpn.main;

import java.io.IOException;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;

/**
 * Defines the interface of a POS server.
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public interface PosServer {

	/**
	 * Start the server.
	 */
	public void start() throws PosServerException, InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException, MalformedObjectNameException, NullPointerException, IOException;

	/**
	 * Stop the server.
	 */
	public void stop()throws IOException;

	/**
	 * Complete shutdown the server and release all resources. This should be
	 * the last method to call.
	 */
	public void shutdown();

	/**
	 * Check whether the server is stopped.
	 */
	public boolean isStopped();

	/**
	 * Returns the actual port this server is listening at.
	 * 
	 * @return
	 */
	public int getLocalPort();

	/**
	 * 是否开启监控服务
	 * @param isMonitorEnable
	 * @throws IOException
	 */
	public void setMonitorEnable(boolean isMonitorEnable)throws IOException;

}
