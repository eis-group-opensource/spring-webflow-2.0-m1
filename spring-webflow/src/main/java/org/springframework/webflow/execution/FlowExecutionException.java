/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution;

import org.springframework.webflow.core.FlowException;

/**
 * Base class for exceptions that occur within a flow while it is executing. Can be used directly, but you are
 * encouraged to create a specific subclass for a particular use case.
 * <p>
 * Execution exceptions occur at runtime when the flow is executing requests on behalf of a client. They signal that an
 * execution problem occured: e.g. action execution failed or no transition matched the current request context.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public class FlowExecutionException extends FlowException {

	/**
	 * The id of the flow definition in which the exception occured.
	 */
	private String flowId;

	/**
	 * The state of the flow where the exception occured (optional).
	 */
	private String stateId;

	/**
	 * Creates a new flow execution exception.
	 * @param flowId the flow where the exception occured
	 * @param stateId the state where the exception occured
	 * @param message a descriptive message
	 */
	public FlowExecutionException(String flowId, String stateId, String message) {
		super(message);
		this.stateId = stateId;
		this.flowId = flowId;
	}

	/**
	 * Creates a new flow execution exception.
	 * @param flowId the flow where the exception occured
	 * @param stateId the state where the exception occured
	 * @param message a descriptive message
	 * @param cause the root cause
	 */
	public FlowExecutionException(String flowId, String stateId, String message, Throwable cause) {
		super(message, cause);
		this.stateId = stateId;
		this.flowId = flowId;
	}

	/**
	 * Returns the id of the flow definition that was executing when this exception occured.
	 */
	public String getFlowId() {
		return flowId;
	}

	/**
	 * Returns the id of the state definition where the exception occured. Could be null if no state was active at the
	 * time when the exception was thrown.
	 */
	public String getStateId() {
		return stateId;
	}
}