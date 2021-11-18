/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.faces.webflow;

import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.VariableResolver;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.springframework.faces.webflow.FlowExecutionHolder;
import org.springframework.faces.webflow.FlowExecutionHolderUtils;
import org.springframework.faces.webflow.el.FlowVariableResolver;
import org.springframework.webflow.execution.FlowExecution;
import org.springframework.webflow.execution.repository.FlowExecutionKey;

/**
 * Unit tests for the FlowVariableResolver class.
 * 
 * @author Ulrik Sandberg
 */
public class FlowVariableResolverTests extends TestCase {

	private FlowVariableResolver tested;

	private TestableVariableResolver variableResolver;

	private MockFacesContext mockFacesContext;

	private MockJsfExternalContext mockJsfExternalContext;

	protected void setUp() throws Exception {
		super.setUp();
		mockFacesContext = new MockFacesContext();
		mockJsfExternalContext = new MockJsfExternalContext();
		mockFacesContext.setExternalContext(mockJsfExternalContext);
		variableResolver = new TestableVariableResolver();
		tested = new FlowVariableResolver(variableResolver);
	}

	public void testResolveVariableNotFlowScope() {
		Object result = tested.resolveVariable(mockFacesContext, "some name");
		assertTrue("not resolved using delegate", variableResolver.resolvedUsingDelegate);
		assertSame(variableResolver.expected, result);
	}

	public void testResolveVariableFlowScopeWithNoThreadLocal() {
		try {
			tested.resolveVariable(mockFacesContext, "flowScope");
			fail("EvaluationException expected");
		} catch (EvaluationException expected) {

		}
		assertFalse("resolved using delegate", variableResolver.resolvedUsingDelegate);
	}

	public void testResolveVariableFlowScopeWithThreadLocal() {
		FlowExecution flowExecutionMock = (FlowExecution) EasyMock.createMock(FlowExecution.class);
		FlowExecutionKey key = null;
		FlowExecutionHolder holder = new FlowExecutionHolder(key, flowExecutionMock, null);
		FlowExecutionHolderUtils.setFlowExecutionHolder(holder, mockFacesContext);
		EasyMock.replay(new Object[] { flowExecutionMock });

		Object result = tested.resolveVariable(mockFacesContext, "flowScope");

		EasyMock.verify(new Object[] { flowExecutionMock });
		assertFalse("resolved using delegate", variableResolver.resolvedUsingDelegate);
		assertSame(flowExecutionMock, result);
	}

	private static class TestableVariableResolver extends VariableResolver {
		private boolean resolvedUsingDelegate;

		private Object expected = new Object();

		public Object resolveVariable(FacesContext arg0, String arg1) throws EvaluationException {
			resolvedUsingDelegate = true;
			return expected;
		}
	}
}