/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.expression;

/**
 * Parses expression strings, returing a configured evaluator instance capable of performing parsed expression
 * evaluation in a thread safe way.
 * 
 * @author Keith Donald
 */
public interface ExpressionParser {

	/**
	 * Is this expression string delimited in a manner that indicates it is a parseable expression? For example
	 * "${expression}".
	 * @param expressionString the proposed expression string
	 * @return true if yes, false if not
	 */
	public boolean isDelimitedExpression(String expressionString);

	/**
	 * Parse the provided expression string, returning an evaluator capable of evaluating it against input.
	 * @param expressionString the parseable expression string
	 * @return the evaluator for the parsed expression
	 * @throws ParserException an exception occured during parsing
	 */
	public Expression parseExpression(String expressionString) throws ParserException;

	/**
	 * Parse the provided settable expression string, returning an evaluator capable of evaluating its value as well as
	 * setting its value.
	 * @param expressionString the parseable expression string
	 * @return the evaluator for the parsed expression
	 * @throws ParserException an exception occured during parsing
	 * @throws UnsupportedOperationException this parser does not support settable expressions
	 */
	public SettableExpression parseSettableExpression(String expressionString) throws ParserException,
			UnsupportedOperationException;

}