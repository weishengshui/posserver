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

}
