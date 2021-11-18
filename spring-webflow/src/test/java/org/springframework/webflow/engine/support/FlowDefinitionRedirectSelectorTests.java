/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.support;

import junit.framework.TestCase;

import org.springframework.binding.expression.Expression;
import org.springframework.binding.expression.ExpressionParser;
import org.springframework.webflow.core.DefaultExpressionParserFactory;
import org.springframework.webflow.execution.ViewSelection;
import org.springframework.webflow.execution.support.FlowDefinitionRedirect;
import org.springframework.webflow.test.MockRequestContext;

public class FlowDefinitionRedirectSelectorTests extends TestCase {
	ExpressionParser parser = DefaultExpressionParserFactory.getExpressionParser();

	public void testMakeSelection() {
		Expression exp = parser.parseExpression("${requestScope.flowIdVar}?a=b&c=${requestScope.bar}");
		FlowDefinitionRedirectSelector selector = new FlowDefinitionRedirectSelector(exp);
		MockRequestContext context = new MockRequestContext();
		context.getRequestScope().put("flowIdVar", "foo");
		context.getRequestScope().put("bar", "baz");
		ViewSelection selection = selector.makeEntrySelection(context);
		assertTrue(selection instanceof FlowDefinitionRedirect);
		FlowDefinitionRedirect redirect = (FlowDefinitionRedirect) selection;
		assertEquals("foo", redirect.getFlowDefinitionId());
		assertEquals("b", redirect.getExecutionInput().get("a"));
		assertEquals("baz", redirect.getExecutionInput().get("c"));
	}

	public void testMakeSelectionInvalidVariable() {
		Expression exp = parser.parseExpression("${flowScope.flowId}");
		FlowDefinitionRedirectSelector selector = new FlowDefinitionRedirectSelector(exp);
		MockRequestContext context = new MockRequestContext();
		try {
			ViewSelection selection = selector.makeEntrySelection(context);
			assertTrue(selection instanceof FlowDefinitionRedirect);
		} catch (IllegalStateException e) {

		}
	}
}