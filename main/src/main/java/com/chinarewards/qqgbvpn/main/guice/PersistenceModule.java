package com.chinarewards.qqgbvpn.main.guice;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class PersistenceModule extends AbstractModule {

	static final protected String DEFAULT_PERSISTENCE_UNIT_NAME = "microblogger";

	final protected String persistenceUnitName;
	final protected Map<String, Object> config;

	protected EntityManagerFactory emf;

	public PersistenceModule(String persistenceUnitName) {
		this(persistenceUnitName, null);
	}

	/**
	 * give null to config to get the default from getPersistenceConfig()
	 * method.
	 * 
	 * @param persistenceUnitName
	 * @param config
	 */
	public PersistenceModule(String persistenceUnitName,
			Map<String, Object> config) {
		this.persistenceUnitName = persistenceUnitName;
		this.config = config;
	}

	public PersistenceModule() {
		this(DEFAULT_PERSISTENCE_UNIT_NAME);
	}

	/**
	 * Override to get customed persistence config.
	 * 
	 * From Hibernate Manual:
	 * 
	 * All the properties defined in Section 2.2.1, “Packaging” can be passed to
	 * the createEntityManagerFactory method and there are a few additional
	 * ones:
	 * 
	 * <ul>
	 * <li><em>javax.persistence.provider</em> to define the provider class used
	 * <li><em>javax.persistence.transactionType</em> to define the transaction
	 * type used (either JTA or RESOURCE_LOCAL)
	 * <li><em>javax.persistence.jtaDataSource</em> to define the JTA datasource
	 * name in JNDI
	 * <li><em>javax.persistence.nonJtaDataSource</em> to define the non JTA
	 * datasource name in JNDI
	 * <li><em>javax.persistence.lock.timeout</em> pessimistic lock timeout in
	 * milliseconds (Integer or String)
	 * <li><em>javax.persistence.query.timeout</em> query timeout in
	 * milliseconds (Integer or String)
	 * <li><em>javax.persistence.sharedCache.mode</em> corresponds to the
	 * share-cache-mode element defined in Section 2.2.1, “Packaging”.
	 * <li><em>javax.persistence.validation.mode</em> corresponds to the
	 * validation-mode element defined in Section 2.2.1, “Packaging”.
	 * </ul>
	 * 
	 * Refer to <a href="http://docs.jboss.org/hibernate/entitymanager/3.5/reference/en/html/configuration.html"
	 * >link</a> for more details.
	 */
	protected Map<String, Object> getPersistenceConfig() {
		return null;
	}

	@Override
	protected void configure() {
		// Create EMF
		Map<String, Object> c = config;
		if (c == null) {
			c = getPersistenceConfig();
		}
		emf = Persistence.createEntityManagerFactory(persistenceUnitName, c);

		bind(EntityManagerFactory.class).toInstance(emf);
		bind(EntityManagerProvider.class).in(Singleton.class);
		bind(EntityManager.class).toProvider(EntityManagerProvider.class).in(
				Singleton.class);
	}

}
