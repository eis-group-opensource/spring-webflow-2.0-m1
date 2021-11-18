/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.method;

import org.springframework.binding.expression.EvaluationContext;
import org.springframework.binding.expression.Expression;
import org.springframework.core.style.ToStringCreator;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * A named method parameter. Each parameter has an identifying name and is of a specified type (class).
 * 
 * @author Keith Donald
 */
public class Parameter {

	/**
	 * The class of the parameter, e.g "springbank.AccountNumber".
	 */
	private Class type;

	/**
	 * The name of the parameter as an evaluatable expression, e.g "accountNumber".
	 */
	private Expression name;

	/**
	 * Create a new named parameter definition. Named parameters are capable of resolving parameter values (arguments)
	 * from argument sources.
	 * @param type the parameter type, may be null
	 * @param name the name the method argument expression (required)
	 */
	public Parameter(Class type, Expression name) {
		Assert.notNull(name, "The parameter name expression is required");
		this.type = type;
		this.name = name;
	}

	/**
	 * Returns the parameter type. Could be null if no parameter type was specified.
	 */
	public Class getType() {
		return type;
	}

	/**
	 * Returns the parameter name.
	 */
	public Expression getName() {
		return name;
	}

	/**
	 * Evaluate this method parameter against the provided argument source, returning a single method argument value.
	 * @param argumentSource the meyhod argument source
	 * @param context the evaluation context
	 * @return the method argument value
	 */
	public Object evaluateArgument(Object argumentSource, EvaluationContext context) {
		return name.evaluate(argumentSource, context);
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof Parameter)) {
			return false;
		}
		Parameter other = (Parameter) obj;
		return ObjectUtils.nullSafeEquals(type, other.type) && name.equals(other.name);
	}

	public int hashCode() {
		return (type != null ? type.hashCode() : 0) + name.hashCode();
	}

	public String toString() {
		return new ToStringCreator(this).append("type", type).append("name", name).toString();
	}
}