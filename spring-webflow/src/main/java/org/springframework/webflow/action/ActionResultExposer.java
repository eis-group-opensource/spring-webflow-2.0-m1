/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.action;

import java.io.Serializable;

import org.springframework.core.style.ToStringCreator;
import org.springframework.util.Assert;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.ScopeType;

/**
 * Specifies how an action result value should be exposed to an executing flow. The return value is exposed as an
 * attribute in a configured scope.
 * 
 * @see EvaluateAction
 * @see AbstractBeanInvokingAction
 * 
 * @author Keith Donald
 */
public class ActionResultExposer implements Serializable {

	/**
	 * The name of the attribute to index the return value with.
	 */
	private String resultName;

	/**
	 * The scope of the attribute indexing the return value.
	 */
	private ScopeType resultScope;

	/**
	 * Creates a action result exposer
	 * @param resultName the result name
	 * @param resultScope the result scope
	 */
	public ActionResultExposer(String resultName, ScopeType resultScope) {
		Assert.notNull(resultName, "The result name is required");
		Assert.notNull(resultScope, "The result scope is required");
		this.resultName = resultName;
		this.resultScope = resultScope;
	}

	/**
	 * Returns name of the attribute to index the return value with.
	 */
	public String getResultName() {
		return resultName;
	}

	/**
	 * Returns the scope the attribute indexing the return value.
	 */
	public ScopeType getResultScope() {
		return resultScope;
	}

	/**
	 * Expose given bean method return value in given flow execution request context.
	 * @param result the return value
	 * @param context the request context
	 */
	public void exposeResult(Object result, RequestContext context) {
		resultScope.getScope(context).put(resultName, result);
	}

	public String toString() {
		return new ToStringCreator(this).append("resultName", resultName).append("resultScope", resultScope).toString();
	}
}