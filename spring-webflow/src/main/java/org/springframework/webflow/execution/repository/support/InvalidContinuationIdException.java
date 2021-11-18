/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.repository.support;

import java.io.Serializable;

import org.springframework.webflow.execution.repository.FlowExecutionRepositoryException;

/**
 * Thrown when no flow execution continuation exists with the provided id. This might occur if the continuation has
 * expired or was explictly invalidated but a client's browser page cache still references it.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public class InvalidContinuationIdException extends FlowExecutionRepositoryException {

	/**
	 * The unique continuation identifier that was invalid.
	 */
	private Serializable continuationId;

	/**
	 * Creates an invalid continuation id exception.
	 * @param continuationId the invalid continuation id
	 */
	public InvalidContinuationIdException(Serializable continuationId) {
		super("The continuation id '" + continuationId + "' is invalid.  Access to flow execution denied.");
		this.continuationId = continuationId;
	}

	/**
	 * Returns the continuation id.
	 */
	public Serializable getContinuationId() {
		return continuationId;
	}
}