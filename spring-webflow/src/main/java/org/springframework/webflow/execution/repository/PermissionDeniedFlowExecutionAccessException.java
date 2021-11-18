/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.repository;

/**
 * Thrown when access to a flow execution was denied by a repository.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public class PermissionDeniedFlowExecutionAccessException extends FlowExecutionAccessException {

	/**
	 * Creates a new flow execution restoration exception.
	 * @param flowExecutionKey the key of the execution that could not be accessed
	 * @param cause the root cause of the access failure
	 */
	public PermissionDeniedFlowExecutionAccessException(FlowExecutionKey flowExecutionKey, Exception cause) {
		super(flowExecutionKey, "Unable to restore flow execution with key '" + flowExecutionKey
				+ "' -- permission denied.", cause);
	}
}