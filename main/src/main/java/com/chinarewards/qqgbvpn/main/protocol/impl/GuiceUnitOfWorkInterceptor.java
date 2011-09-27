/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.UnitOfWork;

/**
 * Simple implementation of <code>ServiceDispatcher</code>.
 * 
 * @author kmtong
 * @since 0.1.0
 */
public class GuiceUnitOfWorkInterceptor implements MethodInterceptor {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Inject
	protected UnitOfWork uow;

	@Inject
	Provider<EntityManager> em;

	/**
	 * Implements the action required to start a transaction. Current
	 * implementation start unit of work and transaction as well.
	 */
	protected void startTransaction() {
		// start unit of work and transaction.
		logger.trace("begin UnitOfWork and transaction");
		uow.begin();

		EntityManager e = em.get();
		EntityTransaction t = e.getTransaction();
		t.begin();

		logger.trace("UnitOfWork and transaction begun");
	}

	/**
	 * Implements the action required to start a transaction. Current
	 * implementation rollback the transaction if it is marked as rollback, or
	 * commit it otherwise. Finally it call UnitOfWork.end().
	 */
	protected void endTransaction() {

		logger.trace("Going to end transaction and UnitOfWork");

		EntityManager eMgr = em.get();
		EntityTransaction t = eMgr.getTransaction();

		// finish the transaction
		try {
			if (t.isActive()) {
				try {
					if (t.getRollbackOnly()) {
						logger.debug("Transaction is marked for ROLLBACK, so we rollback it.");
						t.rollback();
					} else {
						// otherwise, commit it.
						t.commit();
					}
				} catch (Throwable e) {
					logger.warn(
							"Exception occurred when finishing transaction", e);
				}
			}
		} catch (Throwable e) {
			logger.warn("An error has occurred when ending transaction", e);
		}

		// ... and unit of work.
		try {
			uow.end();
		} catch (Throwable e) {
			logger.warn("Exception occurred when ending UnitOfWork", e);
		}

		logger.trace("Transaction and UnitOfWork ended");

	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		startTransaction();
		try {
			return invocation.proceed();
		} finally {
			endTransaction();
		}
	}

}
