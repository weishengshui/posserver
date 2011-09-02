package com.chinarewards.qqgbvpn.config;

/**
 * 
 * 
 * @deprecated
 */
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
