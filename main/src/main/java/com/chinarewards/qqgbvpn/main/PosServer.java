/**
 * 
 */
package com.chinarewards.qqgbvpn.main;

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
	public void start() throws PosServerException;

	/**
	 * Stop the server.
	 */
	public void stop();

	/**
	 * Check whether the server is stopped.
	 */
	public boolean isStopped();

	/**
	 * 
	 * @return
	 */
	public int getLocalPort();

}
