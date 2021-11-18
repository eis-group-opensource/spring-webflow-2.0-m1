/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.support;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.test.MockRequestContext;

/**
 * Unit tests for the ActionTransitionCriteria class.
 * 
 * @author Ulrik Sandberg
 */
public class ActionTransitionCriteriaTests extends TestCase {

	private Action actionMock;

	private ActionTransitionCriteria tested;

	protected void setUp() throws Exception {
		super.setUp();
		actionMock = (Action) EasyMock.createMock(Action.class);
		tested = new ActionTransitionCriteria(actionMock);
	}

	public void testGetTrueEventId() {
		String id = tested.getTrueEventId();
		assertEquals("success", id);
	}

	public void testSetTrueEventId() {
		tested.setTrueEventId("something");
		String id = tested.getTrueEventId();
		assertEquals("something", id);
	}

	public void testGetAction() {
		Action action = tested.getAction();
		assertSame(actionMock, action);
	}

	public void testTest() throws Exception {
		MockRequestContext mockRequestContext = new MockRequestContext();
		EasyMock.expect(actionMock.execute(mockRequestContext)).andReturn(new Event(this, "success"));
		EasyMock.replay(new Object[] { actionMock });
		boolean result = tested.test(mockRequestContext);
		EasyMock.verify(new Object[] { actionMock });
		assertEquals(true, result);
	}
}