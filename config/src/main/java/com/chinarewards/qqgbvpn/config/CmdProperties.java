package com.chinarewards.qqgbvpn.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * cmd properties
 * 
 * @author huangwei
 * @deprecated
 */
public class CmdProperties extends LoadProperties {
	
	private Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * get server port
	 * 
	 * @return
	 */
	public String getCmdNameById(long cmdId) {
		log.debug("Properties========:"+getProperties());
		return getProperties().getProperty(String.valueOf(cmdId));
	}
	
	@Override
	String getJvmParam() {
		return "cmd.config";
	}

	@Override
	String getPropertyFileName() {
		return "cmd.properties";
	}

	@Override
	String getAppName() {
		return "cmd";
	}

}
