/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine;

import org.springframework.webflow.execution.RequestContext;

/**
 * A strategy for calculating the target state of a transition. This facilitates dynamic transition target state
 * resolution that takes into account runtime contextual information.
 * 
 * @author Keith Donald
 */
public interface TargetStateResolver {

	/**
	 * Resolve the target state of the transition from the source state in the current request context. Should never
	 * return null.
	 * @param transition the transition
	 * @param sourceState the source state of the transition, could be null
	 * @param context the current request context
	 * @return the transition's target state
	 */
	public State resolveTargetState(Transition transition, State sourceState, RequestContext context);
}