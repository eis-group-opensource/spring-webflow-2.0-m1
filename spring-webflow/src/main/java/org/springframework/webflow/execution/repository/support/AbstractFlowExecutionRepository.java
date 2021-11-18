/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.repository.support;

import org.springframework.util.Assert;
import org.springframework.webflow.execution.FlowExecution;
import org.springframework.webflow.execution.FlowExecutionFactory;
import org.springframework.webflow.execution.repository.FlowExecutionRepository;

/**
 * Abstract base class for flow execution repository implementations. Does not make any assumptions about the storage
 * medium used to store active flow executions. Mandates the use of a {@link FlowExecutionStateRestorer}, used to
 * rehydrate a flow execution after it has been obtained from storage from resume.
 * <p>
 * The configured {@link FlowExecutionStateRestorer} should be compatible with the chosen {@link FlowExecution}
 * implementation and is configuration as done by a {@link FlowExecutionFactory} (listeners, execution attributes, ...).
 * 
 * @author Erwin Vervaet
 */
public abstract class AbstractFlowExecutionRepository implements FlowExecutionRepository {

	/**
	 * The strategy for restoring transient flow execution state after obtaining it from storage.
	 */
	private FlowExecutionStateRestorer executionStateRestorer;

	/**
	 * Constructor for use in subclasses.
	 * @param executionStateRestorer the transient flow execution state restorer
	 */
	protected AbstractFlowExecutionRepository(FlowExecutionStateRestorer executionStateRestorer) {
		setExecutionStateRestorer(executionStateRestorer);
	}

	/**
	 * Returns the strategy for restoring transient flow execution state after obtaining it from storage.
	 * @return the transient flow execution state restorer
	 */
	protected FlowExecutionStateRestorer getExecutionStateRestorer() {
		return executionStateRestorer;
	}

	/**
	 * Sets the strategy for restoring transient flow execution state after obtaining it from storage.
	 * @param executionStateRestorer the transient flow execution state restorer, may not be null
	 */
	private void setExecutionStateRestorer(FlowExecutionStateRestorer executionStateRestorer) {
		Assert.notNull(executionStateRestorer, "The flow execution state restorer is required");
		this.executionStateRestorer = executionStateRestorer;
	}

}
