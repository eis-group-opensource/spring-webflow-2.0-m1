/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.test;

import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.engine.RequestControlContext;
import org.springframework.webflow.engine.State;
import org.springframework.webflow.engine.Transition;
import org.springframework.webflow.engine.TransitionableState;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.FlowSession;
import org.springframework.webflow.execution.FlowSessionStatus;
import org.springframework.webflow.execution.ViewSelection;

/**
 * Mock implementation of the {@link RequestControlContext} interface to facilitate standalone Flow and State unit
 * tests.
 * 
 * @see org.springframework.webflow.execution.RequestContext
 * @see org.springframework.webflow.execution.FlowSession
 * @see org.springframework.webflow.engine.State
 * 
 * @author Keith Donald
 */
public class MockRequestControlContext extends MockRequestContext implements RequestControlContext {

	/**
	 * Creates a new mock request control context for controlling a mock execution of the provided flow definition.
	 */
	public MockRequestControlContext(Flow rootFlow) {
		super(rootFlow);
	}

	// implementing RequestControlContext

	public void setCurrentState(State state) {
		State previousState = (State) getCurrentState();
		getMockFlowExecutionContext().getMockActiveSession().setState(state);
		if (previousState == null) {
			getMockFlowExecutionContext().getMockActiveSession().setStatus(FlowSessionStatus.ACTIVE);
		}
	}

	public ViewSelection start(Flow flow, MutableAttributeMap input) throws IllegalStateException {
		getMockFlowExecutionContext().setActiveSession(new MockFlowSession(flow, input));
		getMockFlowExecutionContext().getMockActiveSession().setStatus(FlowSessionStatus.STARTING);
		ViewSelection selectedView = flow.start(this, input);
		return selectedView;
	}

	public ViewSelection signalEvent(Event event) {
		setLastEvent(event);
		ViewSelection selectedView = ((Flow) getActiveFlow()).onEvent(this);
		return selectedView;
	}

	public FlowSession endActiveFlowSession(MutableAttributeMap output) throws IllegalStateException {
		MockFlowSession endingSession = getMockFlowExecutionContext().getMockActiveSession();
		endingSession.getDefinitionInternal().end(this, output);
		endingSession.setStatus(FlowSessionStatus.ENDED);
		getMockFlowExecutionContext().setActiveSession(null);
		return endingSession;
	}

	public ViewSelection execute(Transition transition) {
		return transition.execute((TransitionableState) getCurrentState(), this);
	}
}