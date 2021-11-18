/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.faces.webflow.el;

import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ReferenceSyntaxException;

import org.springframework.webflow.execution.FlowExecution;

/**
 * Base class for property resolvers that get and set flow execution attributes.
 * 
 * @author Keith Donald
 */
public abstract class AbstractFlowExecutionPropertyResolver extends PropertyResolver {

	/**
	 * The standard property resolver to delegate to if this one doesn't apply.
	 */
	private final PropertyResolver resolverDelegate;

	/**
	 * Creates a new flow executon property resolver
	 * @param resolverDelegate the resolver to delegate to when the property is not a flow execution attribute
	 */
	public AbstractFlowExecutionPropertyResolver(PropertyResolver resolverDelegate) {
		this.resolverDelegate = resolverDelegate;
	}

	/**
	 * Returns the property resolver this resolver delegates to if necessary.
	 */
	protected final PropertyResolver getResolverDelegate() {
		return resolverDelegate;
	}

	public Class getType(Object base, Object property) throws EvaluationException, PropertyNotFoundException {
		if (base instanceof FlowExecution) {
			FlowExecution execution = (FlowExecution) base;
			assertPropertyNameValid(property);
			return doGetAttributeType(execution, (String) property);
		} else {
			return resolverDelegate.getType(base, property);
		}
	}

	public Class getType(Object base, int index) throws EvaluationException, PropertyNotFoundException {
		if (base instanceof FlowExecution) {
			// cannot access flow execution by index so we cannot determine type. Return null per JSF spec
			return null;
		} else {
			return resolverDelegate.getType(base, index);
		}
	}

	public Object getValue(Object base, Object property) throws EvaluationException, PropertyNotFoundException {
		if (base instanceof FlowExecution) {
			FlowExecution execution = (FlowExecution) base;
			assertPropertyNameValid(property);
			return doGetAttribute(execution, (String) property);
		} else {
			return resolverDelegate.getValue(base, property);
		}
	}

	public Object getValue(Object base, int index) throws EvaluationException, PropertyNotFoundException {
		if (base instanceof FlowExecution) {
			throw new ReferenceSyntaxException("Cannot apply an index value to a flow execution");
		} else {
			return resolverDelegate.getValue(base, index);
		}
	}

	public boolean isReadOnly(Object base, Object property) throws EvaluationException, PropertyNotFoundException {
		if (base instanceof FlowExecution) {
			return false;
		} else {
			return resolverDelegate.isReadOnly(base, property);
		}
	}

	public boolean isReadOnly(Object base, int index) throws EvaluationException, PropertyNotFoundException {
		if (base instanceof FlowExecution) {
			return false;
		} else {
			return resolverDelegate.isReadOnly(base, index);
		}
	}

	public void setValue(Object base, Object property, Object value) throws EvaluationException,
			PropertyNotFoundException {
		if ((base instanceof FlowExecution)) {
			FlowExecution execution = (FlowExecution) base;
			assertPropertyNameValid(property);
			doSetAttribute(execution, (String) property, value);
		} else {
			resolverDelegate.setValue(base, property, value);
		}
	}

	public void setValue(Object base, int index, Object value) throws EvaluationException, PropertyNotFoundException {
		if (base instanceof FlowExecution) {
			throw new ReferenceSyntaxException("Cannot apply an index value to a flow execution");
		} else {
			resolverDelegate.setValue(base, index, value);
		}
	}

	// helpers

	private void assertPropertyNameValid(Object property) {
		if (property == null) {
			throw new PropertyNotFoundException("The name of the flow execution attribute cannot be null");
		}
		if (!(property instanceof String)) {
			throw new PropertyNotFoundException("Flow execution attribute names must be strings but " + property
					+ " was not");
		}
		if (((String) property).length() == 0) {
			throw new PropertyNotFoundException("The name of the flow execution attribute cannot be blank");
		}
	}

	/**
	 * Gets the type of value returned by the flow execution attribute.
	 * @param execution the flow execution
	 * @param attributeName the name of the attribute
	 * @return the type of value returned by the attribute
	 */
	protected abstract Class doGetAttributeType(FlowExecution execution, String attributeName);

	/**
	 * Gets the value of the flow execution attribute.
	 * @param execution the flow execution
	 * @param attributeName the name of the attribute
	 * @return the attribute value
	 */
	protected abstract Object doGetAttribute(FlowExecution execution, String attributeName);

	/**
	 * Sets the value of the flow execution attribute.
	 * @param execution the flow execution
	 * @param attributeName the name of the attribute
	 * @param attributeValue the attribute value
	 */
	protected abstract void doSetAttribute(FlowExecution execution, String attributeName, Object attributeValue);

}