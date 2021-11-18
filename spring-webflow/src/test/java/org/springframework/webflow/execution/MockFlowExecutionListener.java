/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution;

import org.springframework.util.Assert;
import org.springframework.webflow.core.collection.AttributeMap;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.definition.StateDefinition;

/**
 * Mock implementation of the <code>FlowExecutionListener</code> interface for use in unit tests.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public class MockFlowExecutionListener extends FlowExecutionListenerAdapter {

	private boolean sessionStarting;

	private boolean started;

	private boolean executing;

	private boolean paused;

	private int flowNestingLevel;

	private boolean requestInProcess;

	private int requestsSubmitted;

	private int requestsProcessed;

	private int eventsSignaled;

	private boolean stateEntering;

	private int stateTransitions;

	private boolean sessionEnding;

	private int exceptionsThrown;

	/**
	 * Is the flow execution running: it has started but not yet ended.
	 */
	public boolean isStarted() {
		return started;
	}

	/**
	 * Is the flow execution executing?
	 */
	public boolean isExecuting() {
		return executing;
	}

	/**
	 * Is the flow execution paused?
	 */
	public boolean isPaused() {
		return paused;
	}

	/**
	 * Returns the nesting level of the currently active flow in the flow execution. The root flow is at level 0, a sub
	 * flow of the root flow is at level 1, and so on.
	 */
	public int getFlowNestingLevel() {
		return flowNestingLevel;
	}

	/**
	 * Checks if a request is in process. A request is in process if it was submitted but has not yet completed
	 * processing.
	 */
	public boolean isRequestInProcess() {
		return requestInProcess;
	}

	/**
	 * Returns the number of requests submitted so far.
	 */
	public int getRequestsSubmittedCount() {
		return requestsSubmitted;
	}

	/**
	 * Returns the number of requests processed so far.
	 */
	public int getRequestsProcessedCount() {
		return requestsProcessed;
	}

	/**
	 * Returns the number of events signaled so far.
	 */
	public int getEventsSignaledCount() {
		return eventsSignaled;
	}

	/**
	 * Returns the number of state transitions executed so far.
	 */
	public int getTransitionCount() {
		return stateTransitions;
	}

	/**
	 * Returns the number of exceptions thrown.
	 */
	public int getExceptionsThrown() {
		return exceptionsThrown;
	}

	public void requestSubmitted(RequestContext context) {
		Assert.state(!requestInProcess, "There is already a request being processed");
		requestsSubmitted++;
		requestInProcess = true;
	}

	public void sessionStarting(RequestContext context, FlowDefinition definition, MutableAttributeMap input) {
		if (!context.getFlowExecutionContext().isActive()) {
			Assert.state(!started, "The flow execution was already started");
			flowNestingLevel = 0;
			eventsSignaled = 0;
			stateTransitions = 0;
		}
		sessionStarting = true;
	}

	public void sessionCreated(RequestContext context, FlowSession session) {
		Assert.state(sessionStarting, "The session should've been starting...");
		if (session.isRoot()) {
			Assert.state(!started, "The flow execution was already started");
			executing = true;
		} else {
			assertStarted();
			flowNestingLevel++;
		}
	}

	public void sessionStarted(RequestContext context, FlowSession session) {
		Assert.state(sessionStarting, "The session should've been starting...");
		sessionStarting = false;
		if (session.isRoot()) {
			Assert.state(!started, "The flow execution was already started");
			started = true;
		} else {
			assertStarted();
		}
	}

	public void requestProcessed(RequestContext context) {
		Assert.state(requestInProcess, "There is no request being processed");
		requestsProcessed++;
		requestInProcess = false;
	}

	public void eventSignaled(RequestContext context, Event event) {
		eventsSignaled++;
	}

	public void stateEntering(RequestContext context, StateDefinition state) throws EnterStateVetoException {
		stateEntering = true;
	}

	public void stateEntered(RequestContext context, StateDefinition newState, StateDefinition previousState) {
		Assert.state(stateEntering, "State should've entering...");
		stateEntering = false;
		stateTransitions++;
	}

	public void paused(RequestContext context, ViewSelection selectedView) {
		executing = false;
		paused = true;
	}

	public void resumed(RequestContext context) {
		executing = true;
		paused = false;
	}

	public void sessionEnding(RequestContext context, FlowSession session, MutableAttributeMap output) {
		sessionEnding = true;
	}

	public void sessionEnded(RequestContext context, FlowSession session, AttributeMap output) {
		assertStarted();
		Assert.state(sessionEnding, "Should have been ending");
		sessionEnding = false;
		if (session.isRoot()) {
			Assert.state(flowNestingLevel == 0, "The flow execution should have ended");
			started = false;
			executing = false;
		} else {
			flowNestingLevel--;
			Assert.state(started, "The flow execution prematurely ended");
		}
	}

	public void exceptionThrown(RequestContext context, FlowExecutionException exception) {
		exceptionsThrown++;
	}

	/**
	 * Make sure the flow execution has already been started.
	 */
	protected void assertStarted() {
		Assert.state(started, "The flow execution has not yet been started");
	}

	/**
	 * Reset all state collected by this listener.
	 */
	public void reset() {
		started = false;
		executing = false;
		requestsSubmitted = 0;
		requestsProcessed = 0;
		exceptionsThrown = 0;
	}
}