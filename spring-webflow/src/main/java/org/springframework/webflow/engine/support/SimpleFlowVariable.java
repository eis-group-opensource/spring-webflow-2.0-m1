/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.support;

import java.lang.reflect.Modifier;

import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.webflow.engine.FlowVariable;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.ScopeType;

/**
 * A trivial concrete flow variable subclass that creates new variable values using Java reflection.
 * 
 * @author Keith Donald
 */
public class SimpleFlowVariable extends FlowVariable {

	/**
	 * The concrete variable value class.
	 */
	private Class variableClass;

	/**
	 * Creates a new simple flow variable.
	 * @param name the variable name
	 * @param variableClass the concrete variable class
	 * @param scope the variable scope
	 */
	public SimpleFlowVariable(String name, Class variableClass, ScopeType scope) {
		super(name, scope);
		Assert.notNull(variableClass, "The variable class is required");
		Assert.isTrue(!variableClass.isInterface(), "The variable class cannot be an interface");
		Assert.isTrue(!Modifier.isAbstract(variableClass.getModifiers()), "The variable class cannot be abstract");
		this.variableClass = variableClass;
	}

	/**
	 * Returns the variable value class.
	 */
	public Class getVariableClass() {
		return variableClass;
	}

	protected Object createVariableValue(RequestContext context) {
		return BeanUtils.instantiateClass(variableClass);
	}
}