/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.filter;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.UnitOfWork;

/**
 * Add Unit of Work before separately when message is received or sent.
 * 
 * @author cream
 * @since 1.0.0 2011-08-29
 */
public class TransactionFilter extends IoFilterAdapter {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Inject
	Provider<UnitOfWork> uow;

	@Inject
	Provider<EntityManager> em;

	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session,
			Object message) throws Exception {

		logger.trace("messageReceived() started");
		
		startTransaction();

		try {
			nextFilter.messageReceived(session, message);
		} finally {
			endTransaction();
			logger.trace("messageReceived() done");
		}
		
	}

	@Override
	public void messageSent(NextFilter nextFilter, IoSession session,
			WriteRequest writeRequest) throws Exception {

		logger.trace("messageSent() started");
		
		startTransaction();

		try {
			nextFilter.messageSent(session, writeRequest);
		} finally {
			endTransaction();
			logger.trace("messageSent() done");
		}
	}

	/**
	 * Implements the action required to start a transaction. Current
	 * implementation start unit of work and transaction as well.
	 */
	protected void startTransaction() {
		// start unit of work and transaction.
		logger.trace("begin UnitOfWork and transaction");
		UnitOfWork u = uow.get();
		u.begin();

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
		
		UnitOfWork u = uow.get();
		EntityManager eMgr = em.get();
		EntityTransaction t = eMgr.getTransaction();

		// finish the transaction
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
				logger.warn("Exception occurred when finishing transaction", e);
			}
		}

		// ... and unit of work.
		try {
			u.end();
		} catch (Throwable e) {
			logger.warn("Exception occurred when ending UnitOfWork", e);
		}
		
		logger.trace("Transaction and UnitOfWork ended");

	}

}
