/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.config.scope;

import junit.framework.TestCase;

import org.springframework.webflow.execution.FlowExecutionContextHolder;
import org.springframework.webflow.test.MockFlowExecutionContext;

/**
 * Test cases for the
 * @{link FlowScope} class.
 * 
 * @author Ben Hale
 */
public class FlowScopeTests extends TestCase {

	private MockFlowExecutionContext context;

	private FlowScope scope;

	protected void setUp() {
		context = new MockFlowExecutionContext();
		FlowExecutionContextHolder.setFlowExecutionContext(context);
		scope = new FlowScope();
	}

	protected void tearDown() {
		scope = null;
		context = null;
		FlowExecutionContextHolder.setFlowExecutionContext(null);
	}

	public void testGetVarMissing() {
		StubObjectFactory factory = new StubObjectFactory();
		Object gotten = scope.get("name", factory);
		assertNotNull("Should be real object", gotten);
		assertTrue("Should have added object to the map", context.getActiveSession().getScope().contains("name"));
		assertSame("Created object should have been returned", factory.getValue(), gotten);
		assertSame("Created object should have been persisted", factory.getValue(), context.getActiveSession()
				.getScope().get("name"));
	}

	public void testGetVarExist() {
		StubObjectFactory factory = new StubObjectFactory();
		Object value = new Object();
		context.getActiveSession().getScope().put("name", value);
		Object gotten = scope.get("name", factory);
		assertNotNull("Should be real object", gotten);
		assertTrue("Should still be in map", context.getActiveSession().getScope().contains("name"));
		assertSame("Persisted object should have been returned", value, gotten);
		assertNotSame("Created object should not have been returned", factory.getValue(), gotten);
	}

	public void testGetRequestContextMissing() {
		FlowExecutionContextHolder.setFlowExecutionContext(null);
		StubObjectFactory factory = new StubObjectFactory();
		try {
			scope.get("name", factory);
		} catch (IllegalStateException e) {
		}
	}

	public void testGetConversationId() {
		String flowId = scope.getConversationId();
		assertNull("Method not implemented yet, should return null", flowId);
	}

	public void testRemoveVarMissing() {
		Object removed = scope.remove("name");
		assertFalse("Should have removed from object from map", context.getActiveSession().getScope().contains("name"));
		assertNull("Should have returned a null object", removed);
	}

	public void testRemoveVarExist() {
		Object value = new Object();
		context.getActiveSession().getScope().put("name", value);
		Object removed = scope.remove("name");
		assertFalse("Should have removed from object from map", context.getActiveSession().getScope().contains("name"));
		assertSame("Should have returned the previous object", removed, value);
	}

	public void testRemoveRequestContextMissing() {
		FlowExecutionContextHolder.setFlowExecutionContext(null);
		try {
			scope.remove("name");
			fail("Should have thrown a IllegalStateException without a request context");
		} catch (IllegalStateException e) {
		}
	}

}
