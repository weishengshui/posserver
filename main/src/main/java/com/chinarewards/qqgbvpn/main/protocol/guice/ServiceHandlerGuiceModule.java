/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.guice;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.protocol.ServiceHandlerObjectFactory;
import com.chinarewards.qqgbvpn.main.protocol.ServiceMapping;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class ServiceHandlerGuiceModule extends AbstractModule {
	
	Logger log = LoggerFactory.getLogger(getClass());

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
				log.debug("Binding class {} to Guice", clazz);
				bind(clazz);
			} else {
				log.warn("Unsupported service mapping result object {}", o);
			}
		}
		
		bind(ServiceHandlerObjectFactory.class).to(
				GuiceServiceHandlerObjectFactory.class).in(Singleton.class);

	}

}
