/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.faces.webflow;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import junit.framework.TestCase;

import org.springframework.faces.webflow.FlowExecutionHolder;
import org.springframework.faces.webflow.FlowSystemCleanupFilter;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.webflow.context.ExternalContextHolder;
import org.springframework.webflow.engine.impl.FlowExecutionImpl;
import org.springframework.webflow.execution.FlowExecutionContextHolder;
import org.springframework.webflow.test.MockExternalContext;

public class FlowSystemCleanupFilterTests extends TestCase {

	private FlowSystemCleanupFilter filter;

	private ServletRequest request;

	private ServletResponse response;

	private FilterChain chain;

	protected void setUp() throws Exception {
		filter = new FlowSystemCleanupFilter();
		request = new MockHttpServletRequest();
		request.setAttribute(getFlowExecutionHolderKey(), new FlowExecutionHolder(new FlowExecutionImpl()));
		response = new MockHttpServletResponse();
		chain = new MockFilterChain();
		FlowExecutionContextHolder.setFlowExecutionContext(new FlowExecutionImpl());
		ExternalContextHolder.setExternalContext(new MockExternalContext());
	}

	public void testCleanup() throws ServletException, IOException {
		filter.doFilter(request, response, chain);

		assertNull("Should have cleaned up the flow execution", request.getAttribute(getFlowExecutionHolderKey()));
		try {
			FlowExecutionContextHolder.getFlowExecutionContext();
			fail("Should have an empty holder");
		} catch (IllegalStateException e) {
		}
		try {
			ExternalContextHolder.getExternalContext();
			fail("Should have an empty holder");
		} catch (IllegalStateException e) {
		}
	}

	public void testExceptionThrown() throws ServletException, IOException {
		try {
			filter.doFilter(request, response, new ExceptionThrowingMockFilterChain());
		} catch (RuntimeException e) {
			assertNull("Should have cleaned up the flow execution", request.getAttribute(getFlowExecutionHolderKey()));
			try {
				FlowExecutionContextHolder.getFlowExecutionContext();
				fail("Should have an empty holder");
			} catch (IllegalStateException e1) {
			}
			try {
				ExternalContextHolder.getExternalContext();
				fail("Should have an empty holder");
			} catch (IllegalStateException e1) {
			}
		}
	}

	private static String getFlowExecutionHolderKey() {
		return FlowExecutionHolder.class.getName();
	}

	private class ExceptionThrowingMockFilterChain extends MockFilterChain {

		public void doFilter(ServletRequest request, ServletResponse response) {
			throw new RuntimeException();
		}

	}
}
