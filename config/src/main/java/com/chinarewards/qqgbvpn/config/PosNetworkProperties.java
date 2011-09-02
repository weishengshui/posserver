package com.chinarewards.qqgbvpn.config;

/**
 * 
 * 
 * @deprecated
 */
public class PosNetworkProperties extends LoadProperties {

	private static final String SERVER_PORT = "server.port";
	
	private static final String TXSERVER_KEY = "txserver.key";

	/**
	 * get server port
	 * 
	 * @return
	 */
	public int getSearverPort() {
		return Integer.valueOf(getProperties().getProperty(SERVER_PORT));
	}
	
	public String getTxServerKey(){
		return getProperties().getProperty(TXSERVER_KEY);
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
