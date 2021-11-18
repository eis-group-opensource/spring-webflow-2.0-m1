/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.method;

import junit.framework.TestCase;

import org.springframework.binding.convert.ConversionException;
import org.springframework.binding.convert.support.DefaultConversionService;
import org.springframework.binding.convert.support.TextToExpression;
import org.springframework.binding.expression.ognl.OgnlExpressionParser;

/**
 * Test case for {@link TextToMethodSignature}.
 * 
 * @author Erwin Vervaet
 */
public class TextToMethodSignatureTests extends TestCase {

	private TextToMethodSignature converter;

	protected void setUp() throws Exception {
		DefaultConversionService conversionService = new DefaultConversionService();
		conversionService.addConverter(new TextToExpression(new OgnlExpressionParser()));
		converter = new TextToMethodSignature(conversionService);
	}

	public void testParseNoArguments() {
		MethodSignature signature = (MethodSignature) converter.convert("foo");
		assertEquals("foo", signature.getMethodName());
		assertEquals(0, signature.getParameters().size());

		signature = (MethodSignature) converter.convert("foo()");
		assertEquals("foo", signature.getMethodName());
		assertEquals(0, signature.getParameters().size());
	}

	public void testSingleArgument() {
		MethodSignature signature = (MethodSignature) converter.convert("foo(${flowScope.bar})");
		assertEquals("foo", signature.getMethodName());
		assertEquals(1, signature.getParameters().size());
		assertNull(signature.getParameters().getParameter(0).getType());
		assertEquals("flowScope.bar", signature.getParameters().getParameter(0).getName().toString());

		signature = (MethodSignature) converter.convert("foo(${'Foo' + flowScope.bar})");
		assertEquals("foo", signature.getMethodName());
		assertEquals(1, signature.getParameters().size());
		assertEquals("\"Foo\" + flowScope.bar", signature.getParameters().getParameter(0).getName().toString());
	}

	public void testSingleArgumentWithType() {
		MethodSignature signature = (MethodSignature) converter.convert("foo(java.lang.String ${flowScope.bar})");
		assertEquals("foo", signature.getMethodName());
		assertEquals(1, signature.getParameters().size());
		assertEquals(String.class, signature.getParameters().getParameter(0).getType());
		assertEquals("flowScope.bar", signature.getParameters().getParameter(0).getName().toString());

		signature = (MethodSignature) converter.convert("foo(long ${flowScope.bar})");
		assertEquals("foo", signature.getMethodName());
		assertEquals(1, signature.getParameters().size());
		assertEquals(Long.class, signature.getParameters().getParameter(0).getType());
		assertEquals("flowScope.bar", signature.getParameters().getParameter(0).getName().toString());
	}

	public void testMultipleArguments() {
		MethodSignature signature = (MethodSignature) converter
				.convert("foo(${flowScope.bar}, ${externalContext.requestParameterMap.test})");
		assertEquals("foo", signature.getMethodName());
		assertEquals(2, signature.getParameters().size());
		assertNull(signature.getParameters().getParameter(0).getType());
		assertEquals("flowScope.bar", signature.getParameters().getParameter(0).getName().toString());
		assertNull(signature.getParameters().getParameter(1).getType());
		assertEquals("externalContext.requestParameterMap.test", signature.getParameters().getParameter(1).getName()
				.toString());
	}

	public void testMultipleArgumentsWithType() {
		MethodSignature signature = (MethodSignature) converter
				.convert("foo(long ${flowScope.bar}, java.lang.String ${externalContext.requestParameterMap.test})");
		assertEquals("foo", signature.getMethodName());
		assertEquals(2, signature.getParameters().size());
		assertEquals(Long.class, signature.getParameters().getParameter(0).getType());
		assertEquals("flowScope.bar", signature.getParameters().getParameter(0).getName().toString());
		assertEquals(String.class, signature.getParameters().getParameter(1).getType());
		assertEquals("externalContext.requestParameterMap.test", signature.getParameters().getParameter(1).getName()
				.toString());
	}

	public void testCollectionConstructionSyntax() {
		MethodSignature signature = (MethodSignature) converter.convert("foo({1, 2, 3})");
		assertEquals("foo", signature.getMethodName());
		assertEquals(1, signature.getParameters().size());
		assertNull(signature.getParameters().getParameter(0).getType());
		assertEquals("{1, 2, 3}", signature.getParameters().getParameter(0).getName().toString());
	}

	public void testCollectionConstructionSyntaxWithType() {
		MethodSignature signature = (MethodSignature) converter.convert("foo(java.util.List {1, 2, 3})");
		assertEquals("foo", signature.getMethodName());
		assertEquals(1, signature.getParameters().size());
		assertEquals(java.util.List.class, signature.getParameters().getParameter(0).getType());
		assertEquals("{1, 2, 3}", signature.getParameters().getParameter(0).getName().toString());

		signature = (MethodSignature) converter.convert("foo(a${b,#{1:2},e}f${g,#{3:4},j}k)");
	}

	public void testSyntaxErrors() {
		try {
			converter.convert("foo(");
			fail();
		} catch (ConversionException e) {
		}

		try {
			converter.convert("foo(long 1, ${bar()}");
			fail();
		} catch (ConversionException e) {
		}

		try {
			converter.convert("foo(long 1, {1, 2)");
			fail();
		} catch (ConversionException e) {
		}
	}
}
