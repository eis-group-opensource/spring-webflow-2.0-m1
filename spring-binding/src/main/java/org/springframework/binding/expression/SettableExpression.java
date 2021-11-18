/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.expression;

/**
 * An evaluator that is capable of setting a value on a target object at the path defined by this expression.
 * 
 * @author Keith Donald
 */
public interface SettableExpression extends Expression {

	/**
	 * Evaluate this expression against the target object to set its value to the value provided.
	 * @param target the target object
	 * @param value the new value to be set
	 * @param context the evaluation context
	 * @throws EvaluationException an exception occured during evaluation
	 */
	public void evaluateToSet(Object target, Object value, EvaluationContext context) throws EvaluationException;
}