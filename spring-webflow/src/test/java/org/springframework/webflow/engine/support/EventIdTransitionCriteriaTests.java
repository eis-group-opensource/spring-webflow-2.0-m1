/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.support;

import junit.framework.TestCase;

import org.springframework.webflow.execution.Event;
import org.springframework.webflow.test.MockRequestContext;

public class EventIdTransitionCriteriaTests extends TestCase {
	public void testTestCriteria() {
		EventIdTransitionCriteria c = new EventIdTransitionCriteria("foo");
		MockRequestContext context = new MockRequestContext();
		context.setLastEvent(new Event(this, "foo"));
		assertEquals(true, c.test(context));
		context.setLastEvent(new Event(this, "FOO"));
		assertEquals(false, c.test(context)); // case sensitive
		context.setLastEvent(new Event(this, "bar"));
		assertEquals(false, c.test(context));
	}

	public void testNullLastEventId() {
		EventIdTransitionCriteria c = new EventIdTransitionCriteria("foo");
		MockRequestContext context = new MockRequestContext();
		context.setLastEvent(null);
		assertEquals(false, c.test(context));
	}

	public void testIllegalArg() {
		try {
			new EventIdTransitionCriteria(null);
			fail("was null");
		} catch (IllegalArgumentException e) {

		}
		try {
			new EventIdTransitionCriteria("");
			fail("was blank");
		} catch (IllegalArgumentException e) {

		}
	}
}
