/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.expression.ognl;

import java.util.Collections;
import java.util.Map;

import ognl.Ognl;
import ognl.OgnlException;

import org.springframework.binding.expression.EvaluationAttempt;
import org.springframework.binding.expression.EvaluationContext;
import org.springframework.binding.expression.EvaluationException;
import org.springframework.binding.expression.SetValueAttempt;
import org.springframework.binding.expression.SettableExpression;
import org.springframework.util.Assert;

/**
 * Evaluates a parsed Ognl expression.
 * <p>
 * IMPLEMENTATION NOTE: Ognl 2.6.7 expression objects do not respect equality properly, so the equality operations
 * defined within this class do not function properly.
 * 
 * @author Keith Donald
 */
class OgnlExpression implements SettableExpression {

	/**
	 * The expression.
	 */
	private Object expression;

	/**
	 * Creates a new OGNL expression.
	 * @param expression the parsed expression
	 */
	public OgnlExpression(Object expression) {
		this.expression = expression;
	}

	public int hashCode() {
		return expression.hashCode();
	}

	public boolean equals(Object o) {
		if (!(o instanceof OgnlExpression)) {
			return false;
		}
		// as late as Ognl 2.6.7, their expression objects don't implement equals
		// so this always returns false
		OgnlExpression other = (OgnlExpression) o;
		return expression.equals(other.expression);
	}

	public Object evaluate(Object target, EvaluationContext context) throws EvaluationException {
		Assert.notNull(target, "The target object to evaluate is required");
		Map contextAttributes = (context != null ? context.getAttributes() : Collections.EMPTY_MAP);
		try {
			return Ognl.getValue(expression, contextAttributes, target);
		} catch (OgnlException e) {
			if (e.getReason() != null && e.getReason() != e) {
				// unwrap the OgnlException since the actual exception is wrapped inside it
				// and there is not generic (getCause) way to get to it later on
				throw new EvaluationException(new EvaluationAttempt(this, target, context), e.getReason());
			} else {
				throw new EvaluationException(new EvaluationAttempt(this, target, context), e);
			}
		}
	}

	public void evaluateToSet(Object target, Object value, EvaluationContext context) {
		Assert.notNull(target, "The target object to evaluate is required");
		Map contextAttributes = (context != null ? context.getAttributes() : Collections.EMPTY_MAP);
		try {
			Ognl.setValue(expression, contextAttributes, target, value);
		} catch (OgnlException e) {
			throw new EvaluationException(new SetValueAttempt(this, target, value, context), e);
		}
	}

	public String toString() {
		return expression.toString();
	}
}