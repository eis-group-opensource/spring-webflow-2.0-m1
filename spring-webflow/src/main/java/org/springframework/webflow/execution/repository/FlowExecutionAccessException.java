/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.repository;

/**
 * Base class for exceptions that indicate a flow execution could not be accessed within a repository.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public abstract class FlowExecutionAccessException extends FlowExecutionRepositoryException {

	/**
	 * The key of the execution that could not be accessed.
	 */
	private FlowExecutionKey flowExecutionKey;

	/**
	 * Creates a new flow execution access exception.
	 * @param flowExecutionKey the key of the execution that could not be accessed
	 * @param message a descriptive message
	 */
	public FlowExecutionAccessException(FlowExecutionKey flowExecutionKey, String message) {
		this(flowExecutionKey, message, null);
	}

	/**
	 * Creates a new flow execution access exception.
	 * @param flowExecutionKey the key of the execution that could not be accessed
	 * @param message a descriptive message
	 * @param cause the root cause of the access failure
	 */
	public FlowExecutionAccessException(FlowExecutionKey flowExecutionKey, String message, Exception cause) {
		super(message, cause);
		this.flowExecutionKey = flowExecutionKey;
	}

	/**
	 * Returns key of the flow execution that could not be accessed.
	 */
	public FlowExecutionKey getFlowExecutionKey() {
		return flowExecutionKey;
	}
}