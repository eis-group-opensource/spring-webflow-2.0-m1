/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.faces.webflow.el;

import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.VariableResolver;

import org.springframework.faces.webflow.FlowExecutionHolderUtils;
import org.springframework.webflow.execution.FlowExecution;

/**
 * Custom variable resolver that resolves to a thread-bound FlowExecution object for binding expressions prefixed with a
 * {@link #FLOW_EXECUTION_VARIABLE_NAME}. For instance "flowExecution.conversationScope.myProperty".
 * 
 * This class is designed to be used with a {@link FlowExecutionPropertyResolver}.
 * 
 * This class is a more flexible alternative to the {@link FlowVariableResolver} which is expected to be used ONLY with
 * a {@link FlowPropertyResolver} to resolve flow scope variables ONLY. It is more flexible because it provides access
 * to any scope structure of a {@link FlowExecution} object.
 * 
 * @author Keith Donald
 */
public class FlowExecutionVariableResolver extends VariableResolver {

	/**
	 * Name of the flow execution variable.
	 */
	public static final String FLOW_EXECUTION_VARIABLE_NAME = "flowExecution";

	/**
	 * The standard variable resolver to delegate to if this one doesn't apply.
	 */
	private VariableResolver resolverDelegate;

	/**
	 * Creates a new flow executon variable resolver that resolves the current FlowExecution object.
	 * @param resolverDelegate the resolver to delegate to when the variable is not named "flowExecution".
	 */
	public FlowExecutionVariableResolver(VariableResolver resolverDelegate) {
		this.resolverDelegate = resolverDelegate;
	}

	/**
	 * Returns the variable resolver this resolver delegates to if necessary.
	 */
	protected final VariableResolver getResolverDelegate() {
		return resolverDelegate;
	}

	public Object resolveVariable(FacesContext context, String name) throws EvaluationException {
		if (FLOW_EXECUTION_VARIABLE_NAME.equals(name)) {
			return FlowExecutionHolderUtils.getRequiredCurrentFlowExecution(context);
		} else {
			return resolverDelegate.resolveVariable(context, name);
		}
	}
}