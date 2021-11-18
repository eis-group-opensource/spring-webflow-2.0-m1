/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.factory;

import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.execution.FlowExecutionFactory;
import org.springframework.webflow.execution.FlowExecutionListener;

/**
 * A strategy interface for loading the set of FlowExecutionListener's that should apply to executions of a given flow
 * definition. Typically used by a {@link FlowExecutionFactory} as part of execution creation.
 * 
 * @author Keith Donald
 */
public interface FlowExecutionListenerLoader {

	/**
	 * Get the flow execution listeners that apply to the given flow definition.
	 * @param flowDefinition the flow definition
	 * @return the listeners that apply
	 */
	public FlowExecutionListener[] getListeners(FlowDefinition flowDefinition);
}