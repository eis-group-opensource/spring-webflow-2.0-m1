/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.action;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.springframework.webflow.core.collection.LocalAttributeMap;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.test.MockRequestContext;

/**
 * Unit tests for the {@link CompositeAction} class.
 * 
 * @author Ulrik Sandberg
 */
public class CompositeActionTests extends TestCase {

	private CompositeAction tested;

	private Action actionMock;

	protected void setUp() throws Exception {
		super.setUp();
		actionMock = (Action) EasyMock.createMock(Action.class);
		Action[] actions = new Action[] { actionMock };
		tested = new CompositeAction(actions);
	}

	public void testDoExecute() throws Exception {
		MockRequestContext mockRequestContext = new MockRequestContext();
		LocalAttributeMap attributes = new LocalAttributeMap();
		attributes.put("some key", "some value");
		EasyMock.expect(actionMock.execute(mockRequestContext)).andReturn(new Event(this, "some event", attributes));
		EasyMock.replay(new Object[] { actionMock });
		Event result = tested.doExecute(mockRequestContext);
		EasyMock.verify(new Object[] { actionMock });
		assertEquals("some event", result.getId());
		assertEquals(1, result.getAttributes().size());
	}

	public void testDoExecuteWithError() throws Exception {
		tested.setStopOnError(true);
		MockRequestContext mockRequestContext = new MockRequestContext();
		EasyMock.expect(actionMock.execute(mockRequestContext)).andReturn(new Event(this, "error"));
		EasyMock.replay(new Object[] { actionMock });
		Event result = tested.doExecute(mockRequestContext);
		EasyMock.verify(new Object[] { actionMock });
		assertEquals("error", result.getId());
	}

	public void testDoExecuteWithNullResult() throws Exception {
		tested.setStopOnError(true);
		MockRequestContext mockRequestContext = new MockRequestContext();
		EasyMock.expect(actionMock.execute(mockRequestContext)).andReturn(null);
		EasyMock.replay(new Object[] { actionMock });
		Event result = tested.doExecute(mockRequestContext);
		EasyMock.verify(new Object[] { actionMock });
		assertEquals("Expecting success since no check is performed if null result,", "success", result.getId());
	}

	public void testMultipleActions() throws Exception {
		CompositeAction ca = new CompositeAction(new Action[] { new Action() {
			public Event execute(RequestContext context) throws Exception {
				return new Event(this, "foo");
			}
		}, new Action() {
			public Event execute(RequestContext context) throws Exception {
				return new Event(this, "bar");
			}
		} });
		assertEquals("Result of last executed action should be returned", "bar", ca.execute(new MockRequestContext())
				.getId());
	}
}