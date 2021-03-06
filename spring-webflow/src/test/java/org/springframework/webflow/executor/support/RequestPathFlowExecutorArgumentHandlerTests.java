/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.executor.support;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.springframework.webflow.execution.FlowExecutionContext;
import org.springframework.webflow.execution.support.FlowDefinitionRedirect;
import org.springframework.webflow.test.MockExternalContext;
import org.springframework.webflow.test.MockFlowExecutionContext;

/**
 * Unit tests for {@link RequestPathFlowExecutorArgumentHandler}.
 */
public class RequestPathFlowExecutorArgumentHandlerTests extends TestCase {

	private MockExternalContext context = new MockExternalContext();

	private RequestPathFlowExecutorArgumentHandler argumentHandler;

	private String flowExecutionKey;

	public void setUp() {
		argumentHandler = new RequestPathFlowExecutorArgumentHandler();
		flowExecutionKey = "_c12345_k12345";
	}

	public void testExtractFlowId() {
		MockExternalContext context = new MockExternalContext();
		context.setRequestPathInfo("flow");
		assertEquals("flow", argumentHandler.extractFlowId(context));
	}

	public void testExtractFlowIdDefault() {
		argumentHandler.setDefaultFlowId("flow");
		assertEquals("flow", argumentHandler.extractFlowId(new MockExternalContext()));
	}

	public void testExtractFlowIdNoRequestPath() {
		try {
			argumentHandler.extractFlowId(new MockExternalContext());
			fail("should've failed");
		} catch (FlowExecutorArgumentExtractionException e) {
		}
	}

	public void testCreateFlowUrl() {
		context.setContextPath("/app");
		context.setDispatcherPath("/flows");
		FlowDefinitionRedirect redirect = new FlowDefinitionRedirect("flow", null);
		String url = argumentHandler.createFlowDefinitionUrl(redirect, context);
		assertEquals("/app/flows/flow", url);
	}

	public void testCreateFlowUrlInput() {
		context.setContextPath("/app");
		context.setDispatcherPath("/flows");
		Map input = new HashMap();
		input.put("foo", "bar");
		input.put("baz", new Integer(3));
		FlowDefinitionRedirect redirect = new FlowDefinitionRedirect("flow", input);
		String url = argumentHandler.createFlowDefinitionUrl(redirect, context);
		assertTrue("/app/flows/flow?foo=bar&baz=3".equals(url) || "/app/flows/flow?baz=3&foo=bar".equals(url));
	}

	public void testCreateFlowExecutionUrl() {
		context.setContextPath("/app");
		context.setDispatcherPath("/flows");
		FlowExecutionContext flowExecution = new MockFlowExecutionContext();
		String url = argumentHandler.createFlowExecutionUrl(flowExecutionKey, flowExecution, context);
		assertEquals("/app/flows/k/_c12345_k12345", url);
	}

	public void testIsFlowExecutionKeyPresent() {
		context.setContextPath("/app");
		context.setDispatcherPath("/flows");
		context.setRequestPathInfo("/k/_c12345_k12345");
		assertTrue(argumentHandler.isFlowExecutionKeyPresent(context));
		context.setRequestPathInfo("/sellitem");
		assertFalse(argumentHandler.isFlowExecutionKeyPresent(context));
	}

	public void testExtractFlowExecutionKey() {
		context.setContextPath("/app");
		context.setDispatcherPath("/flows");
		context.setRequestPathInfo("/k/_c12345_k12345");
		assertEquals("_c12345_k12345", argumentHandler.extractFlowExecutionKey(context));
	}
}