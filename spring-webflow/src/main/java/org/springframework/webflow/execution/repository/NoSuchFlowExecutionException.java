/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.repository;

/**
 * Thrown when the flow execution with the persistent identifier provided could not be found. This could occur if the
 * execution has been removed from the repository and a client still has a handle to the key.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public class NoSuchFlowExecutionException extends FlowExecutionAccessException {

	/**
	 * Creates a new no such flow execution exception.
	 * @param flowExecutionKey the key of the execution that could not be found
	 * @param cause the root cause of the failure
	 */
	public NoSuchFlowExecutionException(FlowExecutionKey flowExecutionKey, Exception cause) {
		super(flowExecutionKey, "No flow execution could be found with key '" + flowExecutionKey
				+ "' -- perhaps this executing flow has ended or expired? "
				+ "This could happen if your users are relying on browser history "
				+ "(typically via the back button) that references ended flows.", cause);
	}
}