/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine;

import junit.framework.TestCase;

import org.springframework.webflow.engine.support.DefaultTargetStateResolver;
import org.springframework.webflow.engine.support.EventIdTransitionCriteria;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.test.MockRequestControlContext;

/**
 * Tests that each of the Flow state types execute as expected when entered.
 * 
 * @author Keith Donald
 */
public class DecisionStateTests extends TestCase {

	public void testIfDecision() {
		Flow flow = new Flow("flow");
		DecisionState state = new DecisionState(flow, "decisionState");
		state.getTransitionSet().add(new Transition(new EventIdTransitionCriteria("foo"), to("target")));
		new EndState(flow, "target");
		MockRequestControlContext context = new MockRequestControlContext(flow);
		context.setLastEvent(new Event(this, "foo"));
		state.enter(context);
		assertFalse(context.getFlowExecutionContext().isActive());
	}

	public void testElseDecision() {
		Flow flow = new Flow("flow");
		DecisionState state = new DecisionState(flow, "decisionState");
		state.getTransitionSet().add(new Transition(new EventIdTransitionCriteria("foo"), to("invalid")));
		state.getTransitionSet().add(new Transition(to("target")));
		new EndState(flow, "target");
		MockRequestControlContext context = new MockRequestControlContext(flow);
		context.setLastEvent(new Event(this, "bogus"));
		state.enter(context);
		assertFalse(context.getFlowExecutionContext().isActive());
	}

	public void testNoMatching() {
		Flow flow = new Flow("flow");
		DecisionState state = new DecisionState(flow, "decisionState");
		state.getTransitionSet().add(new Transition(new EventIdTransitionCriteria("foo"), to("invalid")));
		state.getTransitionSet().add(new Transition(new EventIdTransitionCriteria("bar"), to("invalid")));
		MockRequestControlContext context = new MockRequestControlContext(flow);
		context.setLastEvent(new Event(this, "bogus"));
		try {
			state.enter(context);
			fail("Expected no matching");
		} catch (NoMatchingTransitionException e) {

		}
	}

	protected TargetStateResolver to(String stateId) {
		return new DefaultTargetStateResolver(stateId);
	}
}
