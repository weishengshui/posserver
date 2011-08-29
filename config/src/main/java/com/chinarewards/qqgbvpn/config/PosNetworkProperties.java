package com.chinarewards.qqgbvpn.config;

public class PosNetworkProperties extends LoadProperties {

	private static final String SERVER_PORT = "server.port";

	private static final String SECRET_FILE = "secretfile";

	/**
	 * get server port
	 * 
	 * @return
	 */
	public int getSearverPort() {
		return Integer.valueOf(getProperties().getProperty(SERVER_PORT));
	}

	public String getSecretFilePaht() {
		return getProperties().getProperty(SECRET_FILE);
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
