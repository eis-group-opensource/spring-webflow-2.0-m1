/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.definition.registry;

import org.springframework.webflow.definition.FlowDefinition;

/**
 * A simple flow definition holder that just holds a constant singleton reference to a flow definition.
 * 
 * @author Keith Donald
 */
public final class StaticFlowDefinitionHolder implements FlowDefinitionHolder {

	/**
	 * The held flow definition.
	 */
	private final FlowDefinition flowDefinition;

	/**
	 * Creates the static flow definition holder.
	 * @param flowDefinition the flow to hold
	 */
	public StaticFlowDefinitionHolder(FlowDefinition flowDefinition) {
		this.flowDefinition = flowDefinition;
	}

	public String getFlowDefinitionId() {
		return flowDefinition.getId();
	}

	public FlowDefinition getFlowDefinition() {
		return flowDefinition;
	}

	public void refresh() {
		// nothing to do
	}
}