/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.support;

import junit.framework.TestCase;

import org.springframework.webflow.execution.Event;

/**
 * Unit tests for {@link EventFactorySupport}.
 */
public class EventFactorySupportTests extends TestCase {

	private EventFactorySupport support = new EventFactorySupport();

	private Object source = new Object();

	protected void setUp() throws Exception {
	}

	public void testSuccess() {
		Event e = support.success(source);
		assertEquals("success", e.getId());
		assertSame(source, e.getSource());
	}

	public void testSuccessWithResult() {
		Object result = new Object();
		Event e = support.success(source, result);
		assertEquals("success", e.getId());
		assertSame(source, e.getSource());
		assertSame(result, e.getAttributes().get("result"));
	}

	public void testError() {
		Event e = support.error(source);
		assertEquals("error", e.getId());
		assertSame(source, e.getSource());
	}

	public void testErrorWithException() {
		Exception ex = new Exception();
		Event e = support.error(source, ex);
		assertEquals("error", e.getId());
		assertSame(source, e.getSource());
		assertSame(ex, e.getAttributes().get("exception"));
	}

	public void testYes() {
		Event e = support.yes(source);
		assertEquals("yes", e.getId());
		assertSame(source, e.getSource());
	}

	public void testNo() {
		Event e = support.no(source);
		assertEquals("no", e.getId());
		assertSame(source, e.getSource());
	}

	public void testBooleanTrueEvent() {
		Event e = support.event(source, true);
		assertEquals("yes", e.getId());
		assertSame(source, e.getSource());
	}

	public void testBooleanFalseEvent() {
		Event e = support.event(source, false);
		assertEquals("no", e.getId());
		assertSame(source, e.getSource());
	}

	public void testEvent() {
		Event e = support.event(source, "no");
		assertEquals("no", e.getId());
		assertSame(source, e.getSource());
	}

	public void testEventWithAttrs() {
		Event e = support.event(source, "no", "foo", "bar");
		assertEquals("no", e.getId());
		assertEquals("bar", e.getAttributes().get("foo"));
		assertSame(source, e.getSource());
	}

}