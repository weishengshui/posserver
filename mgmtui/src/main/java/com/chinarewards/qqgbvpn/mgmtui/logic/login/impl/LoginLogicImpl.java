/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.logic.login.impl;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.domain.event.DomainEntity;
import com.chinarewards.qqgbvpn.domain.event.DomainEvent;
import com.chinarewards.qqgbvpn.logic.journal.JournalLogic;
import com.chinarewards.qqgbvpn.mgmtui.dao.SysUserDao;
import com.chinarewards.qqgbvpn.mgmtui.logic.login.LoginLogic;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

/**
 * @author cream
 * 
 */
public class LoginLogicImpl implements LoginLogic {

	Logger log = LoggerFactory.getLogger(getClass());
	
	@Inject
	SysUserDao userDao;
	
	@Inject
	JournalLogic journalLogic;

	@Transactional
	@Override
	public boolean checkLogin(String username, String password, String ipAddr) {

		// check if user can login
		boolean loginOk = userDao.checkLogin(username, password);
		
		//
		// Do logging. (only if logged in)
		//
		if (loginOk) {
			// construct the eventDetail
			ObjectMapper m = new ObjectMapper();
			ObjectNode n = m.createObjectNode();
			n.put("username", username);
			n.put("ip", ipAddr);
			String eventDetail = null;
			try {
				eventDetail = m.writeValueAsString(n);
				journalLogic.logEvent(DomainEvent.USER_LOGGED_IN, DomainEntity.USER, username, eventDetail);
				// XXX log down this event.
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return loginOk;
	}

}
