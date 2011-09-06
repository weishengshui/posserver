/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.chinarewards.qqgbpvn.core.test.GuiceTest;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.impl.ServiceDispatcherException;
import com.chinarewards.qqgbvpn.main.protocol.impl.ServiceRequestImpl;
import com.chinarewards.qqgbvpn.main.protocol.impl.ServiceResponseImpl;
import com.chinarewards.qqgbvpn.main.protocol.impl.SimpleServiceDispatcher;
import com.chinarewards.qqgbvpn.main.protocol.impl.SimpleServiceHandlerObjectFactory;
import com.chinarewards.qqgbvpn.main.protocol.impl.SimpleServiceMapping;
import com.chinarewards.qqgbvpn.main.protocol.impl.SimpleSession;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class DispatcherTest extends GuiceTest {

	public static class TestRequestCmd implements ICommand {

		private String name;

		private int i;

		public TestRequestCmd(String name, int i) {
			this.name = name;
			this.i = i;
		}

		@Override
		public long getCmdId() {
			return 654;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the i
		 */
		public int getI() {
			return i;
		}

	}

	public static class TestResponseCmd implements ICommand {

		private String name;

		public TestResponseCmd(String name) {
			this.name = name;
		}

		@Override
		public long getCmdId() {
			return 6858;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

	}

	public static class TestServiceHandler implements ServiceHandler {

		@Override
		public void execute(ServiceRequest request, ServiceResponse response) {

			TestRequestCmd msg = (TestRequestCmd) request.getParameter();

			String name = "Result: " + msg.getI() + " - " + msg.getName();

			TestResponseCmd o = new TestResponseCmd(name);
			response.writeResponse(o);

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinarewards.qqgbpvn.main.test.JpaGuiceTest#setUp()
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinarewards.qqgbpvn.main.test.JpaGuiceTest#tearDown()
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Most simple case for dispatching a command.
	 */
	@Test
	public void testDispatch_Simple_OK() throws Exception {

		// fake a message
		TestRequestCmd req = new TestRequestCmd("john", 444);

		// Construct a service mapping
		ServiceMapping mapping = new SimpleServiceMapping();
		mapping.addMapping(654, TestServiceHandler.class);

		// and the corresponding ServiceHandlerObjectFactory
		SimpleServiceHandlerObjectFactory objFactory = new SimpleServiceHandlerObjectFactory(
				mapping);

		// get an instance of dispatcher using that object factory
		ServiceDispatcher dispatcher = new SimpleServiceDispatcher(objFactory);

		// build a request (for dispatcher)
		ServiceSession session = new SimpleSession();
		ServiceRequestImpl request = new ServiceRequestImpl(req, session);
		// build a response (for dispatcher)
		ServiceResponseImpl response = new ServiceResponseImpl();

		// and dispatch the message!
		dispatcher.dispatch(mapping, request, response);

		//
		// Lots of validation here.
		//
		assertNotNull(response.getResponse());
		assertTrue(response.getResponse() instanceof TestResponseCmd);
		TestResponseCmd respObj = (TestResponseCmd) response.getResponse();
		assertEquals("Result: 444 - john", respObj.getName());

	}


	/**
	 * Most simple case for dispatching a command with no mapping.
	 */
	@Test
	public void testDispatch_NoMapping() throws Exception {

		// fake a message
		TestRequestCmd req = new TestRequestCmd("john", 444);

		// Construct a service mapping
		ServiceMapping mapping = new SimpleServiceMapping();
		mapping.addMapping(654 + 777, TestServiceHandler.class);	// non exist mapping

		// and the corresponding ServiceHandlerObjectFactory
		SimpleServiceHandlerObjectFactory objFactory = new SimpleServiceHandlerObjectFactory(
				mapping);

		// get an instance of dispatcher using that object factory
		ServiceDispatcher dispatcher = new SimpleServiceDispatcher(objFactory);

		// build a request (for dispatcher)
		ServiceSession session = new SimpleSession();
		ServiceRequestImpl request = new ServiceRequestImpl(req, session);
		// build a response (for dispatcher)
		ServiceResponseImpl response = new ServiceResponseImpl();

		// and dispatch the message!
		try {
			dispatcher.dispatch(mapping, request, response);
			fail("Should throw ServiceDispatcherException");
		} catch (ServiceDispatcherException e) {
			// correct
		}

	}
}
