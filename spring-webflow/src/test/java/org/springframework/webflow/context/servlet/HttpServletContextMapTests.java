/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.context.servlet;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import junit.framework.TestCase;

import org.springframework.mock.web.MockServletContext;

/**
 * Test case for the {@link HttpServletContextMap} class.
 * 
 * @author Ulrik Sandberg
 * @author Erwin Vervaet
 */
public class HttpServletContextMapTests extends TestCase {

	private HttpServletContextMap tested;

	private MockServletContext context;

	protected void setUp() throws Exception {
		super.setUp();
		context = new MockServletContext();
		// a fresh MockServletContext seems to already contain an element;
		// that's confusing, so we remove it
		context.removeAttribute("javax.servlet.context.tempdir");
		tested = new HttpServletContextMap(context);
		tested.put("SomeKey", "SomeValue");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		context = null;
		tested = null;
	}

	public void testIsEmpty() {
		tested.remove("SomeKey");
		assertEquals("size,", 0, tested.size());
		assertEquals("isEmpty,", true, tested.isEmpty());
	}

	public void testSizeAddOne() {
		assertEquals("size,", 1, tested.size());
	}

	public void testSizeAddTwo() {
		tested.put("SomeOtherKey", "SomeOtherValue");
		assertEquals("size,", 2, tested.size());
	}

	public void testContainsKey() {
		assertEquals("containsKey,", true, tested.containsKey("SomeKey"));
	}

	public void testContainsValue() {
		assertTrue(tested.containsValue("SomeValue"));
	}

	public void testGet() {
		assertEquals("get,", "SomeValue", tested.get("SomeKey"));
	}

	public void testPut() {
		Object old = tested.put("SomeKey", "SomeNewValue");

		assertEquals("old value,", "SomeValue", old);
		assertEquals("new value,", "SomeNewValue", tested.get("SomeKey"));
	}

	public void testRemove() {
		Object old = tested.remove("SomeKey");

		assertEquals("old value,", "SomeValue", old);
		assertNull("should be gone", tested.get("SomeKey"));
	}

	public void testPutAll() {
		Map otherMap = new HashMap();
		otherMap.put("SomeOtherKey", "SomeOtherValue");
		otherMap.put("SomeKey", "SomeUpdatedValue");
		tested.putAll(otherMap);
		assertEquals("SomeOtherValue", tested.get("SomeOtherKey"));
		assertEquals("SomeUpdatedValue", tested.get("SomeKey"));
	}

	public void testClear() {
		tested.clear();
		assertTrue(tested.isEmpty());
	}

	public void testKeySet() {
		assertEquals(1, tested.keySet().size());
		assertTrue(tested.keySet().contains("SomeKey"));
	}

	public void testValues() {
		assertEquals(1, tested.values().size());
		assertTrue(tested.values().contains("SomeValue"));
	}

	public void testEntrySet() {
		assertEquals(1, tested.entrySet().size());
		assertEquals("SomeKey", ((Entry) tested.entrySet().iterator().next()).getKey());
		assertEquals("SomeValue", ((Entry) tested.entrySet().iterator().next()).getValue());
	}
}