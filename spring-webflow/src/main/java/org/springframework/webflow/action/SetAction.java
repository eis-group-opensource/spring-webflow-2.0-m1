/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.action;

import org.springframework.binding.expression.EvaluationContext;
import org.springframework.binding.expression.Expression;
import org.springframework.binding.expression.SettableExpression;
import org.springframework.util.Assert;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.ScopeType;

/**
 * An action that sets an attribute in a {@link ScopeType scope} when executed. Always returns the "success" event.
 * 
 * @author Keith Donald
 */
public class SetAction extends AbstractAction {

	/**
	 * The expression for setting the scoped attribute value.
	 */
	private SettableExpression attributeExpression;

	/**
	 * The target scope.
	 */
	private ScopeType scope;

	/**
	 * The expression for resolving the scoped attribute value.
	 */
	private Expression valueExpression;

	/**
	 * Creates a new set attribute action.
	 * @param attributeExpression the writeable attribute expression
	 * @param scope the target scope of the attribute
	 * @param valueExpression the evaluatable attribute value expression
	 */
	public SetAction(SettableExpression attributeExpression, ScopeType scope, Expression valueExpression) {
		Assert.notNull(attributeExpression, "The attribute expression is required");
		Assert.notNull(scope, "The scope type is required");
		Assert.notNull(valueExpression, "The value expression is required");
		this.attributeExpression = attributeExpression;
		this.scope = scope;
		this.valueExpression = valueExpression;
	}

	protected Event doExecute(RequestContext context) throws Exception {
		EvaluationContext evaluationContext = getEvaluationContext(context);
		Object value = valueExpression.evaluate(context, evaluationContext);
		MutableAttributeMap scopeMap = scope.getScope(context);
		attributeExpression.evaluateToSet(scopeMap, value, evaluationContext);
		return success();
	}

	/**
	 * Template method subclasses may override to customize the expression evaluation context. This implementation
	 * returns null.
	 * @param context the request context
	 * @return the evaluation context
	 */
	protected EvaluationContext getEvaluationContext(RequestContext context) {
		return null;
	}
}