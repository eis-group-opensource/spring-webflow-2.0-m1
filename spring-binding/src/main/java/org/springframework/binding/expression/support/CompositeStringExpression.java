/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.expression.support;

import org.springframework.binding.expression.EvaluationContext;
import org.springframework.binding.expression.EvaluationException;
import org.springframework.binding.expression.Expression;
import org.springframework.core.style.ToStringCreator;

/**
 * Evaluates an array of expressions to build a concatenated string.
 * 
 * @author Keith Donald
 */
public class CompositeStringExpression implements Expression {

	/**
	 * The expression array.
	 */
	private Expression[] expressions;

	/**
	 * Creates a new composite string expression.
	 * @param expressions the ordered set of expressions that when evaluated will have their results stringed together
	 * to build the composite string
	 */
	public CompositeStringExpression(Expression[] expressions) {
		this.expressions = expressions;
	}

	public Object evaluate(Object target, EvaluationContext evaluationContext) throws EvaluationException {
		StringBuffer buffer = new StringBuffer(128);
		for (int i = 0; i < expressions.length; i++) {
			buffer.append(expressions[i].evaluate(target, evaluationContext));
		}
		return buffer.toString();
	}

	public String toString() {
		return new ToStringCreator(this).append("expressions", expressions).toString();
	}
}