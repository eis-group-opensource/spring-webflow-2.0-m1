/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.factory;

import java.util.Iterator;
import java.util.Set;

import org.springframework.core.CollectionFactory;
import org.springframework.util.Assert;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.execution.FlowExecutionListener;

/**
 * A holder that holds a listener plus a set of criteria defining the flows in which that listener applies.
 * <p>
 * This is an internal helper class used by the {@link ConditionalFlowExecutionListenerLoader}.
 * 
 * @see ConditionalFlowExecutionListenerLoader
 * 
 * @author Keith Donald
 */
class ConditionalFlowExecutionListenerHolder {

	/**
	 * The held listener.
	 */
	private FlowExecutionListener listener;

	/**
	 * The listener criteria set.
	 */
	private Set criteriaSet = CollectionFactory.createLinkedSetIfPossible(3);

	/**
	 * Create a new conditional flow execution listener holder.
	 * @param listener the listener to hold
	 */
	public ConditionalFlowExecutionListenerHolder(FlowExecutionListener listener) {
		Assert.notNull(listener, "The listener is required");
		this.listener = listener;
	}

	/**
	 * Returns the held listener.
	 */
	public FlowExecutionListener getListener() {
		return listener;
	}

	/**
	 * Add given criteria.
	 */
	public void add(FlowExecutionListenerCriteria criteria) {
		criteriaSet.add(criteria);
	}

	/**
	 * Remove given criteria. If not present, does nothing.
	 */
	public void remove(FlowExecutionListenerCriteria criteria) {
		criteriaSet.remove(criteria);
	}

	/**
	 * Are there any criteria registered?
	 */
	public boolean isCriteriaSetEmpty() {
		return criteriaSet.isEmpty();
	}

	/**
	 * Determines if the listener held by this holder applies to the specified flow definition. Will do a logical OR
	 * between the registered criteria.
	 * @param flowDefinition the flow
	 * @return true if yes, false otherwise
	 */
	public boolean listenerAppliesTo(FlowDefinition flowDefinition) {
		Iterator it = criteriaSet.iterator();
		while (it.hasNext()) {
			FlowExecutionListenerCriteria criteria = (FlowExecutionListenerCriteria) it.next();
			if (criteria.appliesTo(flowDefinition)) {
				return true;
			}
		}
		return false;
	}
}