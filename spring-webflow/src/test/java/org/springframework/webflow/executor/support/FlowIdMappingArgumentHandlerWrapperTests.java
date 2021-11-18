/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.executor.support;

import java.util.Collections;
import java.util.Properties;

import junit.framework.TestCase;

import org.springframework.util.StringUtils;
import org.springframework.webflow.execution.support.FlowDefinitionRedirect;
import org.springframework.webflow.test.MockExternalContext;

/**
 * Test case for {@link FlowIdMappingArgumentHandlerWrapper}.
 * 
 * @author Erwin Vervaet
 */
public class FlowIdMappingArgumentHandlerWrapperTests extends TestCase {

	private FlowIdMappingArgumentHandlerWrapper argumentHandler;

	protected void setUp() throws Exception {
		this.argumentHandler = new FlowIdMappingArgumentHandlerWrapper();
		this.argumentHandler.setArgumentHandler(new RequestPathFlowExecutorArgumentHandler());
		Properties mappings = new Properties();
		mappings.setProperty("A", "X");
		mappings.setProperty("B", "Y");
		argumentHandler.setMappings(mappings);
		argumentHandler.addMapping("C", "X");
	}

	public void testMappingNoFallback() {
		argumentHandler.setFallback(false);

		assertTrue(argumentHandler.isFlowIdPresent(context("A")));
		assertEquals("X", argumentHandler.extractFlowId(context("A")));
		assertTrue(argumentHandler.isFlowIdPresent(context("B")));
		assertEquals("Y", argumentHandler.extractFlowId(context("B")));
		assertTrue(argumentHandler.isFlowIdPresent(context("C")));
		assertEquals("X", argumentHandler.extractFlowId(context("C")));
		assertFalse(argumentHandler.isFlowIdPresent(context("X")));
		assertFalse(argumentHandler.isFlowIdPresent(context("Y")));
		try {
			argumentHandler.extractFlowId(context("X"));
			fail();
		} catch (FlowExecutorArgumentExtractionException e) {
			// expected
		}
		try {
			argumentHandler.extractFlowId(context(""));
			fail();
		} catch (FlowExecutorArgumentExtractionException e) {
			// expected
		}
	}

	public void testMappingFallback() {
		argumentHandler.setFallback(true);

		assertTrue(argumentHandler.isFlowIdPresent(context("A")));
		assertEquals("X", argumentHandler.extractFlowId(context("A")));
		assertTrue(argumentHandler.isFlowIdPresent(context("B")));
		assertEquals("Y", argumentHandler.extractFlowId(context("B")));
		assertTrue(argumentHandler.isFlowIdPresent(context("C")));
		assertEquals("X", argumentHandler.extractFlowId(context("C")));
		assertTrue(argumentHandler.isFlowIdPresent(context("X")));
		assertEquals("X", argumentHandler.extractFlowId(context("X")));
		assertTrue(argumentHandler.isFlowIdPresent(context("Y")));
		assertEquals("Y", argumentHandler.extractFlowId(context("Y")));
		try {
			argumentHandler.extractFlowId(context(""));
			fail();
		} catch (FlowExecutorArgumentExtractionException e) {
			// expected
		}
	}

	public void testReverseMappingNoFallBack() {
		argumentHandler.setFallback(false);

		assertEquals("/app/flows/C", argumentHandler.createFlowDefinitionUrl(redirect("X"), context()));
		assertEquals("/app/flows/B", argumentHandler.createFlowDefinitionUrl(redirect("Y"), context()));

		try {
			argumentHandler.createFlowDefinitionUrl(redirect("Z"), context());
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	public void testReverseMappingFallback() {
		argumentHandler.setFallback(true);

		assertEquals("/app/flows/C", argumentHandler.createFlowDefinitionUrl(redirect("X"), context()));
		assertEquals("/app/flows/B", argumentHandler.createFlowDefinitionUrl(redirect("Y"), context()));
		assertEquals("/app/flows/Z", argumentHandler.createFlowDefinitionUrl(redirect("Z"), context()));
	}

	public void testWithRequestParameters() {
		argumentHandler.setArgumentHandler(new RequestParameterFlowExecutorArgumentHandler());

		// mapping
		assertTrue(argumentHandler.isFlowIdPresent(contextWithParam("A")));
		assertEquals("X", argumentHandler.extractFlowId(contextWithParam("A")));

		// reverse mapping
		assertEquals("/app/flows?_flowId=C", argumentHandler.createFlowDefinitionUrl(redirect("X"), context()));
	}

	// internal helpers

	private MockExternalContext context() {
		return context("");
	}

	private MockExternalContext context(String flowId) {
		MockExternalContext context = new MockExternalContext();
		context.setContextPath("/app");
		context.setDispatcherPath("/flows");
		if (StringUtils.hasText(flowId)) {
			context.setRequestPathInfo("/" + flowId);
		}
		return context;
	}

	private MockExternalContext contextWithParam(String flowId) {
		MockExternalContext context = context();
		context.putRequestParameter("_flowId", flowId);
		return context;
	}

	private FlowDefinitionRedirect redirect(String flowId) {
		return new FlowDefinitionRedirect(flowId, Collections.EMPTY_MAP);
	}
}
