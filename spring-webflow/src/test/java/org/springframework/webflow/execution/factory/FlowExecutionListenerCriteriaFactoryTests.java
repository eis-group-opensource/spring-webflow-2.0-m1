/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.factory;

import junit.framework.TestCase;

import org.springframework.webflow.engine.Flow;

/**
 * Unit tests for {@link FlowExecutionListenerCriteriaFactory}.
 */
public class FlowExecutionListenerCriteriaFactoryTests extends TestCase {

	private FlowExecutionListenerCriteriaFactory factory = new FlowExecutionListenerCriteriaFactory();

	public void testAllFlows() {
		FlowExecutionListenerCriteria c = factory.allFlows();
		assertEquals(true, c.appliesTo(new Flow("foo")));
	}

	public void testFlowMatch() {
		FlowExecutionListenerCriteria c = factory.flow("foo");
		assertEquals(true, c.appliesTo(new Flow("foo")));
		assertEquals(false, c.appliesTo(new Flow("baz")));
	}

	public void testMultipleFlowMatch() {
		FlowExecutionListenerCriteria c = factory.flows(new String[] { "foo", "bar" });
		assertEquals(true, c.appliesTo(new Flow("foo")));
		assertEquals(true, c.appliesTo(new Flow("bar")));
		assertEquals(false, c.appliesTo(new Flow("baz")));
	}
}
