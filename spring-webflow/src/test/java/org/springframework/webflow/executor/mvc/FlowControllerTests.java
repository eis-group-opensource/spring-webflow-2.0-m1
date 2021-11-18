/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.executor.mvc;

import junit.framework.TestCase;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.webflow.conversation.impl.SessionBindingConversationManager;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistryImpl;
import org.springframework.webflow.definition.registry.StaticFlowDefinitionHolder;
import org.springframework.webflow.engine.SimpleFlow;
import org.springframework.webflow.engine.impl.FlowExecutionImplFactory;
import org.springframework.webflow.engine.impl.FlowExecutionImplStateRestorer;
import org.springframework.webflow.execution.repository.FlowExecutionRepository;
import org.springframework.webflow.execution.repository.support.SimpleFlowExecutionRepository;
import org.springframework.webflow.executor.FlowExecutorImpl;

/**
 * Unit tests for {@link FlowController}.
 */
public class FlowControllerTests extends TestCase {

	private FlowController controller = new FlowController();

	public void setUp() {
		controller.setServletContext(new MockServletContext());

		FlowDefinitionRegistryImpl registry = new FlowDefinitionRegistryImpl();
		registry.registerFlowDefinition(new StaticFlowDefinitionHolder(new SimpleFlow()));
		FlowExecutionImplFactory factory = new FlowExecutionImplFactory();
		FlowExecutionRepository repository = new SimpleFlowExecutionRepository(new FlowExecutionImplStateRestorer(
				registry), new SessionBindingConversationManager());
		controller.setFlowExecutor(new FlowExecutorImpl(registry, factory, repository));
	}

	public void testLaunch() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.addParameter("_flowId", "simpleFlow");
		ModelAndView mv = controller.handleRequestInternal(request, response);
		assertEquals("view", mv.getViewName());
	}

	public void testResume() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("POST");
		request.setContextPath("/app");
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.addParameter("_flowId", "simpleFlow");
		ModelAndView mv = controller.handleRequestInternal(request, response);
		request.addParameter("_flowExecutionKey", (String) mv.getModel().get("flowExecutionKey"));
		request.addParameter("_eventId", "submit");
		mv = controller.handleRequest(request, response);
		assertNull(mv.getViewName());
		assertTrue(mv.getView() instanceof RedirectView);
		RedirectView rv = (RedirectView) mv.getView();
		assertEquals("confirm", rv.getUrl());
		assertNull(mv.getModel().get("flowExecutionKey"));
	}
}