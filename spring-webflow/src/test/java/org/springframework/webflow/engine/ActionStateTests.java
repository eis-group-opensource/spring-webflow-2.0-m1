/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine;

import junit.framework.TestCase;

import org.springframework.webflow.engine.impl.FlowExecutionImpl;
import org.springframework.webflow.engine.support.DefaultTargetStateResolver;
import org.springframework.webflow.engine.support.EventIdTransitionCriteria;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.FlowExecution;
import org.springframework.webflow.execution.TestAction;
import org.springframework.webflow.test.MockExternalContext;

/**
 * Tests that each of the Flow state types execute as expected when entered.
 * 
 * @author Keith Donald
 */
public class ActionStateTests extends TestCase {

	public void testActionStateSingleAction() {
		Flow flow = new Flow("myFlow");
		ActionState state = new ActionState(flow, "actionState");
		state.getActionList().add(new TestAction());
		state.getTransitionSet().add(new Transition(on("success"), to("finish")));
		new EndState(flow, "finish");
		FlowExecution flowExecution = new FlowExecutionImpl(flow);
		flowExecution.start(null, new MockExternalContext());
		assertEquals(1, ((TestAction) state.getActionList().get(0)).getExecutionCount());
	}

	public void testActionAttributesChain() {
		Flow flow = new Flow("myFlow");
		ActionState state = new ActionState(flow, "actionState");
		state.getActionList().add(new TestAction("not mapped result"));
		state.getActionList().add(new TestAction(null));
		state.getActionList().add(new TestAction(""));
		state.getActionList().add(new TestAction("success"));
		state.getTransitionSet().add(new Transition(on("success"), to("finish")));
		new EndState(flow, "finish");
		FlowExecution flowExecution = new FlowExecutionImpl(flow);
		flowExecution.start(null, new MockExternalContext());
		Action[] actions = state.getActionList().toArray();
		for (int i = 0; i < actions.length; i++) {
			TestAction action = (TestAction) actions[i];
			assertEquals(1, action.getExecutionCount());
		}
	}

	public void testActionAttributesChainNoMatchingTransition() {
		Flow flow = new Flow("myFlow");
		ActionState state = new ActionState(flow, "actionState");
		state.getActionList().add(new TestAction("not mapped result"));
		state.getActionList().add(new TestAction(null));
		state.getActionList().add(new TestAction(""));
		state.getActionList().add(new TestAction("yet another not mapped result"));
		state.getTransitionSet().add(new Transition(on("success"), to("finish")));
		new EndState(flow, "finish");
		FlowExecution flowExecution = new FlowExecutionImpl(flow);
		try {
			flowExecution.start(null, new MockExternalContext());
			fail("Should not have matched to another state transition");
		} catch (NoMatchingTransitionException e) {
			// expected
		}
	}

	public void testActionAttributesChainNamedActions() {
		Flow flow = new Flow("myFlow");
		ActionState state = new ActionState(flow, "actionState");
		state.getActionList().add(new AnnotatedAction(new TestAction("not mapped result")));
		state.getActionList().add(new AnnotatedAction(new TestAction(null)));
		AnnotatedAction action3 = new AnnotatedAction(new TestAction(""));
		action3.setName("action3");
		state.getActionList().add(action3);
		AnnotatedAction action4 = new AnnotatedAction(new TestAction("success"));
		action4.setName("action4");
		state.getActionList().add(action4);
		state.getTransitionSet().add(new Transition(on("action4.success"), to("finish")));
		new EndState(flow, "finish");
		FlowExecution flowExecution = new FlowExecutionImpl(flow);
		flowExecution.start(null, new MockExternalContext());
		assertTrue(!flowExecution.isActive());
		Action[] actions = state.getActionList().toArray();
		for (int i = 0; i < actions.length; i++) {
			AnnotatedAction action = (AnnotatedAction) actions[i];
			assertEquals(1, ((TestAction) (action.getTargetAction())).getExecutionCount());
		}
	}

	protected TransitionCriteria on(String event) {
		return new EventIdTransitionCriteria(event);
	}

	protected TargetStateResolver to(String stateId) {
		return new DefaultTargetStateResolver(stateId);
	}
}
