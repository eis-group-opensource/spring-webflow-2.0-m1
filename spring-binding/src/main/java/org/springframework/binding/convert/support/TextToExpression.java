/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.convert.support;

import org.springframework.binding.convert.ConversionContext;
import org.springframework.binding.expression.Expression;
import org.springframework.binding.expression.ExpressionParser;
import org.springframework.binding.expression.SettableExpression;
import org.springframework.binding.expression.support.StaticExpression;
import org.springframework.util.Assert;

/**
 * Converter that converts a String into an Expression object.
 * 
 * @see org.springframework.binding.expression.Expression
 * @see org.springframework.binding.expression.SettableExpression
 * 
 * @author Erwin Vervaet
 */
public class TextToExpression extends AbstractConverter {

	/**
	 * The expression string parser.
	 */
	private ExpressionParser expressionParser;

	/**
	 * Creates a new string-to-expression converter.
	 * @param expressionParser the expression string parser
	 */
	public TextToExpression(ExpressionParser expressionParser) {
		Assert.notNull(expressionParser, "The expression parser is required");
		this.expressionParser = expressionParser;
	}

	/**
	 * Returns the expression parser used by this converter.
	 */
	public ExpressionParser getExpressionParser() {
		return expressionParser;
	}

	public Class[] getSourceClasses() {
		return new Class[] { String.class };
	}

	public Class[] getTargetClasses() {
		return new Class[] { Expression.class, SettableExpression.class };
	}

	protected Object doConvert(Object source, Class targetClass, ConversionContext context) throws Exception {
		String expressionString = (String) source;
		if (getExpressionParser().isDelimitedExpression(expressionString)) {
			return getExpressionParser().parseExpression((String) source);
		} else {
			return new StaticExpression(expressionString);
		}
	}
}