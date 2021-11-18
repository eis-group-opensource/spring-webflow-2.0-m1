/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.repository;

import org.springframework.webflow.core.FlowException;

/**
 * The root of the {@link FlowExecutionRepository} exception hierarchy. Indicates a problem occured either saving,
 * restoring, or invalidating a managed flow execution.
 * 
 * @author Erwin Vervaet
 * @author Keith Donald
 */
public abstract class FlowExecutionRepositoryException extends FlowException {

	/**
	 * Creates a new flow execution repository exception.
	 * @param message a descriptive message
	 */
	public FlowExecutionRepositoryException(String message) {
		super(message);
	}

	/**
	 * Creates a new flow execution repository exception.
	 * @param message a descriptive message
	 * @param cause the root cause of the problem
	 */
	public FlowExecutionRepositoryException(String message, Throwable cause) {
		super(message, cause);
	}
}