/**
 * 
 */
package com.chinarewards.qqgbpvn.main.test;

import org.junit.After;
import org.junit.Before;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public abstract class GuiceTest extends BaseTest {

	private Injector injector;

	/**
	 * Creates an instance of Guice injector with prepared modules returned by
	 * {@link #getModules()}.
	 * 
	 * @return
	 */
	protected Injector createInjector() {
		Module[] modules = getModules();
		if (modules == null) {
			// prevent NullPointerException
			modules = new Module[0];
		}
		injector = Guice.createInjector(modules);
		return injector;
	}

	/**
	 * 
	 * @return
	 */
	public Injector getInjector() {
		return injector;
	}

	/**
	 * Returns a list of modules for Guice injector creation.
	 * 
	 * @return
	 */
	protected Module[] getModules() {
		return null;
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();
		createInjector();
	}

	/* (non-Javadoc)
	 * @see com.chinarewards.qqgbpvn.main.test.BaseTest#tearDown()
	 */
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
	
}
