/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.definition.registry;

import org.springframework.webflow.core.FlowException;

/**
 * Thrown when a flow definition was found during a lookup operation but could not be constructed.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public abstract class FlowDefinitionConstructionException extends FlowException {

	/**
	 * The id of the flow that could not be constructed.
	 */
	private String flowId;

	/**
	 * Creates an exception indicating a flow definition could not be constructed.
	 * @param flowId the flow id
	 * @param cause underlying cause of the exception
	 */
	public FlowDefinitionConstructionException(String flowId, Throwable cause) {
		super("An exception occured constructing the flow with id '" + flowId + "'", cause);
		this.flowId = flowId;
	}

	/**
	 * Returns the id of the flow definition that could not be constructed.
	 * @return the flow id
	 */
	public String getFlowId() {
		return flowId;
	}
}