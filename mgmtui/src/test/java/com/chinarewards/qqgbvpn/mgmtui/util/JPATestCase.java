package com.chinarewards.qqgbvpn.mgmtui.util;

import java.util.Properties;

import javax.persistence.EntityManager;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.common.SimpleDateTimeModule;
import com.chinarewards.qqgbvpn.logic.journal.DefaultJournalModule;
import com.chinarewards.qqgbvpn.mgmtui.guice.QqgbvpnServiceModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;

/**
 * JPATestCase
 * 
 * @author huangwei
 *
 */
public abstract class JPATestCase extends TestCase{

	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected Injector injector;
	protected EntityManager em;

	public JPATestCase() {
		Module[] modules = getModules();
		injector = Guice.createInjector(modules);
		PersistService persistService = injector.getInstance(PersistService.class);
		persistService.start();
		this.em = injector.getInstance(EntityManager.class);
	}
	
	
	protected Module[] getModules() {

		Module[] modules = new Module[] { new QqgbvpnServiceModule(),
				 new DefaultJournalModule(),new SimpleDateTimeModule(),
		// JPA module
		new JpaPersistModule("posnet").properties(getJPAProperties())
		};
		return modules;
	}
	
	protected Properties getJPAProperties(){
		Properties properties = new Properties();
		properties.put("javax.persistence.transactionType", "RESOURCE_LOCAL");
		
		properties.put("hibernate.hbm2ddl.auto", "update");
		properties.put("hibernate.connection.driver_class",
				"org.hsqldb.jdbcDriver");
		properties.put("hibernate.connection.username",
				"sa");
		properties.put("hibernate.connection.password", "");
		properties.put("hibernate.connection.url", "jdbc:hsqldb:.");
		properties.put("hibernate.dialect",
				"org.hibernate.dialect.HSQLDialect");
		properties.put("hibernate.show_sql",
		"true");
		return properties;
	}
	
	public void testEm(){
		assertNotNull(em);
	}
	
	public void begin() {
		em.getTransaction().begin();
	}

	public void commit() {
		em.getTransaction().commit();
	}

	public void rollback() {
		em.getTransaction().rollback();
	}

	protected void setUp() throws Exception {
		super.setUp();
		logger.debug("Begin a Transaction");
		em.getTransaction().begin();
	}

	protected void tearDown() throws Exception {
		if (em.getTransaction().isActive()) {
			rollback();
			logger.debug("Shutdown testCase rollback transaction!");
		}
	}
}
