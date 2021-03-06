/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.support;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.webflow.engine.FlowVariable;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.ScopeType;

/**
 * A concrete flow variable subclass that obtains variable values from a Spring {@link BeanFactory}.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public class BeanFactoryFlowVariable extends FlowVariable {

	/**
	 * The name of the bean whose value will be used as the flow variable. The bean should be a prototype.
	 */
	private String beanName;

	/**
	 * The bean factory where initial variable values will be obtained.
	 */
	private BeanFactory beanFactory;

	/**
	 * Convenience constructor to create a new bean factory flow variable. Defaults the bean name to the variable name.
	 * @param name the variable name which will also be used as the bean name
	 * @param beanFactory the bean factory where initial variable values will be obtained
	 * @param scope the variable scope
	 * @since 1.0.2
	 */
	public BeanFactoryFlowVariable(String name, BeanFactory beanFactory, ScopeType scope) {
		this(name, name, beanFactory, scope);
	}

	/**
	 * Creates a new bean factory flow variable.
	 * @param variableName the variable name
	 * @param beanName the bean name, will default to the variable name if not specified
	 * @param beanFactory the bean factory where initial variable values will be obtained
	 * @param scope the variable scope
	 */
	public BeanFactoryFlowVariable(String variableName, String beanName, BeanFactory beanFactory, ScopeType scope) {
		super(variableName, scope);
		if (StringUtils.hasText(beanName)) {
			this.beanName = beanName;
		} else {
			this.beanName = variableName;
		}
		Assert.notNull(beanFactory, "The bean factory is required");
		Assert.isTrue(!beanFactory.isSingleton(this.beanName), "The bean with name '" + this.beanName
				+ "' must be a prototype (singleton=false)");
		this.beanFactory = beanFactory;
	}

	protected Object createVariableValue(RequestContext context) {
		return beanFactory.getBean(beanName);
	}
}