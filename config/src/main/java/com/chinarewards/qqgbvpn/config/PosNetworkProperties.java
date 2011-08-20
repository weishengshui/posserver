package com.chinarewards.qqgbvpn.config;

public class PosNetworkProperties extends LoadProperties {

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
