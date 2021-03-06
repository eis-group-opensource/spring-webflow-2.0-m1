/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.faces.webflow.el;

import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.VariableResolver;

import org.springframework.faces.webflow.FlowExecutionHolderUtils;
import org.springframework.web.jsf.DelegatingVariableResolver;
import org.springframework.webflow.execution.FlowExecution;

/**
 * Custom variable resolver that searches the current flow execution for variables to resolve. The search algorithm
 * looks in flash scope first, then flow scope, then conversation scope. If no variable is found this resolver delegates
 * to the next resolver in the chain.
 * 
 * Suitable for use along side other variable resolvers to support EL binding expressions like {#bean.property} where
 * "bean" could be a property in any supported scope.
 * 
 * Consider combining use of this class with a Spring {@link DelegatingVariableResolver} to also support
 * lazy-initialized binding variables managed by a Spring application context using custom bean scopes. Also consider
 * such a Spring-backed managed bean facility as the sole-provider for centralized JSF managed bean references.
 * 
 * @author Keith Donald
 */
public class DelegatingFlowVariableResolver extends VariableResolver {

	/**
	 * The standard variable resolver to delegate to if this one doesn't apply.
	 */
	private VariableResolver resolverDelegate;

	/**
	 * Create a new FlowExecutionVariableResolver, using the given original VariableResolver.
	 * <p>
	 * A JSF implementation will automatically pass its original resolver into the constructor of a configured resolver,
	 * provided that there is a corresponding constructor argument.
	 * 
	 * @param resolverDelegate the original VariableResolver
	 */
	public DelegatingFlowVariableResolver(VariableResolver resolverDelegate) {
		this.resolverDelegate = resolverDelegate;
	}

	/**
	 * Return the original VariableResolver that this resolver delegates to.
	 */
	protected final VariableResolver getResolverDelegate() {
		return resolverDelegate;
	}

	public Object resolveVariable(FacesContext context, String name) throws EvaluationException {
		FlowExecution execution = FlowExecutionHolderUtils.getCurrentFlowExecution(context);
		if (execution != null) {
			if (execution.isActive()) {
				// flow execution is active: try flash/flow/conversation scope
				if (execution.getFlashScope().contains(name)) {
					return execution.getFlashScope().get(name);
				} else if (execution.getActiveSession().getScope().contains(name)) {
					return execution.getActiveSession().getScope().get(name);
				} else if (execution.getConversationScope().contains(name)) {
					return execution.getConversationScope().get(name);
				}
			} else {
				// flow execution has ended: check for end-state attributes exposed in the request map
				if (context.getExternalContext().getRequestMap().containsKey(name)) {
					return context.getExternalContext().getRequestMap().get(name);
				}
			}
		}
		// no flow execution bound or flow execution attribute found with that name - delegate
		return resolverDelegate.resolveVariable(context, name);
	}
}