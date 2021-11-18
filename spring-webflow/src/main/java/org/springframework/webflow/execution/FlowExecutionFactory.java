/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution;

import org.springframework.webflow.definition.FlowDefinition;

/**
 * An abstract factory for creating flow exections. A flow execution represents a runtime, top-level instance of a flow
 * definition.
 * <p>
 * This factory provides encapsulation of the flow execution implementation type, as well as its construction and
 * assembly process.
 * <p>
 * Flow execution factories are responsible for registering {@link FlowExecutionListener listeners} with the constructed
 * flow execution.
 * 
 * @see FlowExecution
 * @see FlowDefinition
 * @see FlowExecutionListener
 * 
 * @author Keith Donald
 */
public interface FlowExecutionFactory {

	// TODO: should this class be moved to the execution.factory package for clarity
	// and to align it with package structuring for flow execution repositories?

	/**
	 * Create a new flow execution product for the given flow definition.
	 * @param flowDefinition the flow definition
	 * @return the new flow execution, fully initialized and awaiting to be started
	 * @see FlowExecution#start(org.springframework.webflow.core.collection.MutableAttributeMap,
	 * org.springframework.webflow.context.ExternalContext)
	 */
	public FlowExecution createFlowExecution(FlowDefinition flowDefinition);
}