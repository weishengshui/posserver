package com.chinarewards.qqgbvpn.core;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * A base class which needs JPA entity manager for data access.
 * <p>
 * 
 * This class expects a <code>Provider&lt;EntityManager&gt;</code> to be
 * injected.
 * <p>
 * 
 * Subclasses can call <code>getEm()</code> to gain an instance of
 * <code>EntityManager</code>.
 * 
 * @since 0.1.0
 */
public abstract class JpaBase {

	protected Logger log = LoggerFactory.getLogger(getClass());

	@Inject
	private Provider<EntityManager> em;

	/**
	 * Returns an instance of <code>EntityManager</code>.
	 * 
	 * @return an instance of <code>EntityManager</code>.
	 */
	protected EntityManager getEm() {
		return em.get();
	}

}
