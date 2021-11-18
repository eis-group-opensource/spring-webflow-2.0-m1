/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.repository;

/**
 * Thrown when the flow execution with the persistent identifier provided could not be restored.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public class FlowExecutionRestorationFailureException extends FlowExecutionAccessException {

	/**
	 * Creates a new flow execution restoration failure exception.
	 * @param flowExecutionKey the key of the execution that could not be restored
	 * @param cause the root cause of the restoration failure
	 */
	public FlowExecutionRestorationFailureException(FlowExecutionKey flowExecutionKey, Exception cause) {
		super(flowExecutionKey, "A problem occurred restoring the flow execution with key '" + flowExecutionKey + "'",
				cause);
	}
}