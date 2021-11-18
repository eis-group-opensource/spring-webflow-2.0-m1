/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.action;

import java.io.Serializable;

import org.springframework.binding.method.MethodSignature;
import org.springframework.util.Assert;
import org.springframework.webflow.execution.RequestContext;

/**
 * Thin action proxy that delegates to a method on an arbitrary bean. The bean instance is managed locally by this
 * Action in an instance variable.
 * 
 * @author Keith Donald
 */
class LocalBeanInvokingAction extends AbstractBeanInvokingAction implements Serializable {

	/**
	 * The target bean (any POJO) to invoke.
	 */
	private Object bean;

	/**
	 * Creates a bean invoking action that invokes a method on the specified bean. The bean may be a proxy providing a
	 * layer of indirection if necessary.
	 * @param bean the bean to invoke
	 */
	public LocalBeanInvokingAction(MethodSignature methodSignature, Object bean) {
		super(methodSignature);
		Assert.notNull(bean, "The bean to invoke by this action cannot be null");
		this.bean = bean;
	}

	/**
	 * Returns the target bean to invoke methods on.
	 */
	public Object getBean() {
		return bean;
	}

	protected Object getBean(RequestContext context) throws Exception {
		return getBean();
	}
}