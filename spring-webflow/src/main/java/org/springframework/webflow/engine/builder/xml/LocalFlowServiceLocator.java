/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.builder.xml;

import java.util.Stack;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.binding.convert.ConversionService;
import org.springframework.binding.expression.ExpressionParser;
import org.springframework.core.io.ResourceLoader;
import org.springframework.webflow.action.BeanInvokingActionFactory;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.engine.FlowAttributeMapper;
import org.springframework.webflow.engine.FlowExecutionExceptionHandler;
import org.springframework.webflow.engine.TargetStateResolver;
import org.springframework.webflow.engine.TransitionCriteria;
import org.springframework.webflow.engine.ViewSelector;
import org.springframework.webflow.engine.builder.FlowArtifactFactory;
import org.springframework.webflow.engine.builder.FlowArtifactLookupException;
import org.springframework.webflow.engine.builder.FlowServiceLocator;
import org.springframework.webflow.execution.Action;

/**
 * Flow service locator that searches flow-local registries first before querying the global, externally managed flow
 * service locator.
 * <p>
 * Internal helper class of the {@link org.springframework.webflow.engine.builder.xml.XmlFlowBuilder}. Package private
 * to highlight it's non-public nature.
 * 
 * @see org.springframework.webflow.engine.builder.xml.XmlFlowBuilder
 * 
 * @author Keith Donald
 */
class LocalFlowServiceLocator implements FlowServiceLocator {

	/**
	 * The stack of registries.
	 */
	private Stack localRegistries = new Stack();

	/**
	 * The parent service locator.
	 */
	private FlowServiceLocator parent;

	/**
	 * Creates a new local service locator.
	 * @param parent the parent service locator
	 */
	public LocalFlowServiceLocator(FlowServiceLocator parent) {
		this.parent = parent;
	}

	/**
	 * Push a new registry onto the stack.
	 * @param registry the local registry
	 */
	public void push(LocalFlowServiceRegistry registry) {
		localRegistries.push(registry);
	}

	/**
	 * Pops all registries off the stack until the stack is empty.
	 */
	public void diposeOfAnyRegistries() {
		while (!localRegistries.isEmpty()) {
			pop();
		}
	}

	/**
	 * Pop a registry off the stack.
	 */
	public LocalFlowServiceRegistry pop() {
		return (LocalFlowServiceRegistry) localRegistries.pop();
	}

	/**
	 * Returns the top registry on the stack.
	 */
	public LocalFlowServiceRegistry top() {
		return (LocalFlowServiceRegistry) localRegistries.peek();
	}

	/**
	 * Returns true if this locator has no local registries.
	 */
	public boolean isEmpty() {
		return localRegistries.isEmpty();
	}

	// implementing FlowServiceLocator

	public Flow getSubflow(String id) throws FlowArtifactLookupException {
		Flow currentFlow = getCurrentFlow();
		// quick check for recursive subflow
		if (currentFlow.getId().equals(id)) {
			return currentFlow;
		}
		// check local inline flows
		if (currentFlow.containsInlineFlow(id)) {
			return currentFlow.getInlineFlow(id);
		}
		// check externally managed top-level flows
		return parent.getSubflow(id);
	}

	public Action getAction(String id) throws FlowArtifactLookupException {
		if (containsBean(id)) {
			return (Action) getBean(id, Action.class);
		} else {
			return parent.getAction(id);
		}
	}

	public FlowAttributeMapper getAttributeMapper(String id) throws FlowArtifactLookupException {
		if (containsBean(id)) {
			return (FlowAttributeMapper) getBean(id, FlowAttributeMapper.class);
		} else {
			return parent.getAttributeMapper(id);
		}
	}

	public TransitionCriteria getTransitionCriteria(String id) throws FlowArtifactLookupException {
		if (containsBean(id)) {
			return (TransitionCriteria) getBean(id, TransitionCriteria.class);
		} else {
			return parent.getTransitionCriteria(id);
		}
	}

	public TargetStateResolver getTargetStateResolver(String id) throws FlowArtifactLookupException {
		if (containsBean(id)) {
			return (TargetStateResolver) getBean(id, TargetStateResolver.class);
		} else {
			return parent.getTargetStateResolver(id);
		}
	}

	public ViewSelector getViewSelector(String id) throws FlowArtifactLookupException {
		if (containsBean(id)) {
			return (ViewSelector) getBean(id, ViewSelector.class);
		} else {
			return parent.getViewSelector(id);
		}
	}

	public FlowExecutionExceptionHandler getExceptionHandler(String id) throws FlowArtifactLookupException {
		if (containsBean(id)) {
			return (FlowExecutionExceptionHandler) getBean(id, FlowExecutionExceptionHandler.class);
		} else {
			return parent.getExceptionHandler(id);
		}
	}

	public FlowArtifactFactory getFlowArtifactFactory() {
		return parent.getFlowArtifactFactory();
	}

	public BeanInvokingActionFactory getBeanInvokingActionFactory() {
		return parent.getBeanInvokingActionFactory();
	}

	public BeanFactory getBeanFactory() {
		return top().getBeanFactory();
	}

	public ResourceLoader getResourceLoader() {
		return parent.getResourceLoader();
	}

	public ExpressionParser getExpressionParser() {
		return parent.getExpressionParser();
	}

	public ConversionService getConversionService() {
		return parent.getConversionService();
	}

	// internal helpers

	/**
	 * Returns the flow for the registry at the top of the stack.
	 */
	protected Flow getCurrentFlow() {
		return top().getFlow();
	}

	/**
	 * Does this flow local service locator contain a bean defintion for the given id?
	 */
	protected boolean containsBean(String id) {
		if (localRegistries.isEmpty()) {
			return false;
		} else {
			return getBeanFactory().containsBean(id);
		}
	}

	/**
	 * Get the identified bean and make sure it is of the required type.
	 */
	protected Object getBean(String id, Class artifactType) throws FlowArtifactLookupException {
		try {
			return getBeanFactory().getBean(id, artifactType);
		} catch (BeansException e) {
			throw new FlowArtifactLookupException(id, artifactType, e);
		}
	}
}