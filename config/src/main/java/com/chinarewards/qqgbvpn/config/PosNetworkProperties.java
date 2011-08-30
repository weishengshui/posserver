package com.chinarewards.qqgbvpn.config;

public class PosNetworkProperties extends LoadProperties {

	private static final String SERVER_PORT = "server.port";
<<<<<<< HEAD
	
	private static final String TXSERVER_KEY = "txserver.key";
=======
>>>>>>> refs/remotes/origin/master

	/**
	 * get server port
	 * 
	 * @return
	 */
	public int getSearverPort() {
		return Integer.valueOf(getProperties().getProperty(SERVER_PORT));
<<<<<<< HEAD
	}
	
	public String getTxServerKey(){
		return getProperties().getProperty(TXSERVER_KEY);
=======
>>>>>>> refs/remotes/origin/master
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
