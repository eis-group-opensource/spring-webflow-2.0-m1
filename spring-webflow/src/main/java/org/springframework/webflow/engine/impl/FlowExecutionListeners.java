/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.impl;

import org.springframework.webflow.core.collection.AttributeMap;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.definition.StateDefinition;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.FlowExecutionException;
import org.springframework.webflow.execution.FlowExecutionListener;
import org.springframework.webflow.execution.FlowSession;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.ViewSelection;

/**
 * A helper that aids in publishing events to an array of <code>FlowExecutionListener</code> objects.
 * 
 * @see org.springframework.webflow.execution.FlowExecutionListener
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
class FlowExecutionListeners {

	/**
	 * The list of listeners that should receive event callbacks during managed flow executions.
	 */
	private FlowExecutionListener[] listeners;

	/**
	 * Create a flow execution listener helper that wraps an empty listener array.
	 */
	public FlowExecutionListeners() {
		this(null);
	}

	/**
	 * Create a flow execution listener helper that wraps the specified listener array.
	 * @param listeners the listener array
	 */
	public FlowExecutionListeners(FlowExecutionListener[] listeners) {
		if (listeners != null) {
			this.listeners = listeners;
		} else {
			this.listeners = new FlowExecutionListener[0];
		}
	}

	/**
	 * Returns the wrapped listener array.
	 * @return the listener array
	 */
	public FlowExecutionListener[] getArray() {
		return listeners;
	}

	/**
	 * Returns the size of the listener array.
	 */
	public int size() {
		return listeners.length;
	}

	// methods to fire events to all listeners

	/**
	 * Notify all interested listeners that a request was submitted to the flow execution.
	 */
	public void fireRequestSubmitted(RequestContext context) {
		for (int i = 0; i < listeners.length; i++) {
			listeners[i].requestSubmitted(context);
		}
	}

	/**
	 * Notify all interested listeners that the flow execution finished processing a request.
	 */
	public void fireRequestProcessed(RequestContext context) {
		for (int i = 0; i < listeners.length; i++) {
			listeners[i].requestProcessed(context);
		}
	}

	/**
	 * Notify all interested listeners that a flow execution session is starting (about to be created).
	 */
	public void fireSessionStarting(RequestContext context, FlowDefinition flow, MutableAttributeMap input) {
		for (int i = 0; i < listeners.length; i++) {
			listeners[i].sessionStarting(context, flow, input);
		}
	}

	/**
	 * Notify all interested listeners that a flow execution session has been activated (created, on the stack and about
	 * to start).
	 */
	public void fireSessionCreated(RequestContext context, FlowSession session) {
		for (int i = 0; i < listeners.length; i++) {
			listeners[i].sessionCreated(context, session);
		}
	}

	/**
	 * Notify all interested listeners that a flow execution session has started (has entered its start state).
	 */
	public void fireSessionStarted(RequestContext context, FlowSession session) {
		for (int i = 0; i < listeners.length; i++) {
			listeners[i].sessionStarted(context, session);
		}
	}

	/**
	 * Notify all interested listeners that an event was signaled in the flow execution.
	 */
	public void fireEventSignaled(RequestContext context, Event event) {
		for (int i = 0; i < listeners.length; i++) {
			listeners[i].eventSignaled(context, event);
		}
	}

	/**
	 * Notify all interested listeners that a state is being entered in the flow execution.
	 */
	public void fireStateEntering(RequestContext context, StateDefinition nextState) {
		for (int i = 0; i < listeners.length; i++) {
			listeners[i].stateEntering(context, nextState);
		}
	}

	/**
	 * Notify all interested listeners that a state was entered in the flow execution.
	 */
	public void fireStateEntered(RequestContext context, StateDefinition previousState) {
		for (int i = 0; i < listeners.length; i++) {
			listeners[i].stateEntered(context, previousState, context.getCurrentState());
		}
	}

	/**
	 * Notify all interested listeners that a flow session was paused in the flow execution.
	 */
	public void firePaused(RequestContext context, ViewSelection selectedView) {
		for (int i = 0; i < listeners.length; i++) {
			listeners[i].paused(context, selectedView);
		}
	}

	/**
	 * Notify all interested listeners that the flow execution was resumed.
	 */
	public void fireResumed(RequestContext context) {
		for (int i = 0; i < listeners.length; i++) {
			listeners[i].resumed(context);
		}
	}

	/**
	 * Notify all interested listeners that the active flow execution session is ending.
	 */
	public void fireSessionEnding(RequestContext context, FlowSession session, MutableAttributeMap output) {
		for (int i = 0; i < listeners.length; i++) {
			listeners[i].sessionEnding(context, session, output);
		}
	}

	/**
	 * Notify all interested listeners that a flow execution session has ended.
	 */
	public void fireSessionEnded(RequestContext context, FlowSession session, AttributeMap output) {
		for (int i = 0; i < listeners.length; i++) {
			listeners[i].sessionEnded(context, session, output);
		}
	}

	/**
	 * Notify all interested listeners that a flow execution threw an exception.
	 */
	public void fireExceptionThrown(RequestContext context, FlowExecutionException exception) {
		for (int i = 0; i < listeners.length; i++) {
			listeners[i].exceptionThrown(context, exception);
		}
	}
}