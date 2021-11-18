/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.faces.webflow.el;

import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.VariableResolver;

import org.springframework.faces.webflow.FlowExecutionHolderUtils;

/**
 * Custom variable resolver that resolves the current FlowExecution object for binding expressions prefixed with
 * {@link #FLOW_SCOPE_VARIABLE}. For instance "flowScope.myBean.myProperty". Designed to be used in conjunction with
 * {@link FlowPropertyResolver} only.
 * 
 * This class is the original flow execution variable resolver implementation introduced in Spring Web Flow's JSF
 * support available since 1.0. In general, prefer use of {@link DelegatingFlowVariableResolver} or
 * {@link FlowExecutionVariableResolver} to this implementation as they are both considerably more flexible.
 * 
 * This resolver should only be used with the {@link FlowPropertyResolver} which can only resolve flow-scoped variables.
 * May be deprecated in a future release of Spring Web Flow.
 * 
 * @author Colin Sampaleanu
 */
public class FlowVariableResolver extends VariableResolver {

	/**
	 * Name of the exposed flow scope variable ("flowScope").
	 */
	public static final String FLOW_SCOPE_VARIABLE = "flowScope";

	/**
	 * The standard variable resolver to delegate to if this one doesn't apply.
	 */
	private VariableResolver resolverDelegate;

	/**
	 * Create a new FlowVariableResolver, using the given original VariableResolver.
	 * <p>
	 * A JSF implementation will automatically pass its original resolver into the constructor of a configured resolver,
	 * provided that there is a corresponding constructor argument.
	 * 
	 * @param resolverDelegate the original VariableResolver
	 */
	public FlowVariableResolver(VariableResolver resolverDelegate) {
		this.resolverDelegate = resolverDelegate;
	}

	/**
	 * Return the original VariableResolver that this resolver delegates to.
	 */
	protected final VariableResolver getResolverDelegate() {
		return resolverDelegate;
	}

	public Object resolveVariable(FacesContext context, String name) throws EvaluationException {
		if (FLOW_SCOPE_VARIABLE.equals(name)) {
			return FlowExecutionHolderUtils.getRequiredCurrentFlowExecution(context);
		} else {
			return resolverDelegate.resolveVariable(context, name);
		}
	}
}