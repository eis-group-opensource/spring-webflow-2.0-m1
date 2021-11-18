/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.method;

import junit.framework.TestCase;

import org.springframework.binding.expression.support.StaticExpression;

/**
 * Unit tests for {@link org.springframework.binding.method.MethodInvoker}.
 * 
 * @author Erwin Vervaet
 */
public class MethodInvokerTests extends TestCase {

	private MethodInvoker methodInvoker;

	protected void setUp() throws Exception {
		this.methodInvoker = new MethodInvoker();
	}

	public void testInvocationTargetException() {
		try {
			methodInvoker.invoke(new MethodSignature("test"), new TestObject(), null);
			fail();
		} catch (MethodInvocationException e) {
			assertTrue(e.getTargetException() instanceof IllegalArgumentException);
			assertEquals("just testing", e.getTargetException().getMessage());
		}
	}

	public void testInvalidMethod() {
		try {
			methodInvoker.invoke(new MethodSignature("bogus"), new TestObject(), null);
			fail();
		} catch (MethodInvocationException e) {
			assertTrue(e.getTargetException() instanceof InvalidMethodKeyException);
		}
	}

	public void testBeanArg() {
		Parameters parameters = new Parameters();
		Bean bean = new Bean();
		parameters.add(new Parameter(Bean.class, new StaticExpression(bean)));
		MethodSignature method = new MethodSignature("testBeanArg", parameters);
		assertSame(bean, methodInvoker.invoke(method, new TestObject(), null));
	}

	public void testPrimitiveArg() {
		Parameters parameters = new Parameters();
		parameters.add(new Parameter(Boolean.class, new StaticExpression(Boolean.TRUE)));
		MethodSignature method = new MethodSignature("testPrimitiveArg", parameters);
		assertEquals(Boolean.TRUE, methodInvoker.invoke(method, new TestObject(), null));
	}

	private static class TestObject {

		public void test() {
			throw new IllegalArgumentException("just testing");
		}

		public Object testBeanArg(Bean bean) {
			return bean;
		}

		public boolean testPrimitiveArg(boolean primitive) {
			return primitive;
		}
	}

	private static class Bean {
		String value;
	}
}
