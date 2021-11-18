/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.builder;

import junit.framework.TestCase;

import org.springframework.binding.convert.ConversionException;
import org.springframework.webflow.engine.TransitionCriteria;
import org.springframework.webflow.engine.WildcardTransitionCriteria;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.test.MockFlowServiceLocator;
import org.springframework.webflow.test.MockRequestContext;

/**
 * Test case for {@link TextToTransitionCriteria}.
 */
public class TextToTransitionCriteriaTests extends TestCase {

	private MockFlowServiceLocator serviceLocator = new MockFlowServiceLocator();
	private TextToTransitionCriteria converter = new TextToTransitionCriteria(serviceLocator);

	public void testAny() {
		String expression = "*";
		TransitionCriteria criterion = (TransitionCriteria) converter.convert(expression);
		RequestContext ctx = getRequestContext();
		assertTrue("Criterion should evaluate to true", criterion.test(ctx));

		assertSame(WildcardTransitionCriteria.INSTANCE, converter.convert("*"));
		assertSame(WildcardTransitionCriteria.INSTANCE, converter.convert(""));
	}

	public void testStaticEventId() {
		String expression = "sample";
		TransitionCriteria criterion = (TransitionCriteria) converter.convert(expression);
		RequestContext ctx = getRequestContext();
		assertTrue("Criterion should evaluate to true", criterion.test(ctx));
	}

	public void testTrueEvaluation() throws Exception {
		String expression = "${flowScope.foo == 'bar'}";
		TransitionCriteria criterion = (TransitionCriteria) converter.convert(expression);
		RequestContext ctx = getRequestContext();
		assertTrue("Criterion should evaluate to true", criterion.test(ctx));
	}

	public void testFalseEvaluation() throws Exception {
		String expression = "${flowScope.foo != 'bar'}";
		TransitionCriteria criterion = (TransitionCriteria) converter.convert(expression);
		RequestContext ctx = getRequestContext();
		assertFalse("Criterion should evaluate to false", criterion.test(ctx));
	}

	public void testNonBooleanEvaluation() throws Exception {
		String expression = "${flowScope.foo}";
		TransitionCriteria criterion = (TransitionCriteria) converter.convert(expression);
		RequestContext ctx = getRequestContext();
		try {
			criterion.test(ctx);
			fail("Non-boolean evaluations are not allowed");
		} catch (IllegalArgumentException e) {
			// success
		}
	}

	public void testInvalidSyntax() throws Exception {
		try {
			String expression = "${&foo<<m}";
			converter.convert(expression);
			fail("Syntax error should throw ExpressionSyntaxException");
		} catch (ConversionException ex) {
			// success
		}
	}

	public void testEventId() throws Exception {
		String expression = "${lastEvent.id == 'sample'}";
		TransitionCriteria criterion = (TransitionCriteria) converter.convert(expression);
		RequestContext ctx = getRequestContext();
		assertTrue("Criterion should evaluate to true", criterion.test(ctx));
		expression = "${#result == 'sample'}";
		criterion = (TransitionCriteria) converter.convert(expression);
		assertTrue("Criterion should evaluate to true", criterion.test(ctx));
	}

	public void testBean() {
		TransitionCriteria myTransitionCriteria = new TransitionCriteria() {
			public boolean test(RequestContext context) {
				return false;
			}
		};
		serviceLocator.registerBean("myTransitionCriteria", myTransitionCriteria);
		TransitionCriteria criteria = (TransitionCriteria) converter.convert("bean:myTransitionCriteria");
		assertSame(myTransitionCriteria, criteria);
	}

	private RequestContext getRequestContext() {
		MockRequestContext ctx = new MockRequestContext();
		ctx.getFlowScope().put("foo", "bar");
		ctx.setLastEvent(new Event(this, "sample"));
		return ctx;
	}
}