/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.expression;

import org.springframework.core.style.ToStringCreator;

/**
 * Records an attempt to set an expression value.
 * 
 * @author Keith Donald
 */
public class SetValueAttempt extends EvaluationAttempt {

	/**
	 * The new value.
	 */
	private Object value;

	/**
	 * Creates a new set attempt.
	 * @param expression the settable expression
	 * @param target the target of the expression
	 * @param value the value that was attempted to be set
	 * @param context context attributes that may have influenced the evaluation and set process
	 */
	public SetValueAttempt(SettableExpression expression, Object target, Object value, EvaluationContext context) {
		super(expression, target, context);
		this.value = value;
	}

	/**
	 * Returns the value that was attempted to be set.
	 */
	public Object getValue() {
		return value;
	}

	protected ToStringCreator createToString(ToStringCreator creator) {
		return super.createToString(creator).append("value", value);
	}
}