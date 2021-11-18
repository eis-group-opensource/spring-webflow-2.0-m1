/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.springframework.binding.collection.MapAdaptable;
import org.springframework.binding.expression.Expression;
import org.springframework.binding.expression.SettableExpression;
import org.springframework.webflow.core.collection.LocalAttributeMap;

/**
 * Unit tests for {@link WebFlowOgnlExpressionParser}.
 */
public class WebFlowOgnlExpressionParserTests extends TestCase {

	WebFlowOgnlExpressionParser parser = new WebFlowOgnlExpressionParser();

	public void testEvalSimpleExpression() {
		ArrayList list = new ArrayList();
		Expression exp = parser.parseExpression("size()");
		Integer size = (Integer) exp.evaluate(list, null);
		assertEquals(0, size.intValue());
	}

	public void testEvalMapAdaptable() {
		MapAdaptable adaptable = new MapAdaptable() {
			public Map asMap() {
				HashMap map = new HashMap();
				map.put("size", new Integer(0));
				return map;
			}
		};
		Expression exp = parser.parseExpression("size");
		Integer size = (Integer) exp.evaluate(adaptable, null);
		assertEquals(0, size.intValue());
	}

	public void testEvalAndSetMutableMap() {
		LocalAttributeMap map = new LocalAttributeMap();
		map.put("size", new Integer(0));
		Expression exp = parser.parseExpression("size");
		Integer size = (Integer) exp.evaluate(map, null);
		assertEquals(0, size.intValue());
		assertTrue(exp instanceof SettableExpression);
		SettableExpression sexp = (SettableExpression) exp;
		sexp.evaluateToSet(map, new Integer(1), null);
		size = (Integer) exp.evaluate(map, null);
		assertEquals(1, size.intValue());
	}
}
