/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine;

import junit.framework.TestCase;

import org.springframework.webflow.core.collection.LocalAttributeMap;
import org.springframework.webflow.execution.Event;

/**
 * Tests that each of the Flow state types execute as expected when entered.
 * 
 * @author Keith Donald
 */
public class EventTests extends TestCase {

	public void testNewEvent() {
		Event event = new Event(this, "id");
		assertEquals("id", event.getId());
		assertTrue(event.getTimestamp() > 0);
		assertTrue(event.getAttributes().isEmpty());
	}

	public void testEventNullSource() {
		try {
			new Event(null, "id");
			fail("null source");
		} catch (IllegalArgumentException e) {

		}
	}

	public void testEventNullId() {
		try {
			new Event(this, null);
			fail("null id");
		} catch (IllegalArgumentException e) {

		}
	}

	public void testNewEventWithAttributes() {
		LocalAttributeMap attrs = new LocalAttributeMap();
		attrs.put("name", "value");
		Event event = new Event(this, "id", attrs);
		assertTrue(!event.getAttributes().isEmpty());
		assertEquals(1, event.getAttributes().size());
	}

	public void testNewEventNullAttributes() {
		Event event = new Event(this, "id", null);
		assertTrue(event.getAttributes().isEmpty());
	}

}
