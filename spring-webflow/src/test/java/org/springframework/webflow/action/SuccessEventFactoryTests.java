/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.action;

import junit.framework.TestCase;

import org.springframework.webflow.execution.Event;
import org.springframework.webflow.test.MockRequestContext;

/**
 * Unit tests for {@link SuccessEventFactory}.
 */
public class SuccessEventFactoryTests extends TestCase {

	private MockRequestContext context = new MockRequestContext();

	private SuccessEventFactory factory = new SuccessEventFactory();

	public void testDefaultAdaptionRules() {
		Event result = factory.createResultEvent(this, "result", context);
		assertEquals("success", result.getId());
		assertEquals("result", result.getAttributes().getString("result"));
	}
}