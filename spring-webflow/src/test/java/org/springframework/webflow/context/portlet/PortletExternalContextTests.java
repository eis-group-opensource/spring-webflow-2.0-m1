/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.context.portlet;

import junit.framework.TestCase;

import org.springframework.mock.web.portlet.MockPortletContext;
import org.springframework.mock.web.portlet.MockPortletRequest;
import org.springframework.mock.web.portlet.MockPortletResponse;

/**
 * Unit tests for {@link PortletExternalContext}.
 */
public class PortletExternalContextTests extends TestCase {

	private PortletExternalContext context = new PortletExternalContext(new MockPortletContext(),
			new MockPortletRequest(), new MockPortletResponse());

	public void testApplicationMap() {
		assertEquals(1, context.getApplicationMap().size());
		context.getApplicationMap().put("foo", "bar");
		assertEquals("bar", context.getApplicationMap().get("foo"));
		assertEquals("bar", context.getContext().getAttribute("foo"));
	}

	public void testSessionMap() {
		assertEquals(0, context.getSessionMap().size());
		context.getSessionMap().put("foo", "bar");
		assertEquals("bar", context.getSessionMap().get("foo"));
		assertEquals("bar", context.getRequest().getPortletSession().getAttribute("foo"));
	}

	public void testRequestMap() {
		assertEquals(0, context.getRequestMap().size());
		context.getRequestMap().put("foo", "bar");
		assertEquals("bar", context.getRequestMap().get("foo"));
		assertEquals("bar", context.getRequest().getAttribute("foo"));
	}

	public void testOther() {
		assertNull(context.getRequestPathInfo());
		assertNull(context.getDispatcherPath());
		assertNotNull(context.getResponse());
	}
}
