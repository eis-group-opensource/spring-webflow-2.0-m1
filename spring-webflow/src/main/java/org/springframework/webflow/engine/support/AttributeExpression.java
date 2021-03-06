/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.support;

import org.springframework.binding.expression.EvaluationContext;
import org.springframework.binding.expression.EvaluationException;
import org.springframework.binding.expression.Expression;
import org.springframework.binding.expression.SettableExpression;
import org.springframework.core.style.ToStringCreator;
import org.springframework.util.Assert;
import org.springframework.webflow.core.collection.AttributeMap;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.ScopeType;

/**
 * Expression evaluator that can evaluate attribute maps and supported request context scope types.
 * 
 * @see org.springframework.webflow.execution.RequestContext
 * @see org.springframework.webflow.core.collection.AttributeMap
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public class AttributeExpression implements SettableExpression {

	/**
	 * The expression to evaluate.
	 */
	private Expression expression;

	/**
	 * The scope type.
	 */
	private ScopeType scopeType;

	/**
	 * Create a new expression evaluator that executes given expression in an attribute map. When using this wrapper to
	 * set a property value, make sure the given expression is a {@link SettableExpression}}.
	 * @param expression the nested evaluator to execute
	 */
	public AttributeExpression(Expression expression) {
		this(expression, null);
	}

	/**
	 * Create a new expression evaluator that executes given expression in the specified scope. When using this wrapper
	 * to set a property value, make sure the given expression is a {@link SettableExpression}}.
	 * @param expression the nested evaluator to execute
	 * @param scopeType the scopeType
	 */
	public AttributeExpression(Expression expression, ScopeType scopeType) {
		this.expression = expression;
		this.scopeType = scopeType;
	}

	/**
	 * Returns the expression that will be evaluated.
	 */
	protected Expression getExpression() {
		return expression;
	}

	public Object evaluate(Object target, EvaluationContext context) throws EvaluationException {
		if (target instanceof RequestContext) {
			RequestContext requestContext = (RequestContext) target;
			AttributeMap scope = scopeType.getScope(requestContext);
			return expression.evaluate(scope, context);
		} else if (target instanceof AttributeMap) {
			return expression.evaluate(target, context);
		} else {
			throw new IllegalArgumentException(
					"Only supports evaluation against a [RequestContext] or [AttributeMap] instance, but was a ["
							+ target.getClass() + "]");
		}
	}

	public void evaluateToSet(Object target, Object value, EvaluationContext context) throws EvaluationException {
		Assert.isInstanceOf(SettableExpression.class, expression,
				"When an AttributeExpression is used to set a property value, the nested expression needs "
						+ "to be a SettableExpression");
		if (target instanceof RequestContext) {
			RequestContext requestContext = (RequestContext) target;
			MutableAttributeMap scope = scopeType.getScope(requestContext);
			((SettableExpression) expression).evaluateToSet(scope, value, context);
		} else if (target instanceof AttributeMap) {
			((SettableExpression) expression).evaluateToSet(target, value, context);
		} else {
			throw new IllegalArgumentException(
					"Only supports evaluation against a [RequestContext] or [AttributeMap] instance, but was a ["
							+ target.getClass() + "]");
		}
	}

	public String toString() {
		return new ToStringCreator(this).append("expression", expression).toString();
	}
}