/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution;

import org.springframework.util.Assert;

/**
 * Simple holder class that associates a {@link FlowExecutionContext} instance with the current thread. The
 * FlowExecutionContext will not be inherited by any child threads spawned by the current thread.
 * <p>
 * Used as a central holder for the current FlowExecutionContext in Spring Web Flow, wherever necessary. Often used by
 * artifacts needing to access the current active flow execution.
 * 
 * @see FlowExecutionContext
 * 
 * @author Ben Hale
 * @since 1.1
 */
public class FlowExecutionContextHolder {

	private static final ThreadLocal flowExecutionContextHolder = new ThreadLocal();

	/**
	 * Associate the given FlowExecutionContext with the current thread.
	 * @param flowExecutionContext the current FlowExecutionContext, or <code>null</code> to reset the thread-bound
	 * context
	 */
	public static void setFlowExecutionContext(FlowExecutionContext flowExecutionContext) {
		flowExecutionContextHolder.set(flowExecutionContext);
	}

	/**
	 * Return the FlowExecutionContext associated with the current thread, if any.
	 * @return the current FlowExecutionContext
	 * @throws IllegalStateException if no FlowExecutionContext is bound to this thread
	 */
	public static FlowExecutionContext getFlowExecutionContext() throws IllegalStateException {
		Assert.state(flowExecutionContextHolder.get() != null, "No flow execution context is bound to this thread");
		return (FlowExecutionContext) flowExecutionContextHolder.get();
	}

	// not instantiable
	private FlowExecutionContextHolder() {
	}
}
