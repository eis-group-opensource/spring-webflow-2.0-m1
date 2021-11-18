/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine;

import junit.framework.TestCase;

import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.TestAction;
import org.springframework.webflow.test.MockRequestContext;

public class AnnotedActionTests extends TestCase {

	private AnnotatedAction action = new AnnotatedAction(new TestAction());

	private MockRequestContext context = new MockRequestContext();

	protected void setUp() throws Exception {
	}

	public void testBasicExecute() throws Exception {
		assertEquals("success", action.execute(context).getId());
	}

	public void testExecuteWithCustomAttribute() throws Exception {
		action.getAttributeMap().put("attr", "value");
		action.setTargetAction(new AbstractAction() {
			protected Event doExecute(RequestContext context) throws Exception {
				assertEquals("value", context.getAttributes().getString("attr"));
				return success();
			}
		});
		assertEquals("success", action.execute(context).getId());
	}

	public void testExecuteWithName() throws Exception {
		action.getAttributeMap().put("name", "foo");
		action.setTargetAction(new AbstractAction() {
			protected Event doExecute(RequestContext context) throws Exception {
				assertEquals("foo", context.getAttributes().getString("name"));
				return success();
			}
		});
		assertEquals("foo.success", action.execute(context).getId());
	}
}