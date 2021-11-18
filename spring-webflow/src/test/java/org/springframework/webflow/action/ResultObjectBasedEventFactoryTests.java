/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.action;

import java.util.Date;

import junit.framework.TestCase;

import org.springframework.core.enums.StaticLabeledEnum;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.test.MockRequestContext;

/**
 * Test case for {@link ResultObjectBasedEventFactory}.
 * 
 * @author Erwin Vervaet
 */
public class ResultObjectBasedEventFactoryTests extends TestCase {

	private ResultObjectBasedEventFactory factory = new ResultObjectBasedEventFactory();

	public void testNull() {
		Event event = factory.createResultEvent(this, null, new MockRequestContext());
		assertEquals(factory.getNullEventId(), event.getId());
	}

	public void testBoolean() {
		Event event = factory.createResultEvent(this, Boolean.TRUE, new MockRequestContext());
		assertEquals(factory.getYesEventId(), event.getId());
		event = factory.createResultEvent(this, Boolean.FALSE, new MockRequestContext());
		assertEquals(factory.getNoEventId(), event.getId());
	}

	public void testLabeledEnum() {
		Event event = factory.createResultEvent(this, MyLabeledEnum.A, new MockRequestContext());
		assertEquals("A", event.getId());
		assertSame(MyLabeledEnum.A, event.getAttributes().get("result"));
	}

	public static class MyLabeledEnum extends StaticLabeledEnum {
		public static final MyLabeledEnum A = new MyLabeledEnum(0, "A");
		public static final MyLabeledEnum B = new MyLabeledEnum(0, "B");

		private MyLabeledEnum(int code, String label) {
			super(code, label);
		}

		public String toString() {
			return "MyLabeledEnum " + getLabel();
		}
	}

	/*
	 * public void testJava5Enum() { Event event = factory.createResultEvent(this, MyEnum.A, new MockRequestContext());
	 * assertEquals("A", event.getId()); assertSame(MyEnum.A, event.getAttributes().get("result")); }
	 * 
	 * public static enum MyEnum { A, B;
	 * 
	 * public String toString() { return "MyEnum " + name(); } }
	 */

	public void testString() {
		Event event = factory.createResultEvent(this, "foobar", new MockRequestContext());
		assertEquals("foobar", event.getId());
	}

	public void testEvent() {
		Event orig = new Event(this, "test");
		Event event = factory.createResultEvent(this, orig, new MockRequestContext());
		assertSame(orig, event);
	}

	public void testUnsupported() {
		try {
			factory.createResultEvent(this, new Date(), new MockRequestContext());
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}
	}
}
