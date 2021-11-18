/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.expression.support;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.binding.expression.EvaluationAttempt;
import org.springframework.binding.expression.EvaluationContext;
import org.springframework.binding.expression.EvaluationException;
import org.springframework.binding.expression.SetValueAttempt;
import org.springframework.binding.expression.SettableExpression;
import org.springframework.util.Assert;

/**
 * An expression evaluator that uses the Spring bean wrapper.
 * 
 * @author Keith Donald
 */
class BeanWrapperExpression implements SettableExpression {

	/**
	 * The expression.
	 */
	private String expression;

	public BeanWrapperExpression(String expression) {
		this.expression = expression;
	}

	public int hashCode() {
		return expression.hashCode();
	}

	public boolean equals(Object o) {
		if (!(o instanceof BeanWrapperExpression)) {
			return false;
		}
		BeanWrapperExpression other = (BeanWrapperExpression) o;
		return expression.equals(other.expression);
	}

	public Object evaluate(Object target, EvaluationContext context) throws EvaluationException {
		try {
			return new BeanWrapperImpl(target).getPropertyValue(expression);
		} catch (BeansException e) {
			throw new EvaluationException(new EvaluationAttempt(this, target, context), e);
		}
	}

	public void evaluateToSet(Object target, Object value, EvaluationContext context) throws EvaluationException {
		try {
			Assert.notNull(target, "The target object to evaluate is required");
			new BeanWrapperImpl(target).setPropertyValue(expression, value);
		} catch (BeansException e) {
			throw new EvaluationException(new SetValueAttempt(this, target, value, context), e);
		}
	}

	public String toString() {
		return expression;
	}
}