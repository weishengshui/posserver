/**
 * 
 */
package com.chinarewards.qqgbpvn.main;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.chinarewards.qqgbpvn.core.test.GuiceTest;
import com.chinarewards.qqgbvpn.core.jpa.JpaPersistModuleBuilder;

/**
 * @author Cyril
 * 
 */
public class JpaPersistModuleBuilderTest extends GuiceTest {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinarewards.qqgpvn.main.test.GuiceTest#setUp()
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBuildHibernateProperties_OK() {

		Configuration conf = new BaseConfiguration();

		// setup test data
		// persistence
		conf.setProperty("db.user", "root");
		conf.setProperty("db.password", "password123");
		conf.setProperty("db.driver", "org.hsqldb.jdbcDriver");
		conf.setProperty("db.url", "jdbc:hsqldb:.");
		// additional Hibernate properties
		conf.setProperty("db.hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
		conf.setProperty("db.hibernate.show_sql", true);

		// play with the API
		JpaPersistModuleBuilder b = new JpaPersistModuleBuilder();
		Properties props = b.buildHibernateProperties(conf, "db");
		
		// validate
		assertEquals(6, props.size());
		assertEquals("root", props.get("hibernate.connection.username"));
		assertEquals("password123", props.get("hibernate.connection.password"));
		assertEquals("org.hibernate.dialect.HSQLDialect", props.get("hibernate.dialect"));
		assertEquals("org.hsqldb.jdbcDriver", props.get("hibernate.connection.driver_class"));
		assertEquals("jdbc:hsqldb:.", props.get("hibernate.connection.url"));
		assertEquals(true, props.get("hibernate.show_sql"));
		
	}

	@Test
	public void testBuildHibernateProperties_OverwriteDbUsername() {

		Configuration conf = new BaseConfiguration();

		// setup test data
		// persistence
		conf.setProperty("db.user", "root");
		conf.setProperty("db.password", "password123");
		conf.setProperty("db.driver", "org.hsqldb.jdbcDriver");
		conf.setProperty("db.url", "jdbc:hsqldb:.");
		// additional Hibernate properties
		conf.setProperty("db.hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
		conf.setProperty("db.hibernate.show_sql", true);
		conf.setProperty("db.hibernate.connection.username", "bad-root");	// this will be overwritten

		// play with the API
		JpaPersistModuleBuilder b = new JpaPersistModuleBuilder();
		Properties props = b.buildHibernateProperties(conf, "db");
		
		// validate
		assertEquals(6, props.size());
		assertEquals("root", props.get("hibernate.connection.username"));
		assertEquals("password123", props.get("hibernate.connection.password"));
		assertEquals("org.hibernate.dialect.HSQLDialect", props.get("hibernate.dialect"));
		assertEquals("org.hsqldb.jdbcDriver", props.get("hibernate.connection.driver_class"));
		assertEquals("jdbc:hsqldb:.", props.get("hibernate.connection.url"));
		assertEquals(true, props.get("hibernate.show_sql"));
		
	}

}
