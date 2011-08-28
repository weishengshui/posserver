package com.chinarewards.qqgbvpn.config;

public class PosNetworkProperties extends LoadProperties {

	private static final String SERVER_PORT = "server.port";

	/**
	 * get server port
	 * 
	 * @return
	 */
	public int getSearverPort() {
		return Integer.valueOf(getProperties().getProperty(SERVER_PORT));
	}
	
	@Override
	String getJvmParam() {
		return "posnet.config";
	}

	@Override
	String getPropertyFileName() {
		return "posnet.properties";
	}

	@Override
	String getAppName() {
		return "posnet";
	}

}
