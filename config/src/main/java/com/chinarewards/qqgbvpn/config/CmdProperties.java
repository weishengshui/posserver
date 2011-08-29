package com.chinarewards.qqgbvpn.config;

/**
 * cmd properties
 * 
 * @author huangwei
 *
 */
public class CmdProperties extends LoadProperties {

	/**
	 * get server port
	 * 
	 * @return
	 */
	public String getCmdNameById(long cmdId) {
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
