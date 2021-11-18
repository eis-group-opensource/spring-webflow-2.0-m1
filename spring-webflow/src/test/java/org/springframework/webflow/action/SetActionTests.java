/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.action;

import junit.framework.TestCase;

import org.springframework.binding.expression.Expression;
import org.springframework.binding.expression.ExpressionParser;
import org.springframework.binding.expression.SettableExpression;
import org.springframework.webflow.TestBean;
import org.springframework.webflow.TestBeanWithMap;
import org.springframework.webflow.core.DefaultExpressionParserFactory;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.ScopeType;
import org.springframework.webflow.test.MockRequestContext;

/**
 * Unit tests for {@link SetAction}.
 */
public class SetActionTests extends TestCase {

	private ExpressionParser parser = DefaultExpressionParserFactory.getExpressionParser();

	private MockRequestContext context = new MockRequestContext();

	public void testSetActionWithBooleanValue() throws Exception {
		context.getFlowScope().put("bean", new TestBean());

		SettableExpression attr = parser.parseSettableExpression("bean.executed");
		Expression value = parser.parseExpression("true");
		SetAction action = new SetAction(attr, ScopeType.FLOW, value);
		Event outcome = action.execute(context);
		assertEquals("success", outcome.getId());
		assertEquals(true, ((TestBean) context.getFlowScope().get("bean")).executed);
	}

	public void testSetActionWithStringValue() throws Exception {
		SettableExpression attr = parser.parseSettableExpression("backState");
		Expression value = parser.parseExpression("'otherState'"); // ${'otherState'} also works
		SetAction action = new SetAction(attr, ScopeType.FLOW, value);
		assertEquals("success", action.execute(context).getId());
		assertEquals("otherState", context.getFlowScope().get("backState"));
	}

	public void testSetActionWithValueFromMap() throws Exception {
		TestBeanWithMap beanWithMap = new TestBeanWithMap();
		beanWithMap.getMap().put("key1", "value1");
		beanWithMap.getMap().put("key2", "value2");
		context.getFlowScope().put("beanWithMap", beanWithMap);

		SettableExpression attr = parser.parseSettableExpression("key");
		Expression value = parser.parseExpression("${flowScope.beanWithMap.map['key1']}");
		SetAction action = new SetAction(attr, ScopeType.FLASH, value);
		assertEquals("success", action.execute(context).getId());
		assertEquals("value1", context.getFlashScope().get("key"));
	}
}
