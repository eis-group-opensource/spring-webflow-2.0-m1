/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.definition.registry;

import org.springframework.core.style.StylerUtils;
import org.springframework.webflow.core.FlowException;

/**
 * Thrown when no flow definition was found during a lookup operation by a flow locator.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public class NoSuchFlowDefinitionException extends FlowException {

	/**
	 * The id of the flow definition that could not be located.
	 */
	private String flowId;

	/**
	 * Creates an exception indicating a flow definition could not be found.
	 * @param flowId the flow id
	 * @param availableFlowIds all flow ids available to the locator generating this exception
	 */
	public NoSuchFlowDefinitionException(String flowId, String[] availableFlowIds) {
		super("No such flow definition with id '" + flowId + "' found; the flows available are: "
				+ StylerUtils.style(availableFlowIds));
		this.flowId = flowId;
	}

	/**
	 * Returns the id of the flow definition that could not be found.
	 */
	public String getFlowId() {
		return flowId;
	}
}