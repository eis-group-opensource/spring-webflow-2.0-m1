/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.support;

import org.springframework.util.Assert;
import org.springframework.webflow.engine.TransitionCriteria;
import org.springframework.webflow.execution.RequestContext;

/**
 * Transition criteria that negates the result of the evaluation of another criteria object.
 * 
 * @author Keith Donald
 */
public class NotTransitionCriteria implements TransitionCriteria {

	/**
	 * The criteria to negate.
	 */
	private TransitionCriteria criteria;

	/**
	 * Create a new transition criteria object that will negate the result of given criteria object.
	 * @param criteria the criteria to negate
	 */
	public NotTransitionCriteria(TransitionCriteria criteria) {
		Assert.notNull(criteria, "The criteria object to negate is required");
		this.criteria = criteria;
	}

	public boolean test(RequestContext context) {
		return !criteria.test(context);
	}

	public String toString() {
		return "[not(" + criteria + ")]";
	}
}