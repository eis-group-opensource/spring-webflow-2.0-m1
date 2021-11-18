/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.faces.webflow;

import junit.framework.TestCase;

import org.springframework.faces.webflow.FlowNavigationHandlerArgumentExtractor;
import org.springframework.faces.webflow.JsfExternalContext;
import org.springframework.webflow.executor.support.FlowExecutorArgumentExtractionException;

public class FlowNavigationHandlerArgumentExtractorTests extends TestCase {

	private FlowNavigationHandlerArgumentExtractor extractor;

	private MockFacesContext facesContext;

	protected void setUp() throws Exception {
		extractor = new FlowNavigationHandlerArgumentExtractor();
		facesContext = new MockFacesContext();
		facesContext.setExternalContext(new MockJsfExternalContext());
	}

	public void testExtractFlowId() {
		JsfExternalContext context = new JsfExternalContext(facesContext);
		context.handleNavigationCalled("action", "flowId:foo");
		String flowId = extractor.extractFlowId(context);
		assertEquals("Wrong flow id", "foo", flowId);
	}

	public void testExtractFlowIdWrongFormat() {
		JsfExternalContext context = new JsfExternalContext(facesContext);
		context.handleNavigationCalled("action", "bogus:foo");
		try {
			extractor.extractFlowId(context);
			fail();
		} catch (FlowExecutorArgumentExtractionException e) {
			// expected
		}
	}

	public void testExtractEventId() {
		JsfExternalContext context = new JsfExternalContext(facesContext);
		context.handleNavigationCalled("action", "submit");
		String eventId = extractor.extractEventId(context);
		assertEquals("Wrong event id", "submit", eventId);
	}
}