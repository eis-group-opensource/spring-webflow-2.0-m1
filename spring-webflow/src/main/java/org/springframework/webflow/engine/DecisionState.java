/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine;

import org.springframework.webflow.execution.FlowExecutionException;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.ViewSelection;

/**
 * A simple transitionable state that when entered will execute the first transition whose matching criteria evaluates
 * to <code>true</code> in the {@link RequestContext context} of the current request.
 * <p>
 * A decision state is a convenient, simple way to encapsulate reusable state transition logic in one place.
 * 
 * @author Keith Donald
 */
public class DecisionState extends TransitionableState {

	/**
	 * Creates a new decision state.
	 * @param flow the owning flow
	 * @param stateId the state identifier (must be unique to the flow)
	 * @throws IllegalArgumentException when this state cannot be added to given flow, e.g. because the id is not unique
	 */
	public DecisionState(Flow flow, String stateId) throws IllegalArgumentException {
		super(flow, stateId);
	}

	/**
	 * Specialization of State's <code>doEnter</code> template method that executes behaviour specific to this state
	 * type in polymorphic fashion.
	 * <p>
	 * Simply looks up the first transition that matches the state of the context and executes it.
	 * @param context the control context for the currently executing flow, used by this state to manipulate the flow
	 * execution
	 * @return a view selection containing model and view information needed to render the results of the state
	 * execution
	 * @throws FlowExecutionException if an exception occurs in this state
	 */
	protected ViewSelection doEnter(RequestControlContext context) throws FlowExecutionException {
		return getRequiredTransition(context).execute(this, context);
	}
}