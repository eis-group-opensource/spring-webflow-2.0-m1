/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.expression.ognl;

import ognl.Ognl;
import ognl.OgnlException;
import ognl.OgnlRuntime;
import ognl.PropertyAccessor;

import org.springframework.binding.expression.Expression;
import org.springframework.binding.expression.ParserException;
import org.springframework.binding.expression.SettableExpression;
import org.springframework.binding.expression.support.AbstractExpressionParser;

/**
 * An expression parser that parses Ognl expressions.
 * 
 * @author Keith Donald
 */
public class OgnlExpressionParser extends AbstractExpressionParser {

	protected Expression doParseExpression(String expressionString) throws ParserException {
		return doParseSettableExpression(expressionString);
	}

	public SettableExpression doParseSettableExpression(String expressionString) throws ParserException {
		try {
			return new OgnlExpression(Ognl.parseExpression(expressionString));
		} catch (OgnlException e) {
			throw new ParserException(expressionString, e);
		}
	}

	/**
	 * Add a property access strategy for the given class.
	 * @param clazz the class that contains properties needing access
	 * @param propertyAccessor the property access strategy
	 */
	public void addPropertyAccessor(Class clazz, PropertyAccessor propertyAccessor) {
		OgnlRuntime.setPropertyAccessor(clazz, propertyAccessor);
	}
}