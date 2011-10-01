/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.protocol.impl.GuiceUnitOfWorkInterceptor;
import com.chinarewards.qqgbvpn.main.protocol.impl.SimpleServiceDispatcher;
import com.chinarewards.qqgbvpn.main.protocol.impl.SimpleServiceHandlerObjectFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class ServiceHandlerModule extends AbstractModule {

	protected final Configuration configuration;

	protected ServiceMapping mapping;
	
	protected static final Logger log = LoggerFactory.getLogger(ServiceHandlerModule.class);

	public ServiceHandlerModule(Configuration configuration) {
		this.configuration = configuration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure() {

		bind(ServiceMappingConfigBuilder.class).in(Singleton.class);

		bind(ServiceMapping.class).toProvider(ServiceMappingProvider.class).in(
				Singleton.class);

		ServiceMappingConfigBuilder mappingBuilder = new ServiceMappingConfigBuilder();
		mapping = mappingBuilder.buildMapping(configuration);

		bind(ServiceHandlerObjectFactory.class).to(
				SimpleServiceHandlerObjectFactory.class).in(Singleton.class);

		bind(ServiceDispatcher.class).to(SimpleServiceDispatcher.class).in(
				Singleton.class);

		// Start Guice UnitOfWork and Transaction around the dispatcher method
		GuiceUnitOfWorkInterceptor uowIntercept = new GuiceUnitOfWorkInterceptor();
		requestInjection(uowIntercept);
		bindInterceptor(Matchers.subclassesOf(ServiceDispatcher.class),
				Matchers.any(), uowIntercept);
		
		
		log.debug("Finished configuration");
	}

	private static class ServiceMappingProvider implements
			Provider<ServiceMapping> {

		protected final Configuration configuration;

		protected final ServiceMappingConfigBuilder builder;

		Logger log = LoggerFactory.getLogger(getClass());

		@Inject
		public ServiceMappingProvider(Configuration configuration,
				ServiceMappingConfigBuilder builder) {
			this.configuration = configuration;
			this.builder = builder;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.google.inject.Provider#get()
		 */
		@Override
		public ServiceMapping get() {
			log.debug("Returning ServiceMapping");
			return builder.buildMapping(configuration);
		}
	}

}
