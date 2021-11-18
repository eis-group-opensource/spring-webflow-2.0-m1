/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.context.portlet;

import java.util.Iterator;

import junit.framework.TestCase;

import org.springframework.mock.web.portlet.MockPortletRequest;

/**
 * Unit test for the {@link PortletRequestMap} class.
 * 
 * @author Ulrik Sandberg
 */
public class PortletRequestMapTests extends TestCase {

	private PortletRequestMap tested;

	private MockPortletRequest request;

	protected void setUp() throws Exception {
		super.setUp();
		request = new MockPortletRequest();
		tested = new PortletRequestMap(request);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		request = null;
		tested = null;
	}

	public void testGetAttribute() {
		request.setAttribute("Some key", "Some value");
		// perform test
		Object result = tested.getAttribute("Some key");
		assertEquals("Some value", result);
	}

	public void testSetAttribute() {
		// perform test
		tested.setAttribute("Some key", "Some value");
		assertEquals("Some value", request.getAttribute("Some key"));
	}

	public void testRemoveAttribute() {
		request.setAttribute("Some key", "Some value");
		// perform test
		tested.removeAttribute("Some key");
		assertNull(request.getAttribute("Some key"));
	}

	public void testGetAttributeNames() {
		request.setAttribute("Some key", "Some value");
		request.removeAttribute("javax.servlet.context.tempdir");
		// perform test
		Iterator names = tested.getAttributeNames();
		assertNotNull("Null result unexpected", names);
		assertTrue("More elements", names.hasNext());
		String name = (String) names.next();
		assertEquals("Some key", name);
	}
}