/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.repository.continuation;

import org.springframework.webflow.execution.FlowExecution;
import org.springframework.webflow.execution.repository.FlowExecutionRepositoryException;

/**
 * Thrown when a FlowExecutionContinuation could not be deserialized into a FlowExecution.
 * 
 * @see FlowExecutionContinuation
 * @see FlowExecution
 * 
 * @author Keith Donald
 */
public class ContinuationUnmarshalException extends FlowExecutionRepositoryException {

	/**
	 * Creates a new flow execution unmarshalling exception.
	 * @param message the exception message
	 * @param cause the cause
	 */
	public ContinuationUnmarshalException(String message, Throwable cause) {
		super(message, cause);
	}
}