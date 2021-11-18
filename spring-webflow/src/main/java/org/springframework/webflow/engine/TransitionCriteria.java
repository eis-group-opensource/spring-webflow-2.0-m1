/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine;

import org.springframework.webflow.execution.RequestContext;

/**
 * Strategy interface encapsulating criteria that determine whether or not a transition should execute given a flow
 * execution request context.
 * 
 * @see org.springframework.webflow.engine.Transition
 * @see org.springframework.webflow.execution.RequestContext
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public interface TransitionCriteria {

	/**
	 * Check if the transition should fire based on the given flow execution request context.
	 * @param context the flow execution request context
	 * @return true if the transition should fire, false otherwise
	 */
	public boolean test(RequestContext context);

}