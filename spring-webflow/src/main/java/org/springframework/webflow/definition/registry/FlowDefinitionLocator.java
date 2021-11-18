/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.definition.registry;

import org.springframework.webflow.definition.FlowDefinition;

/**
 * A runtime service locator interface for retrieving flow definitions by <code>id</code>.
 * <p>
 * Flow locators are needed by flow executors at runtime to retrieve fully-configured flow definitions to support
 * launching new flow executions.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public interface FlowDefinitionLocator {

	/**
	 * Lookup the flow definition with the specified <code>path</code>.
	 * @param flowPath the flow definition path
	 * @return the flow definition
	 * @throws NoSuchFlowDefinitionException when the flow definition with the specified id does not exist
	 * @throws FlowDefinitionConstructionException if there is a problem constructing the identified flow definition
	 */
	public FlowDefinition getFlowDefinition(String flowPath) throws NoSuchFlowDefinitionException,
			FlowDefinitionConstructionException;
}