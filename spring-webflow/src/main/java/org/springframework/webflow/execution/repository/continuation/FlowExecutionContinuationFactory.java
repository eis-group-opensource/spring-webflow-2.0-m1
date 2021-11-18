/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.repository.continuation;

import org.springframework.webflow.execution.FlowExecution;

/**
 * A factory for creating different {@link FlowExecutionContinuation} implementations.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public interface FlowExecutionContinuationFactory {

	/**
	 * Creates a new flow execution continuation for given flow execution.
	 * @param flowExecution the flow execution
	 * @return the continuation
	 * @throws ContinuationCreationException when the continuation cannot be created
	 */
	public FlowExecutionContinuation createContinuation(FlowExecution flowExecution)
			throws ContinuationCreationException;

	/**
	 * Creates a new flow execution continuation from the provided byte array.
	 * @param bytes the flow execution byte array
	 * @return the continuation
	 * @throws ContinuationCreationException when the continuation cannot be created
	 */
	public FlowExecutionContinuation createContinuation(byte[] bytes) throws ContinuationCreationException;
}