/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.core;

import org.springframework.binding.expression.Expression;
import org.springframework.binding.expression.ExpressionParser;
import org.springframework.binding.expression.ParserException;
import org.springframework.binding.expression.SettableExpression;

/**
 * Static helper factory that creates instances of the default expression parser used by Spring Web Flow when requested.
 * Marked final with a private constructor to prevent subclassing.
 * <p>
 * The default is an OGNL based expression parser. Also asserts that OGNL is in the classpath the first time the parser
 * is used.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public final class DefaultExpressionParserFactory {

	/**
	 * The singleton instance of the default expression parser.
	 */
	private static ExpressionParser INSTANCE;

	// static factory - not instantiable
	private DefaultExpressionParserFactory() {
	}

	/**
	 * Returns the default expression parser. The returned expression parser is a thread-safe object.
	 * @return the expression parser
	 */
	public static synchronized ExpressionParser getExpressionParser() {
		// return a wrapper that will lazily load the default expression parser
		// this prevents the default OGNL-based parser from being intialized until it is actually used
		// which allows OGNL to be an optional dependency if the expression parser wrapper is replaced and never used
		return new ExpressionParser() {
			public boolean isDelimitedExpression(String expressionString) {
				return getDefaultExpressionParser().isDelimitedExpression(expressionString);
			}

			public Expression parseExpression(String expressionString) throws ParserException {
				return getDefaultExpressionParser().parseExpression(expressionString);
			}

			public SettableExpression parseSettableExpression(String expressionString) throws ParserException,
					UnsupportedOperationException {
				return getDefaultExpressionParser().parseSettableExpression(expressionString);
			}
		};
	}

	/**
	 * Returns the default expression parser, creating it if necessary.
	 * @return the default expression parser
	 */
	private static synchronized ExpressionParser getDefaultExpressionParser() {
		if (INSTANCE == null) {
			INSTANCE = createDefaultExpressionParser();
		}
		return INSTANCE;
	}

	/**
	 * Create the default expression parser.
	 * @return the default expression parser
	 */
	private static ExpressionParser createDefaultExpressionParser() {
		try {
			Class.forName("ognl.Ognl");
			return new WebFlowOgnlExpressionParser();
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(
					"Unable to load the default expression parser: OGNL could not be found in the classpath.  "
							+ "Please add OGNL 2.x to your classpath or set the default ExpressionParser instance to something that is in the classpath.  "
							+ "Details: " + e.getMessage());
		} catch (NoClassDefFoundError e) {
			throw new IllegalStateException(
					"Unable to construct the default expression parser: ognl.Ognl could not be instantiated.  "
							+ "Please add OGNL 2.x to your classpath or set the default ExpressionParser instance to something that is in the classpath.  "
							+ "Details: " + e);
		}
	}
}