/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.support;

import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.engine.builder.AbstractFlowBuilder;
import org.springframework.webflow.engine.builder.FlowAssembler;
import org.springframework.webflow.engine.builder.FlowBuilderException;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.ViewSelection;
import org.springframework.webflow.execution.support.ApplicationView;
import org.springframework.webflow.test.execution.AbstractFlowExecutionTests;

/**
 * Unit tests for {@link DefaultTargetStateResolver}.
 * 
 * @author Erwin Vervaet
 */
public class DefaultTargetStateResolverTests extends AbstractFlowExecutionTests {

	private boolean fail = false;

	protected FlowDefinition getFlowDefinition() {
		return new FlowAssembler("testFlow", new TestFlowBuilder()).assembleFlow();
	}

	public void testNonNullSourceState() {
		fail = false;
		ViewSelection viewSelection = startFlow();
		assertFlowExecutionActive();
		assertCurrentStateEquals("stateA");
		assertEquals("stateAView", ((ApplicationView) viewSelection).getViewName());
		viewSelection = signalEvent("aEvent");
		assertFlowExecutionActive();
		assertCurrentStateEquals("stateB");
		assertEquals("stateBView", ((ApplicationView) viewSelection).getViewName());
		viewSelection = signalEvent("bEvent");
		assertFlowExecutionEnded();
		assertTrue(viewSelection == ViewSelection.NULL_VIEW);
	}

	public void testNullSourceState() {
		fail = true;
		ViewSelection viewSelection = startFlow();
		assertFlowExecutionEnded();
		assertTrue(viewSelection == ViewSelection.NULL_VIEW);
	}

	private class TestFlowBuilder extends AbstractFlowBuilder {

		public void buildStartActions() throws FlowBuilderException {
			getFlow().getStartActionList().add(new Action() {
				public Event execute(RequestContext context) throws Exception {
					if (fail) {
						throw new UnsupportedOperationException();
					}
					return new Event(this, "success");
				}
			});
		}

		public void buildExceptionHandlers() throws FlowBuilderException {
			TransitionExecutingFlowExecutionExceptionHandler handler = new TransitionExecutingFlowExecutionExceptionHandler();
			handler.add(UnsupportedOperationException.class, "stateC");
			getFlow().getExceptionHandlerSet().add(handler);
		}

		public void buildStates() throws FlowBuilderException {
			addViewState("stateA", "stateAView", transition(on("aEvent"), to("stateB")));
			addViewState("stateB", "stateBView", transition(on("bEvent"), to("stateC")));
			addEndState("stateC");
		}
	}
}
