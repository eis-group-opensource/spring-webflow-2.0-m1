/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.executor.support;

import junit.framework.TestCase;

import org.springframework.webflow.conversation.impl.SessionBindingConversationManager;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistryImpl;
import org.springframework.webflow.definition.registry.StaticFlowDefinitionHolder;
import org.springframework.webflow.engine.EndState;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.engine.TargetStateResolver;
import org.springframework.webflow.engine.Transition;
import org.springframework.webflow.engine.ViewState;
import org.springframework.webflow.engine.impl.FlowExecutionImplFactory;
import org.springframework.webflow.engine.impl.FlowExecutionImplStateRestorer;
import org.springframework.webflow.engine.support.DefaultTargetStateResolver;
import org.springframework.webflow.execution.repository.support.SimpleFlowExecutionRepository;
import org.springframework.webflow.executor.FlowExecutorImpl;
import org.springframework.webflow.executor.ResponseInstruction;
import org.springframework.webflow.test.MockExternalContext;

/**
 * Unit tests for {@link FlowRequestHandler}.
 */
public class FlowRequestHandlerTests extends TestCase {

	private FlowRequestHandler handler;

	private MockExternalContext context = new MockExternalContext();

	protected void setUp() throws Exception {
		FlowDefinitionRegistryImpl registry = new FlowDefinitionRegistryImpl();
		Flow flow = new Flow("flow");
		ViewState view = new ViewState(flow, "view");
		view.getTransitionSet().add(new Transition(to("end")));
		new EndState(flow, "end");
		registry.registerFlowDefinition(new StaticFlowDefinitionHolder(flow));
		FlowExecutorImpl executor = new FlowExecutorImpl(registry, new FlowExecutionImplFactory(),
				new SimpleFlowExecutionRepository(new FlowExecutionImplStateRestorer(registry),
						new SessionBindingConversationManager()));
		handler = new FlowRequestHandler(executor);
	}

	public void testLaunch() {
		context.putRequestParameter("_flowId", "flow");
		ResponseInstruction response = handler.handleFlowRequest(context);
		assertTrue(response.isNull());
		assertTrue(response.getFlowExecutionContext().isActive());
		assertEquals("flow", response.getFlowExecutionContext().getDefinition().getId());
		assertEquals("view", response.getFlowExecutionContext().getActiveSession().getState().getId());
	}

	public void testResumeOnEvent() {
		context.putRequestParameter("_flowId", "flow");
		ResponseInstruction response = handler.handleFlowRequest(context);

		String flowExecutionKey = response.getFlowExecutionKey();
		context.putRequestParameter("_flowExecutionKey", flowExecutionKey);
		context.putRequestParameter("_eventId", "submit");
		response = handler.handleFlowRequest(context);

		assertTrue(response.isNull());
		assertTrue(!response.getFlowExecutionContext().isActive());
		assertEquals("flow", response.getFlowExecutionContext().getDefinition().getId());

	}

	public void testRefreshFlowExecution() {
		context.putRequestParameter("_flowId", "flow");
		ResponseInstruction response = handler.handleFlowRequest(context);

		String flowExecutionKey = response.getFlowExecutionKey();
		context.putRequestParameter("_flowExecutionKey", flowExecutionKey);
		response = handler.handleFlowRequest(context);

		assertTrue(response.isNull());
		assertTrue(response.getFlowExecutionContext().isActive());
		assertEquals("flow", response.getFlowExecutionContext().getDefinition().getId());
		assertEquals("view", response.getFlowExecutionContext().getActiveSession().getState().getId());
	}

	protected TargetStateResolver to(String stateId) {
		return new DefaultTargetStateResolver(stateId);
	}
}