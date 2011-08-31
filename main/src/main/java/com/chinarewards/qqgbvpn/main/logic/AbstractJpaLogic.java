/**
 * 
 */
package com.chinarewards.qqgbvpn.main.logic;

import javax.persistence.EntityManager;

import com.google.inject.Provider;

/**
 * Abstract implementation of logic requiring JPA access.
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class AbstractJpaLogic extends BaseLogic {

	protected final Provider<EntityManager> em;

	/**
	 * Constructor.
	 * 
	 * @param em
	 */
	public AbstractJpaLogic(Provider<EntityManager> em) {
		this.em = em;
	}

	public EntityManager getEm() {
		return em.get();
	}

}
