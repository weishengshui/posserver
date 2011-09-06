/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.guice;

import java.util.Properties;

import javax.servlet.ServletContextEvent;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.common.HomeDirLocator;
import com.chinarewards.qqgbvpn.common.SimpleDateTimeModule;
import com.chinarewards.qqgbvpn.config.ConfigReader;
import com.chinarewards.qqgbvpn.config.HardCodedConfigModule;
import com.chinarewards.qqgbvpn.core.jpa.JpaPersistModuleBuilder;
import com.chinarewards.qqgbvpn.logic.journal.DefaultJournalModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.struts2.Struts2GuicePluginModule;

/**
 * Entry point for all guice configurations. Register any Guice modules here.
 * 
 * @author cyril
 * @since 0.1.0
 */
public class GuiceBootstrap extends GuiceServletContextListener {

	Logger log = LoggerFactory.getLogger(getClass());
	
	protected String rootConfigFilename = "posnet.ini";
	
	Configuration configuration;

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {

		Injector injector = (Injector) servletContextEvent.getServletContext()
				.getAttribute(Injector.class.getName());
		injector.getInstance(PersistService.class).stop();
		super.contextDestroyed(servletContextEvent);
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		super.contextInitialized(servletContextEvent);
		Injector injector = (Injector) servletContextEvent.getServletContext()
				.getAttribute(Injector.class.getName());
		injector.getInstance(PersistService.class).start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.inject.servlet.GuiceServletContextListener#getInjector()
	 */
	protected Injector getInjector() {

		log.info("Guice bootstrapping");

		testLogVerboseLevel();
		
		try {
			this.buildConfiguration();
		} catch (ConfigurationException e) {
			throw new RuntimeException("Failed to build configuration", e);
		}

		Injector injector = null;

		Module[] modules = getModules();
		injector = Guice.createInjector(modules);
		log.info("Guice injector created");

		return injector;
	}

	protected Module[] getModules() {

		Module[] modules = new Module[] { new QqgbvpnServletModule(),
				new Struts2GuicePluginModule(), new QqgbvpnServiceModule(),
				new QQApiModule(), new DefaultJournalModule(),
				new SimpleDateTimeModule(),
				// JPA module
				getConfigModule(),
				buildJpaPersistModule() };

		return modules;
	}

	// TODO 需修改为配置文件中统一的数据库
	protected Properties getJPAProperties() {
		Properties properties = new Properties();
		properties.put("javax.persistence.transactionType", "RESOURCE_LOCAL");

		properties.put("hibernate.hbm2ddl.auto", "update");
		properties.put("hibernate.connection.driver_class",
				"com.mysql.jdbc.Driver");
		properties.put("hibernate.connection.username", "root");
		properties.put("hibernate.connection.password", "123456");
		properties.put("hibernate.connection.url",
				"jdbc:mysql://192.168.1.33:3306/qqapi");
		properties.put("hibernate.dialect",
				"org.hibernate.dialect.MySQL5Dialect");
		properties.put("hibernate.show_sql", "true");
		return properties;
	}

	protected Module getConfigModule() {

		// FIXME this will be deprecated.
		return new HardCodedConfigModule(configuration);

	}

	protected void buildConfiguration() throws ConfigurationException {

		// check if the directory is given via command line.
		String homedir = null;	// we don't have default directory
		HomeDirLocator homeDirLocator = new HomeDirLocator(homedir);
		ConfigReader cr = new ConfigReader(homeDirLocator);

		log.info("Home directory: {}", homeDirLocator.getHomeDir());

		// read the configuration
		Configuration conf = cr.read(this.getRootConfigFilename());
		configuration = conf;

		if (this.configuration == null) {
			// no configuration is found, throw exception
			throw new RuntimeException(
					"No configuration is found. Please specify "
							+ "POSNET_HOME environment variable for the home directory.");
		}

	}

	/**
	 * @return the rootConfigFilename
	 */
	public String getRootConfigFilename() {
		return rootConfigFilename;
	}


	
	protected JpaPersistModule buildJpaPersistModule() {

		// TODO make it not a builder.
		JpaPersistModuleBuilder builder = new JpaPersistModuleBuilder();

		JpaPersistModule jpaModule = new JpaPersistModule("posnet");
		builder.configModule(jpaModule, configuration, "db");

		return jpaModule;
	}

	protected void testLogVerboseLevel() {
		System.out.println("Testing java logging verbose level");
		log.error("ERROR message (testing)");
		log.warn("WARN  message (testing)");
		log.info("INFO  message (testing)");
		log.debug("DEBUG message (testing)");
		log.trace("TRACE message (testing)");
	}

}
