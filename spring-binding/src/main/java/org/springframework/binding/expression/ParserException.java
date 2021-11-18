/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.expression;

import org.springframework.core.NestedRuntimeException;

/**
 * Base class for exceptions thrown during expression parsing.
 * 
 * @author Keith Donald
 */
public class ParserException extends NestedRuntimeException {

	/**
	 * The expression string that could not be parsed.
	 */
	private String expressionString;

	/**
	 * Creates a new expression parsing exception.
	 * @param expressionString the expression string that could not be parsed
	 * @param cause the underlying cause of this exception
	 */
	public ParserException(String expressionString, Throwable cause) {
		this(expressionString, "Unable to parse expression string '" + expressionString + "'", cause);
	}

	/**
	 * Creates a new expression parsing exception.
	 * @param expressionString the expression string that could not be parsed
	 * @param message a descriptive message
	 * @param cause the underlying cause of this exception
	 */
	public ParserException(String expressionString, String message, Throwable cause) {
		super(message, cause);
		this.expressionString = expressionString;
	}

	/**
	 * Returns the expression string that could not be parsed.
	 */
	public Object getExpressionString() {
		return expressionString;
	}
}