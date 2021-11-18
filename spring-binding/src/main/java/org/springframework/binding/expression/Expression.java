/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.expression;

/**
 * Evaluates a single parsed expression on the provided input object in the specified context. This provides a common
 * abstraction for expression evaluation independent of any language like OGNL or Spring's BeanWrapper.
 * 
 * @author Keith Donald
 */
public interface Expression {

	/**
	 * Evaluate the expression encapsulated by this evaluator against the provided target object and return the result
	 * of the evaluation.
	 * @param target the target of the expression
	 * @param context the expression evaluation context
	 * @return the evaluation result
	 * @throws EvaluationException an exception occured during evaluation
	 */
	public Object evaluate(Object target, EvaluationContext context) throws EvaluationException;
}