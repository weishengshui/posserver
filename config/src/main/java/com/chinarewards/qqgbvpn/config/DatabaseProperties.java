package com.chinarewards.qqgbvpn.config;

public class DatabaseProperties extends LoadProperties {

	@Override
	String getJvmParam() {
		return "db.config";
	}

	@Override
	String getPropertyFileName() {
		return "db.properties";
	}

	@Override
	String getAppName() {
		return "posnet";
	}

}
