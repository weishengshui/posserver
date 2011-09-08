/**
 * 
 */
package com.chinarewards.qqgbvpn.core.jpa;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.chinarewards.qqgbvpn.core.JpaBase;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class DbChecker extends JpaBase {

	boolean hasError = false;
	
	public void check() {
		
		EntityManager em = getEm();
		
		Query q = em.createNativeQuery("SELECT COUNT(*) FROM changelog");
		
	}
	
	protected String getChangeLogTableName() {
		return "changelog";
	}
	
	protected void resetState() {
		hasError = false;
	}

	public boolean hasError() {
		return hasError;
	}

}
