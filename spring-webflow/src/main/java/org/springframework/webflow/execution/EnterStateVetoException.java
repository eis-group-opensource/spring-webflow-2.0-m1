/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution;

import org.springframework.webflow.definition.StateDefinition;

/**
 * Exception thrown to veto the entering of a state of a flow. Typically thrown by {@link FlowExecutionListener} objects
 * that apply security or other runtime constraint checks to flow executions.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public class EnterStateVetoException extends FlowExecutionException {

	/**
	 * The state whose entering was vetoed.
	 */
	private String vetoedStateId;

	/**
	 * Create a new enter state veto exception.
	 * @param flowId the active flow
	 * @param sourceStateId the current state when the veto operation occured
	 * @param vetoedStateId the state for which entering is vetoed
	 * @param message a descriptive message
	 */
	public EnterStateVetoException(String flowId, String sourceStateId, String vetoedStateId, String message) {
		super(flowId, sourceStateId, message);
		this.vetoedStateId = vetoedStateId;
	}

	/**
	 * Create a new enter state veto exception.
	 * @param flowId the active flow
	 * @param sourceStateId the current state when the veto operation occured
	 * @param vetoedStateId the state for which entering is vetoed
	 * @param message a descriptive message
	 * @param cause the underlying cause
	 */
	public EnterStateVetoException(String flowId, String sourceStateId, String vetoedStateId, String message,
			Throwable cause) {
		super(flowId, sourceStateId, message, cause);
		this.vetoedStateId = vetoedStateId;
	}

	/**
	 * Create a new enter state veto exception.
	 * @param context the flow execution request context
	 * @param vetoedState the state for which entering is vetoed
	 * @param message a descriptive message
	 */
	public EnterStateVetoException(RequestContext context, StateDefinition vetoedState, String message) {
		this(context.getActiveFlow().getId(), context.getCurrentState().getId(), vetoedState.getId(), message);
	}

	/**
	 * Create a new enter state veto exception.
	 * @param context the flow execution request context
	 * @param vetoedState the state for which entering is vetoed
	 * @param message a descriptive message
	 * @param cause the underlying cause
	 */
	public EnterStateVetoException(RequestContext context, StateDefinition vetoedState, String message, Throwable cause) {
		this(context.getActiveFlow().getId(), context.getCurrentState().getId(), vetoedState.getId(), message, cause);
	}

	/**
	 * Returns the state for which entering was vetoed.
	 */
	public String getVetoedStateId() {
		return vetoedStateId;
	}
}