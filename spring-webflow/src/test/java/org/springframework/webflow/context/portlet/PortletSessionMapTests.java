/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.context.portlet;

import java.util.Iterator;

import javax.portlet.PortletSession;

import junit.framework.TestCase;

import org.springframework.mock.web.portlet.MockPortletRequest;
import org.springframework.web.util.WebUtils;

/**
 * Unit test for the {@link PortletSessionMap} class.
 * 
 * @author Ulrik Sandberg
 */
public class PortletSessionMapTests extends TestCase {

	private PortletSessionMap tested;

	private MockPortletRequest request;

	protected void setUp() throws Exception {
		super.setUp();
		request = new MockPortletRequest();
		tested = new PortletSessionMap(request, PortletSession.PORTLET_SCOPE);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		request = null;
		tested = null;
	}

	public void testGetAttribute() {
		request.getPortletSession().setAttribute("Some key", "Some value");
		// perform test
		Object result = tested.getAttribute("Some key");
		assertEquals("Some value", result);
	}

	public void testGetAttributeNullSession() {
		request.setSession(null);
		// perform test
		Object result = tested.getAttribute("Some key");
		assertNull("No value expected", result);
	}

	public void testSetAttribute() {
		// perform test
		tested.setAttribute("Some key", "Some value");
		assertEquals("Some value", request.getPortletSession().getAttribute("Some key"));
	}

	public void testRemoveAttribute() {
		request.getPortletSession().setAttribute("Some key", "Some value");
		// perform test
		tested.removeAttribute("Some key");
		assertNull(request.getPortletSession().getAttribute("Some key"));
	}

	public void testRemoveAttributeNullSession() {
		request.setSession(null);
		// perform test
		tested.removeAttribute("Some key");
		assertNull(request.getPortletSession().getAttribute("Some key"));
	}

	public void testGetAttributeNames() {
		request.getPortletSession().setAttribute("Some key", "Some value");
		// perform test
		Iterator names = tested.getAttributeNames();
		assertNotNull("Null result unexpected", names);
		assertTrue("More elements", names.hasNext());
		String name = (String) names.next();
		assertEquals("Some key", name);
	}

	public void testGetAttributeNamesNullSession() {
		request.setSession(null);
		// perform test
		Iterator names = tested.getAttributeNames();
		assertNotNull("Null result unexpected", names);
		assertFalse("No elements expected", names.hasNext());
	}

	public void testGetSessionAsMutex() {
		Object mutex = tested.getMutex();
		assertSame(mutex, request.getPortletSession());
	}

	public void testGetSessionMutex() {
		Object object = new Object();
		request.getPortletSession().setAttribute(WebUtils.SESSION_MUTEX_ATTRIBUTE, object);
		Object mutex = tested.getMutex();
		assertSame(mutex, object);
	}
}