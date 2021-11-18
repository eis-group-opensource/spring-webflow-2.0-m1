/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.expression.support;

import java.util.Collection;

import org.springframework.binding.expression.EvaluationContext;
import org.springframework.binding.expression.EvaluationException;
import org.springframework.binding.expression.Expression;
import org.springframework.binding.expression.SetValueAttempt;
import org.springframework.binding.expression.SettableExpression;
import org.springframework.core.style.ToStringCreator;
import org.springframework.util.Assert;

/**
 * A settable expression that adds non-null values to a collection.
 * 
 * @author Keith Donald
 */
public class CollectionAddingExpression implements SettableExpression {

	/**
	 * The expression that resolves a mutable collection reference.
	 */
	private Expression collectionExpression;

	/**
	 * Creates a collection adding property expression.
	 * @param collectionExpression the collection expression
	 */
	public CollectionAddingExpression(Expression collectionExpression) {
		this.collectionExpression = collectionExpression;
	}

	public Object evaluate(Object target, EvaluationContext context) throws EvaluationException {
		return collectionExpression.evaluate(target, context);
	}

	public void evaluateToSet(Object target, Object value, EvaluationContext context) throws EvaluationException {
		Object result = evaluate(target, context);
		if (result == null) {
			throw new EvaluationException(new SetValueAttempt(this, target, value, null), new IllegalArgumentException(
					"The collection expression evaluated to a [null] reference"));
		}
		Assert.isInstanceOf(Collection.class, result, "Not a collection: ");
		if (value != null) {
			// add the value to the collection
			((Collection) result).add(value);
		}
	}

	public String toString() {
		return new ToStringCreator(this).append("collectionExpression", collectionExpression).toString();
	}
}