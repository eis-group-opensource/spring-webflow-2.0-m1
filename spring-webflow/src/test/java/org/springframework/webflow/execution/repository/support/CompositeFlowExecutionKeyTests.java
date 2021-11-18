/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.repository.support;

import junit.framework.TestCase;

import org.springframework.webflow.conversation.impl.SimpleConversationId;

/**
 * Unit tests for {@link CompositeFlowExecutionKey}.
 */
public class CompositeFlowExecutionKeyTests extends TestCase {

	public void testValidKey() {
		CompositeFlowExecutionKey key = new CompositeFlowExecutionKey(new SimpleConversationId("foo"), "bar");
		assertEquals("_cfoo_kbar", key.toString());
	}

	public void testKeyEquals() {
		CompositeFlowExecutionKey key = new CompositeFlowExecutionKey(new SimpleConversationId("foo"), "bar");
		CompositeFlowExecutionKey key2 = new CompositeFlowExecutionKey(new SimpleConversationId("foo"), "bar");
		assertEquals(key, key2);
	}

}
