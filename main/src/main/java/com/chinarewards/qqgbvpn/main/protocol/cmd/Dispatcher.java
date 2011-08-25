/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.cmd;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public interface Dispatcher {

	public void dispatch(Object cmd, Object context);

}
