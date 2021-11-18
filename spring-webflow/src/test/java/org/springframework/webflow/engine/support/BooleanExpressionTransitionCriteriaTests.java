/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.support;

import junit.framework.TestCase;

import org.springframework.binding.expression.Expression;
import org.springframework.binding.expression.ExpressionParser;
import org.springframework.webflow.core.DefaultExpressionParserFactory;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.test.MockRequestContext;

/**
 * Unit tests for {@link org.springframework.webflow.engine.support.BooleanExpressionTransitionCriteria}.
 */
public class BooleanExpressionTransitionCriteriaTests extends TestCase {

	private ExpressionParser parser = DefaultExpressionParserFactory.getExpressionParser();

	public void testMatchCriteria() {
		Expression exp = parser.parseExpression("${requestScope.flag}");
		BooleanExpressionTransitionCriteria c = new BooleanExpressionTransitionCriteria(exp);
		MockRequestContext context = new MockRequestContext();
		context.getRequestScope().put("flag", Boolean.TRUE);
		assertEquals(true, c.test(context));
	}

	public void testNotABoolean() {
		Expression exp = parser.parseExpression("${requestScope.flag}");
		BooleanExpressionTransitionCriteria c = new BooleanExpressionTransitionCriteria(exp);
		MockRequestContext context = new MockRequestContext();
		context.getRequestScope().put("flag", "foo");
		try {
			c.test(context);
			fail("not a boolean");
		} catch (IllegalArgumentException e) {
		}
	}

	public void testResult() {
		Expression exp = parser.parseExpression("${#result == 'foo'}");
		BooleanExpressionTransitionCriteria c = new BooleanExpressionTransitionCriteria(exp);
		MockRequestContext context = new MockRequestContext();
		context.setLastEvent(new Event(this, "foo"));
		assertEquals(true, c.test(context));
	}

	public void testFunctionInvocation() {
		Expression exp = parser.parseExpression("${#result.endsWith('error')}");
		BooleanExpressionTransitionCriteria c = new BooleanExpressionTransitionCriteria(exp);
		MockRequestContext context = new MockRequestContext();
		context.setLastEvent(new Event(this, "error"));
		assertTrue(c.test(context));
	}
}