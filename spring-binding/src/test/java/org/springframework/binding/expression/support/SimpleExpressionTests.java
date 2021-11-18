/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.expression.support;

import java.util.ArrayList;
import java.util.List;

import org.jboss.el.ExpressionFactoryImpl;
import org.springframework.binding.expression.EvaluationException;
import org.springframework.binding.expression.ExpressionParser;
import org.springframework.binding.expression.ParserException;
import org.springframework.binding.expression.el.ELExpressionParser;
import org.springframework.binding.expression.ognl.OgnlExpressionParser;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests simple expressions. Any expression language capable enough for real life usage should be able to pass these
 * tests.
 * 
 * @author Erwin Vervaet
 * @author Jeremy Grelle
 */
public class SimpleExpressionTests extends TestCase {

	private ExpressionParser expressionParser;
	private String expressionPrefix;
	private TestBean bean;

	public static TestSuite suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(new SimpleExpressionTests("testGetValue", new OgnlExpressionParser(), "$"));
		suite.addTest(new SimpleExpressionTests("testSetValue", new OgnlExpressionParser(), "$"));
		suite.addTest(new SimpleExpressionTests("testSyntaxError", new OgnlExpressionParser(), "$"));
		suite.addTest(new SimpleExpressionTests("testGetValue", new BeanWrapperExpressionParser(), "$"));
		suite.addTest(new SimpleExpressionTests("testSetValue", new BeanWrapperExpressionParser(), "$"));
		suite.addTest(new SimpleExpressionTests("testSyntaxError", new BeanWrapperExpressionParser(), "$"));
		suite.addTest(new SimpleExpressionTests("testGetValue", new ELExpressionParser(new ExpressionFactoryImpl()),
				"#"));
		suite.addTest(new SimpleExpressionTests("testSetValue", new ELExpressionParser(new ExpressionFactoryImpl()),
				"#"));
		suite.addTest(new SimpleExpressionTests("testSyntaxError", new ELExpressionParser(new ExpressionFactoryImpl()),
				"#"));
		return suite;
	}

	public SimpleExpressionTests(String name, ExpressionParser expressionParser, String expressionPrefix) {
		super(name);
		this.expressionParser = expressionParser;
		this.expressionPrefix = expressionPrefix;
	}

	protected void setUp() throws Exception {
		bean = new TestBean();
		bean.setFlag(true);
		List list = new ArrayList();
		list.add("foo");
		list.add("bar");
		bean.setList(list);
	}

	public void testGetValue() {
		assertEquals(Boolean.TRUE, expressionParser.parseExpression(expressionPrefix + "{flag}").evaluate(bean, null));
		assertEquals(Boolean.TRUE, expressionParser.parseExpression("flag").evaluate(bean, null));
		assertSame(bean.getList(), expressionParser.parseExpression(expressionPrefix + "{list}").evaluate(bean, null));
		assertEquals("foo", expressionParser.parseExpression(expressionPrefix + "{list[0]}").evaluate(bean, null));
	}

	public void testSetValue() {
		expressionParser.parseSettableExpression(expressionPrefix + "{flag}").evaluateToSet(bean, Boolean.FALSE, null);
		assertFalse(bean.isFlag());
		expressionParser.parseSettableExpression("flag").evaluateToSet(bean, Boolean.TRUE, null);
		assertTrue(bean.isFlag());
		List newList = new ArrayList();
		newList.add("boo");
		expressionParser.parseSettableExpression(expressionPrefix + "{list}").evaluateToSet(bean, newList, null);
		assertSame(newList, bean.getList());
		expressionParser.parseSettableExpression(expressionPrefix + "{list[0]}").evaluateToSet(bean, "baa", null);
		assertEquals("baa", bean.getList().get(0));
	}

	public void testSyntaxError() {
		try {
			expressionParser.parseExpression("foo(").evaluate(bean, null);
			fail("should have failed");
		} catch (ParserException e) {
		} catch (EvaluationException e) {
		}
	}
}
