/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.faces.webflow;

import java.io.Serializable;

import org.springframework.core.style.ToStringCreator;
import org.springframework.webflow.execution.FlowExecution;
import org.springframework.webflow.execution.ViewSelection;
import org.springframework.webflow.execution.repository.FlowExecutionKey;
import org.springframework.webflow.execution.repository.FlowExecutionLock;

/**
 * A holder storing a reference to a flow execution and the key of that flow execution if it has been (or is about to
 * be) managed in a repository.
 * 
 * @author Keith Donald
 */
public class FlowExecutionHolder implements Serializable {

	/**
	 * The flow execution continuation key (may be null if the flow execution has not yet been generated a repository
	 * key). May change as well over the life of this object, as a flow execution can be given a new key to capture its
	 * state at another point in time.
	 */
	private FlowExecutionKey flowExecutionKey;

	/**
	 * The held flow execution representing the state of an ongoing conversation at a point in time.
	 */
	private FlowExecution flowExecution;

	/**
	 * The lock obtained to exclusively manipulate the flow execution.
	 */
	private FlowExecutionLock flowExecutionLock;

	/**
	 * The currently selected view selection for this request.
	 */
	private ViewSelection viewSelection;

	/**
	 * Creates a new flow execution holder for a flow execution that has not yet been placed in a repository.
	 * @param flowExecution the flow execution to hold
	 */
	public FlowExecutionHolder(FlowExecution flowExecution) {
		this.flowExecution = flowExecution;
	}

	/**
	 * Creates a new flow execution holder for a flow execution that has been restored from a repository.
	 * @param flowExecutionKey the continuation key
	 * @param flowExecution the flow execution to hold
	 * @param flowExecutionLock the lock acquired on the flow execution
	 */
	public FlowExecutionHolder(FlowExecutionKey flowExecutionKey, FlowExecution flowExecution,
			FlowExecutionLock flowExecutionLock) {
		this.flowExecutionKey = flowExecutionKey;
		this.flowExecution = flowExecution;
		this.flowExecutionLock = flowExecutionLock;
	}

	/**
	 * Returns the continuation key.
	 */
	public FlowExecutionKey getFlowExecutionKey() {
		return flowExecutionKey;
	}

	/**
	 * Sets the continuation key.
	 */
	public void setFlowExecutionKey(FlowExecutionKey key) {
		this.flowExecutionKey = key;
	}

	/**
	 * Returns the flow execution.
	 */
	public FlowExecution getFlowExecution() {
		return flowExecution;
	}

	/**
	 * Returns the flow execution lock
	 */
	public FlowExecutionLock getFlowExecutionLock() {
		return flowExecutionLock;
	}

	/**
	 * Sets the lock acquired on the flow execution
	 * @param lock the flow execution lock
	 */
	public void setFlowExecutionLock(FlowExecutionLock lock) {
		this.flowExecutionLock = lock;
	}

	/**
	 * Returns the view selected from the current flow execution request.
	 */
	public ViewSelection getViewSelection() {
		return viewSelection;
	}

	/**
	 * Sets the selected view from the current flow execution request.
	 * @param viewSelection the view selection
	 */
	public void setViewSelection(ViewSelection viewSelection) {
		this.viewSelection = viewSelection;
	}

	/**
	 * Replace the current flow execution with the one provided. This method will clear out all state associated with
	 * the original execution and unlock it if necessary.
	 * @param flowExecution the new "current" flow execution
	 */
	public void replaceWith(FlowExecution flowExecution) {
		this.flowExecutionKey = null;
		this.viewSelection = null;
		unlockFlowExecutionIfNecessary();
		this.flowExecution = flowExecution;
	}

	/**
	 * Unlock the held flow execution if necessary.
	 */
	public void unlockFlowExecutionIfNecessary() {
		if (flowExecutionLock != null) {
			flowExecutionLock.unlock();
			this.flowExecutionLock = null;
		}
	}

	public String toString() {
		return new ToStringCreator(this).append("flowExecutionKey", flowExecutionKey).append("flowExecution",
				flowExecution).toString();
	}
}