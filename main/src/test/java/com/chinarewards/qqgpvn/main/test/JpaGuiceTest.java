/**
 * 
 */
package com.chinarewards.qqgpvn.main.test;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public abstract class JpaGuiceTest extends GuiceTest {

	protected EntityManager em;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();

		em = getInjector().getInstance(EntityManager.class);
		em.getTransaction().begin();
	}

	@After
	public void tearDown() throws Exception {

		em.getTransaction().rollback();
	}

}
