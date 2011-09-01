/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.impl;

import com.chinarewards.qqgbvpn.main.protocol.ServiceHandler;
import com.chinarewards.qqgbvpn.main.protocol.ServiceHandlerObjectFactory;
import com.chinarewards.qqgbvpn.main.protocol.ServiceMapping;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class SimpleServiceHandlerObjectFactory implements
		ServiceHandlerObjectFactory {

	protected final ServiceMapping mapping;

	public SimpleServiceHandlerObjectFactory(ServiceMapping mapping) {
		this.mapping = mapping;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.chinarewards.qqgbvpn.main.protocol.ServiceHandlerObjectFactory#getHandler
	 * (long)
	 */
	@Override
	public ServiceHandler getHandler(long commandId) {

		Object o = mapping.getMapping(commandId);

		// if no mapping is found, return null.
		if (o == null)
			return null;

		ServiceHandler handler = null;

		if (o instanceof Class) {
			// it is an class, we create an NEW instance of ServiceHandler from
			// it.
			try {
				handler = (ServiceHandler) (((Class) o).newInstance());
			} catch (InstantiationException e) {
				// XXX throws better exception
				throw new RuntimeException(
						"Error creating ServiceHandler class", e);
			} catch (IllegalAccessException e) {
				// XXX throws better exception
				throw new RuntimeException(
						"Error creating ServiceHandler class", e);
			}

		} else {
			// assume it is an instance of ServiceHandler
			handler = (ServiceHandler) o;
		}

		return handler;
	}

}
