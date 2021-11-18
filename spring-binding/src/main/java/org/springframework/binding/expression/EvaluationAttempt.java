/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.expression;

import org.springframework.core.style.ToStringCreator;

/**
 * A simple holder for information about an evaluation attempt.
 * 
 * @author Keith Donald
 */
public class EvaluationAttempt {

	/**
	 * The expression that attempted to evaluate.
	 */
	private Expression expression;

	/**
	 * The target object being evaluated.
	 */
	private Object target;

	/**
	 * The evaluation context.
	 */
	private EvaluationContext context;

	/**
	 * Create an evaluation attempt.
	 * @param expression the expression that failed to evaluate
	 * @param target the target of the expression
	 * @param context the context attributes that might have affected evaluation behavior
	 */
	public EvaluationAttempt(Expression expression, Object target, EvaluationContext context) {
		this.expression = expression;
		this.target = target;
		this.context = context;
	}

	/**
	 * Returns the expression that attempted to evaluate.
	 */
	public Expression getExpression() {
		return expression;
	}

	/**
	 * Returns the target object upon which evaluation was attempted.
	 */
	public Object getTarget() {
		return target;
	}

	/**
	 * Returns context attributes that may have influenced the evaluation process.
	 */
	public EvaluationContext getContext() {
		return context;
	}

	public String toString() {
		return createToString(new ToStringCreator(this)).toString();
	}

	protected ToStringCreator createToString(ToStringCreator creator) {
		return creator.append("expression", expression).append("target", target).append("context", context);
	}
}