/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine;

import org.springframework.webflow.core.collection.AttributeMap;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.FlowExecutionException;

/**
 * Thrown if an unhandled exception occurs when an action is executed. Typically wraps another exception noting the root
 * cause failure. The root cause may be checked or unchecked.
 * 
 * @see org.springframework.webflow.execution.Action
 * @see org.springframework.webflow.engine.ActionState
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public class ActionExecutionException extends FlowExecutionException {

	/**
	 * Create a new action execution exception.
	 * @param flowId the current flow
	 * @param stateId the current state (may be null)
	 * @param action the action that generated an unrecoverable exception
	 * @param executionAttributes action execution properties that may have contributed to this failure
	 * @param cause the underlying cause
	 */
	public ActionExecutionException(String flowId, String stateId, Action action, AttributeMap executionAttributes,
			Throwable cause) {
		super(flowId, stateId, "Exception thrown executing " + action + " in state '" + stateId + "' of flow '"
				+ flowId + "' -- action execution attributes were '" + executionAttributes + "'", cause);
	}

	/**
	 * Create a new action execution exception.
	 * @param flowId the current flow
	 * @param stateId the current state (may be null)
	 * @param action the action that generated an unrecoverable exception
	 * @param executionAttributes action execution properties that may have contributed to this failure
	 * @param message a descriptive message
	 * @param cause the underlying cause
	 */
	public ActionExecutionException(String flowId, String stateId, Action action, AttributeMap executionAttributes,
			String message, Throwable cause) {
		super(flowId, stateId, message, cause);
	}

}