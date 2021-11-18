/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine;

import junit.framework.TestCase;

public class AnnotatedObjectTests extends TestCase {

	private AnnotatedObject object = new Flow("foo");

	public void testSetCaption() {
		object.setCaption("caption");
		assertEquals("caption", object.getCaption());
	}

	public void testSetDescription() {
		object.setDescription("description");
		assertEquals("description", object.getDescription());
	}

	public void testPutCustomAttributes() {
		object.getAttributeMap().put("foo", "bar");
		assertEquals("bar", object.getAttributeMap().get("foo"));
	}

}
