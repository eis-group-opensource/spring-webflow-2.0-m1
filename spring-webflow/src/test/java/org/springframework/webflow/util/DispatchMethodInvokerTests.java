/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.util;

import junit.framework.TestCase;

/**
 * Unit tests for {@link DispatchMethodInvoker}.
 * 
 * @author Ben Hale
 */
public class DispatchMethodInvokerTests extends TestCase {

	private class MockClass {
		private boolean methodCalled = false;

		public boolean getMethodCalled() {
			return methodCalled;
		}

		public void argumentMethod(Object o) {
			methodCalled = true;
		}

		public void noArgumentMethod() {
			methodCalled = true;
		}

		public void exceptionMethod(Object o) throws Exception {
			throw new Exception("expected exception");
		}
	}

	private MockClass mockClass;

	protected void setUp() {
		mockClass = new MockClass();
	}

	public void testInvokeWithExplicitParameters() throws Exception {
		DispatchMethodInvoker invoker = new DispatchMethodInvoker(mockClass, new Class[] { Object.class });
		invoker.invoke("argumentMethod", new Object[] { "testValue" });
		assertTrue("Method should have been called successfully", mockClass.getMethodCalled());
	}

	public void testInvokeWithAssignableParameters() throws Exception {
		DispatchMethodInvoker invoker = new DispatchMethodInvoker(mockClass, new Class[] { String.class });
		invoker.invoke("argumentMethod", new Object[] { "testValue" });
		assertTrue("Method should have been called successfully", mockClass.getMethodCalled());
	}

	public void testInvokeWithNoParameters() throws Exception {
		DispatchMethodInvoker invoker = new DispatchMethodInvoker(mockClass, new Class[0]);
		invoker.invoke("noArgumentMethod", new Object[0]);
		assertTrue("Method should have been called successfully", mockClass.getMethodCalled());
	}

	public void testInvokeWithException() {
		DispatchMethodInvoker invoker = new DispatchMethodInvoker(mockClass, new Class[] { Object.class });
		try {
			invoker.invoke("exceptionMethod", new Object[] { "testValue" });
			fail("Should have thrown an exception");
		} catch (Exception e) {
		}
	}

}
