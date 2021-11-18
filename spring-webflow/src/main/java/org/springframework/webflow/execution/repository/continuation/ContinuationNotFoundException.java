/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.repository.continuation;

import java.io.Serializable;

import org.springframework.webflow.execution.repository.FlowExecutionRepositoryException;

/**
 * Thrown when no flow execution continuation exists within a continuation group with a particular id. This might occur
 * if the continuation has expired or was explictly invalidated but a client's browser page cache still references it.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public class ContinuationNotFoundException extends FlowExecutionRepositoryException {

	/**
	 * The unique continuation identifier that was not found.
	 */
	private Serializable continuationId;

	/**
	 * Creates a continuation not found exception.
	 * @param continuationId the continuation id that could not be found
	 */
	public ContinuationNotFoundException(Serializable continuationId) {
		super("No flow execution continuation could be found in this group with id '" + continuationId
				+ "' -- perhaps the continuation has expired or has been invalidated? ");
		this.continuationId = continuationId;
	}

	/**
	 * Returns the continuation id that could not be found.
	 */
	public Serializable getContinuationId() {
		return continuationId;
	}
}