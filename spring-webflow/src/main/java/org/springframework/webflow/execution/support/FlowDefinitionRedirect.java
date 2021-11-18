/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.support;

import java.util.Collections;
import java.util.Map;

import org.springframework.util.Assert;
import org.springframework.webflow.execution.ViewSelection;

/**
 * Concrete response type that requests that a <i>new</i> execution of a flow definition (representing the start of a
 * new conversation) be launched.
 * <p>
 * This allows "redirect to new flow" semantics; useful for restarting a flow after completion, or starting an entirely
 * new flow from within the end state of another flow definition.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public final class FlowDefinitionRedirect extends ViewSelection {

	/**
	 * The id of the flow definition to launch.
	 */
	private final String flowDefinitionId;

	/**
	 * A map of input attributes to pass to the flow.
	 */
	private final Map executionInput;

	/**
	 * Creates a new flow definition redirect.
	 * @param flowDefinitionId the id of the flow definition to launch
	 * @param executionInput the input data to pass to the new flow execution on launch
	 */
	public FlowDefinitionRedirect(String flowDefinitionId, Map executionInput) {
		Assert.hasText(flowDefinitionId, "The flow definition id is required");
		this.flowDefinitionId = flowDefinitionId;
		if (executionInput == null) {
			executionInput = Collections.EMPTY_MAP;
		}
		this.executionInput = executionInput;
	}

	/**
	 * Return the id of the flow definition to launch a new execution of.
	 */
	public String getFlowDefinitionId() {
		return flowDefinitionId;
	}

	/**
	 * Return the flow execution input map as an unmodifiable map. Never returns null.
	 */
	public Map getExecutionInput() {
		return Collections.unmodifiableMap(executionInput);
	}

	public boolean equals(Object o) {
		if (!(o instanceof FlowDefinitionRedirect)) {
			return false;
		}
		FlowDefinitionRedirect other = (FlowDefinitionRedirect) o;
		return flowDefinitionId.equals(other.flowDefinitionId) && executionInput.equals(other.executionInput);
	}

	public int hashCode() {
		return flowDefinitionId.hashCode() + executionInput.hashCode();
	}

	public String toString() {
		return "flowRedirect:'" + flowDefinitionId + "'";
	}
}