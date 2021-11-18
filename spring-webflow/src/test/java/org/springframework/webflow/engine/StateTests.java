/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine;

import junit.framework.TestCase;

import org.springframework.webflow.execution.FlowExecutionException;
import org.springframework.webflow.execution.TestAction;
import org.springframework.webflow.execution.ViewSelection;
import org.springframework.webflow.test.MockRequestControlContext;

/**
 * Tests that each of the Flow state types execute as expected when entered.
 * 
 * @author Keith Donald
 */
public class StateTests extends TestCase {

	private Flow flow;

	private State state;

	private boolean entered;

	public void setUp() {
		flow = new Flow("flow");
		state = new State(flow, "myState") {
			protected ViewSelection doEnter(RequestControlContext context) throws FlowExecutionException {
				entered = true;
				return ViewSelection.NULL_VIEW;
			}
		};
	}

	public void testStateEnter() {
		assertEquals("myState", state.getId());
		MockRequestControlContext context = new MockRequestControlContext(flow);
		state.enter(context);
		assertEquals(state, context.getCurrentState());
		assertTrue(entered);
	}

	public void testStateEnterWithEntryAction() {
		TestAction action = new TestAction();
		state.getEntryActionList().add(action);
		MockRequestControlContext context = new MockRequestControlContext(flow);
		state.enter(context);
		assertEquals(state, context.getCurrentState());
		assertTrue(action.isExecuted());
		assertTrue(entered);
		assertEquals(1, action.getExecutionCount());
	}
}
