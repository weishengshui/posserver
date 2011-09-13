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
 * Add Unit of Work.
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
		logger.debug("begin UnitOfWork");
		UnitOfWork u = uow.get();
		u.begin();
		EntityManager e = em.get();
		EntityTransaction t = e.getTransaction();
		t.begin();
		try {
			nextFilter.messageReceived(session, message);
		} finally {
			logger.debug("end UnitOfWork");
			if (t.isActive()) {
				if (t.getRollbackOnly()) {
					logger.info("ROLLBACK");
					t.rollback();
				} else {
					t.commit();
				}
			}
			u.end();
		}
	}

	@Override
	public void messageSent(NextFilter nextFilter, IoSession session,
			WriteRequest writeRequest) throws Exception {

		logger.debug("begin UnitOfWork");
		UnitOfWork u = uow.get();
		u.begin();
		EntityManager e = em.get();
		EntityTransaction t = e.getTransaction();
		t.begin();
		try {
			nextFilter.messageSent(session, writeRequest);
		} finally {
			logger.debug("end UnitOfWork");
			if (t.isActive()) {
				if (t.getRollbackOnly()) {
					logger.info("ROLLBACK");
					t.rollback();
				} else {
					t.commit();
				}
			}
			u.end();
		}
	}

}
