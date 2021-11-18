/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.definition.registry;

import org.springframework.webflow.definition.FlowDefinition;

/**
 * A holder holding a reference to a Flow definition. Provides a layer of indirection, enabling things like
 * "hot-reloadable" flow definitions.
 * 
 * @see FlowDefinitionRegistry#registerFlowDefinition(FlowDefinitionHolder)
 * 
 * @author Keith Donald
 */
public interface FlowDefinitionHolder {

	/**
	 * Returns the <code>id</code> of the flow definition held by this holder. This is a <i>lightweight</i> method
	 * callers may call to obtain the id of the flow without triggering full flow definition assembly (which may be an
	 * expensive operation).
	 */
	public String getFlowDefinitionId();

	/**
	 * Returns the flow definition held by this holder. Calling this method the first time may trigger flow assembly
	 * (which may be expensive).
	 * @throws FlowDefinitionConstructionException if there is a problem constructing the target flow definition
	 */
	public FlowDefinition getFlowDefinition() throws FlowDefinitionConstructionException;

	/**
	 * Refresh the flow definition held by this holder. Calling this method typically triggers flow re-assembly, which
	 * may include a refresh from an externalized resource such as a file.
	 * @throws FlowDefinitionConstructionException if there is a problem constructing the target flow definition
	 */
	public void refresh() throws FlowDefinitionConstructionException;
}