/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.factory;

import org.springframework.webflow.definition.FlowDefinition;

/**
 * Strategy interface that determines if a flow execution listener should attach to executions of a specific flow
 * definition.
 * <p>
 * This selection strategy is typically used by the {@link FlowExecutionListenerLoader} to determine which listeners
 * should apply to which flow definitions.
 * 
 * @see org.springframework.webflow.execution.FlowExecution
 * @see org.springframework.webflow.execution.FlowExecutionListener
 * @see org.springframework.webflow.execution.factory.FlowExecutionListenerLoader
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public interface FlowExecutionListenerCriteria {

	/**
	 * Do the listeners guarded by this criteria object apply to the provided flow definition?
	 * @param definition the flow definition
	 * @return true if yes, false if no
	 */
	public boolean appliesTo(FlowDefinition definition);
}