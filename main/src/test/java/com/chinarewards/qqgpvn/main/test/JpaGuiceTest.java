/**
 * 
 */
package com.chinarewards.qqgpvn.main.test;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.google.inject.persist.PersistService;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public abstract class JpaGuiceTest extends GuiceTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();

		EntityManager em = getInjector().getInstance(EntityManager.class);
		em.getTransaction().begin();
	}

	@After
	public void tearDown() throws Exception {

		EntityManager em = getInjector().getInstance(EntityManager.class);
		em.getTransaction().rollback();
	}

	@BeforeClass
	public void setUpBeforeClass() throws Exception {
		// get the persistence service
		PersistService ps = getInjector().getInstance(PersistService.class);
		ps.start();
	}

	@AfterClass
	public void tearDownAfterClass() throws Exception {
		// get the persistence service
		PersistService ps = getInjector().getInstance(PersistService.class);
		ps.stop();
	}

}
