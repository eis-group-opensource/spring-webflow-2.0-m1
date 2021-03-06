/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.executor;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.definition.registry.NoSuchFlowDefinitionException;
import org.springframework.webflow.engine.NoMatchingTransitionException;
import org.springframework.webflow.execution.repository.NoSuchFlowExecutionException;
import org.springframework.webflow.execution.support.ApplicationView;
import org.springframework.webflow.test.MockExternalContext;

public class FlowExecutorIntegrationTests extends AbstractDependencyInjectionSpringContextTests {

	private FlowExecutor flowExecutor;

	public void setFlowExecutor(FlowExecutor flowExecutor) {
		this.flowExecutor = flowExecutor;
	}

	protected String[] getConfigLocations() {
		return new String[] { "org/springframework/webflow/executor/context.xml",
				"org/springframework/webflow/executor/repository-simple.xml" };
	}

	public void testConfigurationOk() {
		assertNotNull(flowExecutor);
	}

	public void testLaunchFlow() {
		ExternalContext context = new ServletExternalContext(new MockServletContext(), new MockHttpServletRequest(),
				new MockHttpServletResponse());
		ResponseInstruction response = flowExecutor.launch("flow", context);
		assertTrue(response.getFlowExecutionContext().isActive());
		assertEquals("viewState1", response.getFlowExecutionContext().getActiveSession().getState().getId());
		assertTrue(response.isApplicationView());
		ApplicationView view = (ApplicationView) response.getViewSelection();
		assertEquals("view1", view.getViewName());
		assertEquals(0, view.getModel().size());
	}

	public void testLaunchNoSuchFlow() {
		try {
			ExternalContext context = new ServletExternalContext(new MockServletContext(),
					new MockHttpServletRequest(), new MockHttpServletResponse());
			flowExecutor.launch("bogus", context);
			fail("no such flow expected");
		} catch (NoSuchFlowDefinitionException e) {
			assertEquals("bogus", e.getFlowId());
		}
	}

	public void testLaunchAndSignalEvent() {
		ExternalContext context = new ServletExternalContext(new MockServletContext(), new MockHttpServletRequest(),
				new MockHttpServletResponse());
		ResponseInstruction response = flowExecutor.launch("flow", context);
		String key = response.getFlowExecutionKey();
		assertEquals("viewState1", response.getFlowExecutionContext().getActiveSession().getState().getId());
		response = flowExecutor.resume(key, "event1", context);
		assertTrue(response.getFlowExecutionContext().isActive());
		assertEquals("viewState2", response.getFlowExecutionContext().getActiveSession().getState().getId());
		assertTrue(response.isApplicationView());
		assertNotNull(response.getFlowExecutionKey());
		ApplicationView view = (ApplicationView) response.getViewSelection();
		assertEquals("view2", view.getViewName());
		assertEquals(0, view.getModel().size());
		response = flowExecutor.resume(response.getFlowExecutionKey(), "event1", context);
		view = (ApplicationView) response.getViewSelection();
		assertFalse(response.getFlowExecutionContext().isActive());
		assertTrue(response.isApplicationView());
		assertNull(response.getFlowExecutionKey());
		assertEquals("endView1", view.getViewName());
		assertEquals(0, view.getModel().size());
		try {
			flowExecutor.resume(key, "event1", context);
			fail("Should've been removed");
		} catch (NoSuchFlowExecutionException e) {

		}
	}

	public void testRefresh() {
		ExternalContext context = new ServletExternalContext(new MockServletContext(), new MockHttpServletRequest(),
				new MockHttpServletResponse());
		ResponseInstruction response = flowExecutor.launch("flow", context);
		ResponseInstruction response2 = flowExecutor.refresh(response.getFlowExecutionKey(), context);
		assertEquals(response, response2);
	}

	public void testNoSuchFlowExecution() {
		try {
			flowExecutor.resume("_cbogus_kbogus", "bogus", new MockExternalContext());
			fail("Should've failed");
		} catch (NoSuchFlowExecutionException e) {
			assertEquals("_cbogus_kbogus", e.getFlowExecutionKey().toString());
		}
	}

	public void testSignalEventNoMatchingTransition() {
		ExternalContext context = new ServletExternalContext(new MockServletContext(), new MockHttpServletRequest(),
				new MockHttpServletResponse());
		ResponseInstruction response = flowExecutor.launch("flow", context);
		String key = response.getFlowExecutionKey();
		try {
			flowExecutor.resume(key, "bogus", context);
			fail("Should've been removed");
		} catch (NoMatchingTransitionException e) {
			assertEquals("flow", e.getFlowId());
			assertEquals("viewState1", e.getStateId());
			assertEquals("bogus", e.getEvent().getId());
		}
	}
}