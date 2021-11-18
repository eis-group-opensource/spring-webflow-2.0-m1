/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.support;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * Unit tests for {@link ApplicationView}.
 */
public class ApplicationViewTests extends TestCase {

	public void testConstructAndAccess() {
		Map model = new HashMap();
		model.put("name", "value");
		ApplicationView view = new ApplicationView("view", model);
		assertEquals("view", view.getViewName());
		assertEquals(1, view.getModel().size());
		assertEquals("value", model.get("name"));
		try {
			view.getModel().put("foo", "bar");
		} catch (UnsupportedOperationException e) {

		}
	}

	public void testNullParams() {
		ApplicationView view = new ApplicationView(null, null);
		assertEquals(0, view.getModel().size());
		assertEquals(null, view.getViewName());
		ApplicationView view2 = new ApplicationView(null, null);
		assertEquals(view, view2);
	}

	public void testMapLookup() {
		ApplicationView view = new ApplicationView("view", null);
		Map map = new HashMap();
		map.put("view", view);
		assertSame(view, map.get("view"));
	}
}
