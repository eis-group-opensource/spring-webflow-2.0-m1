/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.collection;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

public class MapAccessorTests extends TestCase {
	private MapAccessor accessor;

	protected void setUp() throws Exception {
		Map map = new HashMap();
		map.put("string", "hello");
		map.put("integer", new Integer(9));
		map.put("null", null);
		this.accessor = new MapAccessor(map);
	}

	public void testAccessNullAttribute() {
		assertEquals(null, accessor.get("null"));
		assertEquals(null, accessor.get("null", "something else"));
		assertEquals(null, accessor.getRequired("null"));
		assertEquals(null, accessor.getString("null"));
		assertEquals(null, accessor.getRequiredString("null"));
		assertEquals(null, accessor.getInteger("null"));
		assertEquals(null, accessor.getRequiredInteger("null"));
		assertEquals(null, accessor.getCollection("null"));
		assertEquals(null, accessor.getRequiredCollection("null"));
	}

	public void testGetString() {
		assertEquals("hello", accessor.getString("string"));
		assertEquals("hello", accessor.getRequiredString("string"));
	}

	public void testGetInteger() {
		assertEquals(new Integer(9), accessor.getInteger("integer"));
		assertEquals(new Integer(9), accessor.getRequiredInteger("integer"));
	}

	public void testGetRequiredMissingKey() {
		try {
			accessor.getRequired("bogus");
		} catch (IllegalArgumentException e) {
		}
	}

}
