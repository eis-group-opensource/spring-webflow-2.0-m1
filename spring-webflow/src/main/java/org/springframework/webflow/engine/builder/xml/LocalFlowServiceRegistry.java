/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.builder.xml;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.webflow.engine.Flow;

/**
 * Simple object that holds a reference to a local bean factory housing services needed by a flow definition at
 * execution time.
 * <p>
 * Internal helper class of the {@link org.springframework.webflow.engine.builder.xml.XmlFlowBuilder}. Package private
 * to highlight it's non-public nature.
 * 
 * @see org.springframework.webflow.engine.builder.xml.XmlFlowBuilder
 * @see org.springframework.webflow.engine.builder.xml.LocalFlowServiceLocator
 * 
 * @author Keith Donald
 */
class LocalFlowServiceRegistry {

	/**
	 * The flow this registry is for (and scoped by).
	 */
	private Flow flow;

	/**
	 * The local registry holding the artifacts local to the flow.
	 */
	private BeanFactory beanFactory;

	/**
	 * Create a new local service registry.
	 * @param flow the flow this registry is for (and scoped by)
	 * @param beanFactory the actual backing registry - a Spring bean factory
	 */
	public LocalFlowServiceRegistry(Flow flow, BeanFactory beanFactory) {
		this.flow = flow;
		this.beanFactory = beanFactory;
	}

	/**
	 * Returns the flow this registry is for (and scoped by).
	 */
	public Flow getFlow() {
		return flow;
	}

	/**
	 * Returns the bean factory acting as the physical registry.
	 */
	public BeanFactory getBeanFactory() {
		return beanFactory;
	}
}