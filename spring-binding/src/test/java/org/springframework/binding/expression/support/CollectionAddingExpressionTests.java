/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.expression.support;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.springframework.binding.expression.Expression;
import org.springframework.binding.expression.ExpressionParser;

/**
 * Unit tests for {@link org.springframework.binding.expression.support.CollectionAddingExpression}.
 */
public class CollectionAddingExpressionTests extends TestCase {

	ExpressionParser parser = new BeanWrapperExpressionParser();

	TestBean bean = new TestBean();

	Expression exp = parser.parseExpression("list");

	public void testEvaluation() {
		ArrayList list = new ArrayList();
		bean.setList(list);
		CollectionAddingExpression colExp = new CollectionAddingExpression(exp);
		assertSame(list, colExp.evaluate(bean, null));
	}

	public void testAddToCollection() {
		CollectionAddingExpression colExp = new CollectionAddingExpression(exp);
		colExp.evaluateToSet(bean, "1", null);
		colExp.evaluateToSet(bean, "2", null);
		assertEquals("1", bean.getList().get(0));
		assertEquals("2", bean.getList().get(1));
	}

	public void testNotACollection() {
		Expression exp = parser.parseExpression("flag");
		CollectionAddingExpression colExp = new CollectionAddingExpression(exp);
		try {
			colExp.evaluateToSet(bean, "1", null);
			fail("not a collection");
		} catch (IllegalArgumentException e) {
		}
	}

	public void testNoAddOnNullValue() {
		CollectionAddingExpression colExp = new CollectionAddingExpression(exp);
		colExp.evaluateToSet(bean, null, null);
		colExp.evaluateToSet(bean, "2", null);
		assertEquals("2", bean.getList().get(0));
	}
}