/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.method;

import org.springframework.core.style.ToStringCreator;
import org.springframework.util.Assert;

/**
 * A specification for a method consisting of the methodName and an optional set of named arguments. This class provides
 * the ability to resolve a method with parameters and evaluate its argument values as part of a
 * {@link MethodInvoker method invoker attempt}.
 * 
 * @author Keith Donald
 */
public class MethodSignature {

	/**
	 * The name of the method, e.g "execute".
	 */
	private String methodName;

	/**
	 * The parameter types of the method, e.g "int param1".
	 */
	private Parameters parameters;

	/**
	 * Creates a method signature with no parameters.
	 * @param methodName the name of the method
	 */
	public MethodSignature(String methodName) {
		this(methodName, Parameters.NONE);
	}

	/**
	 * Creates a method signature with a single parameter.
	 * @param methodName the name of the method
	 * @param parameter the method parameter
	 */
	public MethodSignature(String methodName, Parameter parameter) {
		this(methodName, new Parameters(parameter));
	}

	/**
	 * Creates a method signature with a list of parameters.
	 * @param methodName the name of the method
	 * @param parameters the method parameters
	 */
	public MethodSignature(String methodName, Parameters parameters) {
		Assert.notNull(methodName, "The method name is required");
		Assert.notNull(parameters, "The parameters are required");
		this.methodName = methodName;
		this.parameters = parameters;
	}

	/**
	 * Returns the method name.
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * Returns the method parameters.
	 */
	public Parameters getParameters() {
		return parameters;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof MethodSignature)) {
			return false;
		}
		MethodSignature other = (MethodSignature) obj;
		return methodName.equals(other.methodName) && parameters.equals(other.parameters);
	}

	public int hashCode() {
		return methodName.hashCode() + parameters.hashCode();
	}

	public String toString() {
		return new ToStringCreator(this).append("methodName", methodName).append("parameters", parameters).toString();
	}
}