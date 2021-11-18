/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.expression.support;

import org.springframework.binding.expression.EvaluationContext;
import org.springframework.binding.expression.EvaluationException;
import org.springframework.binding.expression.Expression;
import org.springframework.util.ObjectUtils;

/**
 * A simple expression evaluator that just returns a fixed result on each evaluation.
 * 
 * @author Keith Donald
 */
public class StaticExpression implements Expression {

	/**
	 * The value expression.
	 */
	private Object value;

	/**
	 * Create a static evaluator for the given value.
	 * @param value the value
	 */
	public StaticExpression(Object value) {
		this.value = value;
	}

	public int hashCode() {
		if (value == null) {
			return 0;
		} else {
			return value.hashCode();
		}
	}

	public boolean equals(Object o) {
		if (!(o instanceof StaticExpression)) {
			return false;
		}
		StaticExpression other = (StaticExpression) o;
		return ObjectUtils.nullSafeEquals(value, other.value);
	}

	public Object evaluate(Object target, EvaluationContext context) throws EvaluationException {
		return value;
	}

	public String toString() {
		return String.valueOf(value);
	}
}