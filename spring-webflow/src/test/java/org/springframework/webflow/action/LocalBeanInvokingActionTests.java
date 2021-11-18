/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.action;

import junit.framework.TestCase;

import org.springframework.binding.expression.Expression;
import org.springframework.binding.method.MethodSignature;
import org.springframework.binding.method.Parameter;
import org.springframework.binding.method.Parameters;
import org.springframework.webflow.TestBean;
import org.springframework.webflow.core.DefaultExpressionParserFactory;
import org.springframework.webflow.core.collection.LocalAttributeMap;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.ScopeType;
import org.springframework.webflow.test.MockRequestContext;

/**
 * Unit test for the {@link LocalBeanInvokingAction}.
 * 
 * @author Keith Donald
 */
public class LocalBeanInvokingActionTests extends TestCase {

	private TestBean bean = new TestBean();

	private LocalBeanInvokingAction action;

	private MockRequestContext context = new MockRequestContext();

	public void setUp() {
		action = new LocalBeanInvokingAction(new MethodSignature("execute"), bean);
	}

	public void testInvokeBean() throws Exception {
		action.execute(context);
		assertTrue(bean.executed);
	}

	public void testNullTargetBean() throws Exception {
		try {
			action = new LocalBeanInvokingAction(new MethodSignature("execute"), null);
			fail("Should've failed with iae");
		} catch (IllegalArgumentException e) {

		}
	}

	public void testExposeResultInScopes() throws Exception {
		LocalAttributeMap attributes = new LocalAttributeMap();
		attributes.put("foo", "a string value");
		attributes.put("bar", "12345");
		context.setLastEvent(new Event(this, "submit", attributes));
		MethodSignature method = new MethodSignature("execute", new Parameters(new Parameter[] {
				new Parameter(String.class, expression("lastEvent.attributes.foo")),
				new Parameter(Integer.class, expression("lastEvent.attributes.bar")) }));
		action = new LocalBeanInvokingAction(method, bean);
		action.setMethodResultExposer(new ActionResultExposer("foo", ScopeType.REQUEST));
		testInvokeBean();
		assertEquals(new Integer(12345), context.getRequestScope().get("foo"));

		context.getRequestScope().clear();

		action.setMethodResultExposer(new ActionResultExposer("foo", ScopeType.FLOW));
		testInvokeBean();
		assertEquals(new Integer(12345), context.getFlowScope().get("foo"));
		assertNull(context.getRequestScope().get("foo"));
	}

	private Expression expression(String string) {
		return DefaultExpressionParserFactory.getExpressionParser().parseExpression(string);
	}
}