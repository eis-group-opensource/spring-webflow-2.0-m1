/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.expression.support;

import org.springframework.binding.expression.Expression;
import org.springframework.binding.expression.ParserException;
import org.springframework.binding.expression.SettableExpression;

/**
 * An expression parser that parses bean wrapper expressions.
 * 
 * @author Keith Donald
 */
public class BeanWrapperExpressionParser extends AbstractExpressionParser {

	protected Expression doParseExpression(String expressionString) throws ParserException {
		return doParseSettableExpression(expressionString);
	}

	public SettableExpression doParseSettableExpression(String expressionString) throws ParserException {
		return new BeanWrapperExpression(expressionString);
	}
}