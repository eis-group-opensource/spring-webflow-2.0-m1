/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.factory;

import org.springframework.util.Assert;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.execution.FlowExecutionListener;

/**
 * A simple flow execution listener loader that simply returns a static listener array on each invocation. For more
 * elaborate needs see the {@link ConditionalFlowExecutionListenerLoader}.
 * 
 * @see ConditionalFlowExecutionListenerLoader
 * 
 * @author Keith Donald
 */
public final class StaticFlowExecutionListenerLoader implements FlowExecutionListenerLoader {

	/**
	 * A shared listener loader instance that returns am empty listener array on each invocation.
	 */
	public static final FlowExecutionListenerLoader EMPTY_INSTANCE = new StaticFlowExecutionListenerLoader();

	/**
	 * The listener array to return when {@link #getListeners(FlowDefinition)} is invoked.
	 */
	private final FlowExecutionListener[] listeners;

	/**
	 * Creates a new flow execution listener loader that returns an empty listener array on each invocation.
	 */
	private StaticFlowExecutionListenerLoader() {
		this(new FlowExecutionListener[0]);
	}

	/**
	 * Creates a new flow execution listener loader that returns the provided listener on each invocation.
	 * @param listener the listener
	 */
	public StaticFlowExecutionListenerLoader(FlowExecutionListener listener) {
		this(new FlowExecutionListener[] { listener });
	}

	/**
	 * Creates a new flow execution listener loader that returns the provided listener array on each invocation. Clients
	 * should not attempt to modify the passed in array as no deep copy is made.
	 * @param listeners the listener array.
	 */
	public StaticFlowExecutionListenerLoader(FlowExecutionListener[] listeners) {
		Assert.notNull(listeners, "The flow execution listener array is required");
		this.listeners = listeners;
	}

	public FlowExecutionListener[] getListeners(FlowDefinition flowDefinition) {
		return listeners;
	}
}