/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.expression.ognl;

import junit.framework.TestCase;

import org.springframework.binding.expression.Expression;
import org.springframework.binding.expression.ParserException;
import org.springframework.binding.expression.ognl.OgnlExpressionParser;
import org.springframework.binding.expression.support.TestBean;

/**
 * Unit tests for {@link org.springframework.binding.expression.ognl.OgnlExpressionParser}.
 */
public class OgnlExpressionParserTests extends TestCase {

	private OgnlExpressionParser parser = new OgnlExpressionParser();

	private TestBean bean = new TestBean();

	public void testParseSimpleDelimited() {
		String exp = "${flag}";
		Expression e = parser.parseExpression(exp);
		assertNotNull(e);
		Boolean b = (Boolean) e.evaluate(bean, null);
		assertFalse(b.booleanValue());
	}

	public void testParseSimple() {
		String exp = "flag";
		Expression e = parser.parseExpression(exp);
		assertNotNull(e);
		Boolean b = (Boolean) e.evaluate(bean, null);
		assertFalse(b.booleanValue());
	}

	public void testParseNull() {
		Expression e = parser.parseExpression(null);
		assertNotNull(e);
		assertNull(e.evaluate(bean, null));
	}

	public void testParseEmpty() {
		Expression e = parser.parseExpression("");
		assertNotNull(e);
		assertEquals("", e.evaluate(bean, null));
	}

	public void testParseComposite() {
		String exp = "hello ${flag} ${flag} ${flag}";
		Expression e = parser.parseExpression(exp);
		assertNotNull(e);
		String str = (String) e.evaluate(bean, null);
		assertEquals("hello false false false", str);
	}

	public void testEnclosedCompositeNotSupported() {
		String exp = "${hello ${flag} ${flag} ${flag}}";
		try {
			parser.parseExpression(exp);
			fail("Should've failed - not intended use");
		} catch (ParserException e) {
		}
	}

	public void testSyntaxError1() {
		try {
			parser.parseExpression("${");
			fail();
		} catch (ParserException e) {
		}

		try {
			String exp = "hello ${flag} ${abcd defg";
			parser.parseExpression(exp);
			fail("Should've failed - not intended use");
		} catch (ParserException e) {
		}
	}

	public void testSyntaxError2() {
		try {
			parser.parseExpression("${}");
			fail("Should've failed - not intended use");
		} catch (ParserException e) {
		}

		try {
			String exp = "hello ${flag} ${}";
			parser.parseExpression(exp);
			fail("Should've failed - not intended use");
		} catch (ParserException e) {
		}
	}

	public void testIsDelimitedExpression() {
		assertTrue(parser.isDelimitedExpression("${foo}"));
		assertTrue(parser.isDelimitedExpression("${foo ${foo}}"));
		assertTrue(parser.isDelimitedExpression("foo ${bar}"));
	}

	public void testIsNotDelimitedExpression() {
		assertFalse(parser.isDelimitedExpression("foo"));
		assertFalse(parser.isDelimitedExpression("foo ${"));
		assertFalse(parser.isDelimitedExpression("$foo}"));
		assertFalse(parser.isDelimitedExpression("foo ${}"));
	}

	public void testCollectionContructionSyntax() {
		// lists
		parser.parseExpression("name in {null, \"Untitled\"}");
		parser.parseExpression("${name in {null, \"Untitled\"}}");

		// native arrays
		parser.parseExpression("new int[] {1, 2, 3}");
		parser.parseExpression("${new int[] {1, 2, 3}}");

		// maps
		parser.parseExpression("#{ 'foo' : 'foo value', 'bar' : 'bar value' }");
		parser.parseExpression("${#{ 'foo' : 'foo value', 'bar' : 'bar value' }}");
		parser.parseExpression("#@java.util.LinkedHashMap@{ 'foo' : 'foo value', 'bar' : 'bar value' }");
		parser.parseExpression("${#@java.util.LinkedHashMap@{ 'foo' : 'foo value', 'bar' : 'bar value' }}");

		// complex examples
		parser.parseExpression("b,#{1:2}");
		parser.parseExpression("${b,#{1:2}}");
		parser.parseExpression("a${b,#{1:2},e}f${g,#{3:4},j}k");
	}
}