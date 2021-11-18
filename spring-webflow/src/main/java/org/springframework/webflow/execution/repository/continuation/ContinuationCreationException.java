/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.repository.continuation;

import org.springframework.webflow.execution.FlowExecution;
import org.springframework.webflow.execution.repository.FlowExecutionRepositoryException;

/**
 * Thrown when a continuation snapshot could not be taken of flow execution state.
 * 
 * @author Keith Donald
 */
public class ContinuationCreationException extends FlowExecutionRepositoryException {

	/**
	 * The flow execution that could not be snapshotted.
	 */
	private FlowExecution flowExecution;

	/**
	 * Creates a new continuation creation exception.
	 * @param flowExecution the flow execution
	 * @param message a descriptive message
	 * @param cause the cause
	 */
	public ContinuationCreationException(FlowExecution flowExecution, String message, Throwable cause) {
		super(message, cause);
		this.flowExecution = flowExecution;
	}

	/**
	 * Returns the flow execution that could not be snapshotted.
	 */
	public FlowExecution getFlowExecution() {
		return flowExecution;
	}
}