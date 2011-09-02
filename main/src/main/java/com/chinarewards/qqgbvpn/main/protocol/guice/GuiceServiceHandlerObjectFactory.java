/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.guice;

import com.chinarewards.qqgbvpn.main.protocol.ServiceHandler;
import com.chinarewards.qqgbvpn.main.protocol.ServiceHandlerObjectFactory;
import com.chinarewards.qqgbvpn.main.protocol.ServiceMapping;
import com.google.inject.Injector;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class GuiceServiceHandlerObjectFactory implements
		ServiceHandlerObjectFactory {

	protected final ServiceMapping mapping;

	protected final Injector injector;

	public GuiceServiceHandlerObjectFactory(Injector injector,
			ServiceMapping mapping) {
		this.mapping = mapping;
		this.injector = injector;
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
		if (o instanceof Class) {
			Class clazz = (Class) o;
			ServiceHandler handler = injector.getInstance(clazz);
			return handler;
		} else {
			throw new UnsupportedOperationException("Handler object of type "
					+ o + " is not supported");
		}
	}

}
