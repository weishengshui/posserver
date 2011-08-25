package com.chinarewards.qqgbvpn.config;

public class URLProperties extends LoadProperties {

	@Override
	String getJvmParam() {
		return "db.config";
	}

	@Override
	String getPropertyFileName() {
		return "url.properties";
	}

	@Override
	String getAppName() {
		return "posnet";
	}

}
