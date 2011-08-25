/**
 * 
 */
package com.chinarewards.qqgbvpn.main.logic.journal;

import java.util.Date;

import javax.persistence.EntityManager;

import com.chinarewards.qqgbvpn.common.DateTimeProvider;
import com.chinarewards.qqgbvpn.main.logic.AbstractJpaLogic;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class DbJournalLogic extends AbstractJpaLogic implements JournalLogic {

	protected final DateTimeProvider dtProvider;

	@Inject
	public DbJournalLogic(DateTimeProvider dtProvider,
			Provider<EntityManager> em) {
		super(em);
		this.dtProvider = dtProvider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.chinarewards.qqgbvpn.main.logic.journal.JournalLogic#logEvent(java
	 * .lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Transactional
	public void logEvent(String event, String domain, String entityId,
			String detail) {

		Date now = dtProvider.getTime();

		// FIXME implements me

		// start new transaction on top of this transaction.

		// save the entity

		// close the new transaction

		throw new UnsupportedOperationException("implements me");

	}

}
