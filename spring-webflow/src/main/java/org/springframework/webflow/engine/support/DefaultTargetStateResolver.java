/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.support;

import org.springframework.binding.expression.Expression;
import org.springframework.binding.expression.support.StaticExpression;
import org.springframework.util.Assert;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.engine.State;
import org.springframework.webflow.engine.TargetStateResolver;
import org.springframework.webflow.engine.Transition;
import org.springframework.webflow.execution.RequestContext;

/**
 * A transition target state resolver that evaluates an expression to resolve the target state. The default
 * implementation.
 * 
 * @author Keith Donald
 */
public class DefaultTargetStateResolver implements TargetStateResolver {

	/**
	 * The expression for the target state identifier.
	 */
	private Expression targetStateIdExpression;

	/**
	 * Creates a new target state resolver that always returns the same target state id.
	 * @param targetStateId the id of the target state
	 */
	public DefaultTargetStateResolver(String targetStateId) {
		this(new StaticExpression(targetStateId));
	}

	/**
	 * Creates a new target state resolver.
	 * @param targetStateIdExpression the target state id expression
	 */
	public DefaultTargetStateResolver(Expression targetStateIdExpression) {
		Assert.notNull(targetStateIdExpression, "The target state id expression is required");
		this.targetStateIdExpression = targetStateIdExpression;
	}

	public State resolveTargetState(Transition transition, State sourceState, RequestContext context) {
		String stateId = String.valueOf(targetStateIdExpression.evaluate(context, null));
		return ((Flow) context.getActiveFlow()).getStateInstance(stateId);
	}

	public String toString() {
		return targetStateIdExpression.toString();
	}
}