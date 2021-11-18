/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine;

import junit.framework.TestCase;

import org.springframework.binding.expression.support.StaticExpression;
import org.springframework.webflow.engine.impl.FlowExecutionImpl;
import org.springframework.webflow.engine.support.ApplicationViewSelector;
import org.springframework.webflow.engine.support.DefaultTargetStateResolver;
import org.springframework.webflow.engine.support.EventIdTransitionCriteria;
import org.springframework.webflow.execution.FlowExecution;
import org.springframework.webflow.execution.TestAction;
import org.springframework.webflow.execution.ViewSelection;
import org.springframework.webflow.execution.support.ApplicationView;
import org.springframework.webflow.test.MockExternalContext;

/**
 * Tests that each of the Flow state types execute as expected when entered.
 * 
 * @author Keith Donald
 */
public class ViewStateTests extends TestCase {

	public void testViewState() {
		Flow flow = new Flow("myFlow");
		ViewState state = new ViewState(flow, "viewState");
		state.setViewSelector(view("myViewName"));
		state.getTransitionSet().add(new Transition(on("submit"), to("finish")));
		new EndState(flow, "finish");
		FlowExecution flowExecution = new FlowExecutionImpl(flow);
		ApplicationView view = (ApplicationView) flowExecution.start(null, new MockExternalContext());
		assertEquals("viewState", flowExecution.getActiveSession().getState().getId());
		assertNotNull(view);
		assertEquals("myViewName", view.getViewName());
	}

	public void testViewStateMarker() {
		Flow flow = new Flow("myFlow");
		ViewState state = new ViewState(flow, "viewState");
		state.getTransitionSet().add(new Transition(on("submit"), to("finish")));
		new EndState(flow, "finish");
		FlowExecution flowExecution = new FlowExecutionImpl(flow);
		ViewSelection view = flowExecution.start(null, new MockExternalContext());
		assertEquals("viewState", flowExecution.getActiveSession().getState().getId());
		assertEquals(ViewSelection.NULL_VIEW, view);
	}

	public void testViewStateNotRenderableSelection() {
		Flow flow = new Flow("myFlow");
		ViewState state = new ViewState(flow, "viewState");
		state.setViewSelector(new ApplicationViewSelector(new StaticExpression("myView"), true));
		TestAction action = new TestAction();
		state.getRenderActionList().add(action);
		state.getTransitionSet().add(new Transition(on("submit"), to("finish")));
		new EndState(flow, "finish");
		FlowExecution flowExecution = new FlowExecutionImpl(flow);
		assertFalse(action.isExecuted());

		flowExecution.start(null, new MockExternalContext());
		assertEquals("viewState", flowExecution.getActiveSession().getState().getId());
		assertFalse(action.isExecuted());
		assertEquals(action.getExecutionCount(), 0);

		flowExecution.refresh(new MockExternalContext());
		assertEquals(action.getExecutionCount(), 1);
	}

	public void testViewStateRenderableSelection() {
		Flow flow = new Flow("myFlow");
		ViewState state = new ViewState(flow, "viewState");
		state.setViewSelector(new ApplicationViewSelector(new StaticExpression("test")));
		TestAction action = new TestAction();
		state.getRenderActionList().add(action);
		state.getTransitionSet().add(new Transition(on("submit"), to("finish")));
		new EndState(flow, "finish");
		FlowExecution flowExecution = new FlowExecutionImpl(flow);
		assertFalse(action.isExecuted());

		flowExecution.start(null, new MockExternalContext());
		assertEquals("viewState", flowExecution.getActiveSession().getState().getId());
		assertTrue(action.isExecuted());
		assertEquals(action.getExecutionCount(), 1);

		flowExecution.refresh(new MockExternalContext());
		assertEquals(action.getExecutionCount(), 2);

	}

	protected TransitionCriteria on(String event) {
		return new EventIdTransitionCriteria(event);
	}

	protected TargetStateResolver to(String stateId) {
		return new DefaultTargetStateResolver(stateId);
	}

	public static ViewSelector view(String viewName) {
		return new ApplicationViewSelector(new StaticExpression(viewName));
	}
}