/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.context.servlet;

import java.util.Iterator;

import junit.framework.TestCase;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.util.WebUtils;

/**
 * Unit test for the {@link HttpSessionMap} class.
 * 
 * @author Ulrik Sandberg
 */
public class HttpSessionMapTests extends TestCase {

	private HttpSessionMap tested;

	private MockHttpServletRequest request;

	protected void setUp() throws Exception {
		super.setUp();
		request = new MockHttpServletRequest();
		tested = new HttpSessionMap(request);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		request = null;
		tested = null;
	}

	public void testGetAttribute() {
		request.getSession().setAttribute("Some key", "Some value");
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
		assertEquals("Some value", request.getSession().getAttribute("Some key"));
	}

	public void testRemoveAttribute() {
		request.getSession().setAttribute("Some key", "Some value");
		// perform test
		tested.removeAttribute("Some key");
		assertNull(request.getSession().getAttribute("Some key"));
	}

	public void testRemoveAttributeNullSession() {
		request.setSession(null);
		// perform test
		tested.removeAttribute("Some key");
		assertNull(request.getSession().getAttribute("Some key"));
	}

	public void testGetAttributeNames() {
		request.getSession().setAttribute("Some key", "Some value");
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
		assertSame(mutex, request.getSession());
	}

	public void testGetSessionMutex() {
		Object object = new Object();
		request.getSession().setAttribute(WebUtils.SESSION_MUTEX_ATTRIBUTE, object);
		Object mutex = tested.getMutex();
		assertSame(mutex, object);
	}
}