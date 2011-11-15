package com.chinarewards.qqgbvpn.main.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.PosServer;
import com.chinarewards.qqgbvpn.main.SessionStore;

public class CleanExpiredServerSessionTask implements Runnable {
	
	protected Logger log = LoggerFactory.getLogger(PosServer.class);
	
	protected final SessionStore sessionStore;
	
	public CleanExpiredServerSessionTask(SessionStore sessionStore){
		this.sessionStore = sessionStore;
		
	}

	@Override
	public void run() {
		log.info("clean expired server session timer start...");
		sessionStore.expiredSession();
		log.info("clean expired server session timer end...");
	}
	
}
