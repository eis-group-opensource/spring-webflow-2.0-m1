/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.action;

import junit.framework.TestCase;

import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.FlowSessionStatus;
import org.springframework.webflow.test.MockRequestContext;

/**
 * Unit tests for {@link ResultObjectBasedEventFactory}.
 */
public class ResultObjectEventFactoryTests extends TestCase {

	private MockRequestContext context = new MockRequestContext();

	private ResultObjectBasedEventFactory factory = new ResultObjectBasedEventFactory();

	public void testAlreadyAnEvent() {
		Event event = new Event(this, "event");
		Event result = factory.createResultEvent(this, event, context);
		assertSame(event, result);
	}

	public void testMappedTypes() {
		assertTrue(factory.isMappedValueType(FlowSessionStatus.class));
		assertTrue(factory.isMappedValueType(boolean.class));
		assertTrue(factory.isMappedValueType(Boolean.class));
		assertTrue(factory.isMappedValueType(String.class));
		assertFalse(factory.isMappedValueType(Integer.class));
	}

	public void testNullResult() {
		Event result = factory.createResultEvent(this, null, context);
		assertEquals("null", result.getId());
	}

	public void testBooleanResult() {
		Event result = factory.createResultEvent(this, Boolean.TRUE, context);
		assertEquals("yes", result.getId());
		result = factory.createResultEvent(this, Boolean.FALSE, context);
		assertEquals("no", result.getId());
	}

	public void testLabeledEnumResult() {
		Event result = factory.createResultEvent(this, FlowSessionStatus.ACTIVE, context);
		assertEquals("Active", result.getId());
	}

	public void testOtherResult() {
		Event result = factory.createResultEvent(this, "hello", context);
		assertEquals("hello", result.getId());
	}
}