/**
 * 
 */
package com.chinarewards.qqgbvpn.config;

import java.io.File;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

import com.chinarewards.qqgbvpn.common.HomeDirLocator;
import com.google.inject.Inject;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class ConfigReader {

	private final HomeDirLocator homeDirLocator;

	/**
	 * 
	 * @param homeDirLocator
	 *            the home directory locator.
	 */
	@Inject
	public ConfigReader(HomeDirLocator homeDirLocator) {
		this.homeDirLocator = homeDirLocator;
	}

	/**
	 * Read the configuration from the specified filename.
	 * 
	 * @param filename
	 * @return
	 * @throws ConfigurationException
	 */
	public Configuration read(String filename) throws ConfigurationException {

		// get home directory
		String dir = homeDirLocator.getHomeDir();
		if (dir == null) {
			return null;
		}

		// if conf/ subdirectory is specified.
		File f = new File(dir);
		if (getConfSubdirectory() != null) {
			f = new File(dir, getConfSubdirectory());
		}

		// get the target config file.
		Configuration conf = buildConfigObject(new File(f, filename));

		return conf;

	}
	
	public String getConfSubdirectory() {
		return "conf";
	}

	/**
	 * Build a configuration object with the given absolute file. Supported
	 * configuration file formats are:
	 * 
	 * <ul>
	 * <li>.INI file</li>
	 * </ul>
	 * 
	 * @param absFile
	 * @return the built configuration, or <code>null</code> if the file is not
	 *         a supported configuration file.
	 * @throws ConfigurationException
	 */
	protected Configuration buildConfigObject(File absFile)
			throws ConfigurationException {

		String ext = getFileExtension(absFile);

		if (ext == null)
			return null;

		// TODO extract this code.
		if ("ini".equals(ext)) {
			PropertiesConfiguration p = new PropertiesConfiguration(absFile.getAbsoluteFile());
			p.setReloadingStrategy(new FileChangedReloadingStrategy());
			p.setDelimiterParsingDisabled(true);	// no delimiter is used in the config value
			return p;
		}

		// not supported
		return null;

	}

	/**
	 * Returns the file extension of the file. This method examine the filename
	 * and extract the part of string with the dot '.' character.
	 * 
	 * @param absFile
	 * @return the file extension, or <code>null</code> if not exists.
	 */
	protected String getFileExtension(File absFile) {

		String filename = absFile.getName();

		// extract the extension part (.zzz)

		int index = filename.indexOf(".");
		if (index == -1) {
			return null;
		}
		String ext = filename.substring(index + 1);
		if ("".equals(ext)) {
			return null;
		}

		return ext;

	}

}
