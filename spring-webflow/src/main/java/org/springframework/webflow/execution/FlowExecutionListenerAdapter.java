/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution;

import org.springframework.webflow.core.collection.AttributeMap;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.definition.StateDefinition;

/**
 * An abstract adapter class for listeners (observers) of flow execution lifecycle events. The methods in this class are
 * empty. This class exists as convenience for creating listener objects; subclass it and override what you need.
 * 
 * @author Erwin Vervaet
 * @author Keith Donald
 */
public abstract class FlowExecutionListenerAdapter implements FlowExecutionListener {

	public void requestSubmitted(RequestContext context) {
	}

	public void requestProcessed(RequestContext context) {
	}

	public void sessionStarting(RequestContext context, FlowDefinition definition, MutableAttributeMap input) {
	}

	public void sessionCreated(RequestContext context, FlowSession session) {
	}

	public void sessionStarted(RequestContext context, FlowSession session) {
	}

	public void eventSignaled(RequestContext context, Event event) {
	}

	public void stateEntering(RequestContext context, StateDefinition state) throws EnterStateVetoException {
	}

	public void stateEntered(RequestContext context, StateDefinition previousState, StateDefinition newState) {
	}

	public void resumed(RequestContext context) {
	}

	public void paused(RequestContext context, ViewSelection selectedView) {
	}

	public void sessionEnding(RequestContext context, FlowSession session, MutableAttributeMap output) {
	}

	public void sessionEnded(RequestContext context, FlowSession session, AttributeMap output) {
	}

	public void exceptionThrown(RequestContext context, FlowExecutionException exception) {
	}

}