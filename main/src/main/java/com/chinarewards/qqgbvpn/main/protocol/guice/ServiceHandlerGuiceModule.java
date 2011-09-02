/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.guice;

import java.util.Iterator;

import org.mortbay.log.Log;

import com.chinarewards.qqgbvpn.main.protocol.ServiceMapping;
import com.google.inject.AbstractModule;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class ServiceHandlerGuiceModule extends AbstractModule {

	private final ServiceMapping mapping;

	public ServiceHandlerGuiceModule(ServiceMapping mapping) {
		this.mapping = mapping;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure() {
		
		Iterator<Long> commandIdsIter = this.mapping.getCommandIds();
		while (commandIdsIter.hasNext()) {
			long commandId = commandIdsIter.next();
			Object o = mapping.getMapping(commandId);
			if (o instanceof Class) {
				Class clazz = (Class)o;
				bind(clazz);
			} else {
				Log.warn("Unsupported service mapping result object {}", o);
			}
		}

	}

}
