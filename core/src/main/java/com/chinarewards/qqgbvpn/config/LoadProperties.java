/**
 * 
 */
package com.chinarewards.qqgbvpn.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @author cream
 * @deprecated
 */
public abstract class LoadProperties {

	static Logger logger = LoggerFactory.getLogger(LoadProperties.class);

	protected Properties properties;

	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	public Properties getProperties() {
		return properties;
	}

	protected LoadProperties() {
		properties = loadProperties();
	}

	protected Properties loadProperties() {
		Properties props = new Properties();

		InputStream in = getPropertiesAsStream();
		try {
			props.load(in);
		} catch (IOException e) {
			logger.error("loading properties file failed", e);
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error("Failed to close input stream.", e);
				}
			}
		}

		return props;
	}

	/**
	 * Try to load the property file in the follow order.
	 * <ol>
	 * <li>try -D JVMPARAM=xxxx</li>
	 * <li>try ~user/.APPNAME/PROPERTYFILENAME</li>
	 * <li>try OS specific (e.g. /etc/APPNAME/db.tiger.properties)</li>
	 * <li>classpath embedded version (default)</li>
	 * </ol>
	 * 
	 * @return InputStream of the property file
	 */
	protected InputStream getPropertiesAsStream() {
		String[] trySeq = new String[] {
				// first, try -D indexer.cfg=xxxx
				System.getProperty(getJvmParam()),
				// second, try ~user/.indexer/cfg.properties
				System.getProperty("user.home") + "/." + getAppName() + "/"
						+ getPropertyFileName(),
				// third, try OS specific (e.g. /etc/indexer/cfg.properties)
				getOsSpecificConfigPath() };

		for (String path : trySeq) {
			if (path == null || path.trim().isEmpty())
				continue;
			File f = new File(path);
			if (f.exists() && f.canRead()) {
				try {
					return new FileInputStream(f);
				} catch (FileNotFoundException e) {
					// strange
					logger.error("Shouldn't happened!", e);
					continue;
				}
			}
		}
		return Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(getPropertyFileName());
	}

	protected String getOsSpecificConfigPath() {
		String osName = System.getProperty("os.name");
		if (osName.toUpperCase().contains("MAC")) {
			return "/etc/" + getAppName() + "/" + getPropertyFileName();
		} else if (osName.toUpperCase().contains("NIX")
				|| osName.toUpperCase().contains("NUX")) {
			return "/etc/" + getAppName() + "/" + getPropertyFileName();
		} else if (osName.toUpperCase().contains("WIN")) {
			return "C:\\" + getAppName() + "\\" + getPropertyFileName();
		} else {
			throw new UnsupportedOperationException("OS(" + osName
					+ ") unsupported yet.!");
		}
	}

	abstract String getJvmParam();

	abstract String getPropertyFileName();

	abstract String getAppName();
}
