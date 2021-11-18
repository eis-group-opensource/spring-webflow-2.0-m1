/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.action;

import junit.framework.TestCase;

import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.webflow.core.collection.LocalAttributeMap;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.test.MockRequestContext;

/**
 * Unit tests for {@link AbstractAction}.
 */
public class AbstractActionTests extends TestCase {

	private TestAbstractAction action = new TestAbstractAction();

	public void testInitCallback() throws Exception {
		action.afterPropertiesSet();
		assertTrue(action.initialized);
	}

	public void testInitCallbackWithException() throws Exception {
		action = new TestAbstractAction() {
			protected void initAction() {
				throw new IllegalStateException("Cannot initialize");
			}
		};
		try {
			action.afterPropertiesSet();
			fail("Should've failed initialization");
		} catch (BeanInitializationException e) {
			assertFalse(action.initialized);
		}
	}

	public void testNormalExecute() throws Exception {
		action = new TestAbstractAction() {
			protected Event doExecute(RequestContext context) throws Exception {
				return success();
			}
		};
		Event result = action.execute(new MockRequestContext());
		assertEquals("success", result.getId());
		assertTrue(result.getAttributes().size() == 0);
	}

	public void testExceptionalExecute() throws Exception {
		try {
			action.execute(new MockRequestContext());
			fail("Should've failed execute");
		} catch (IllegalStateException e) {

		}
	}

	public void testPreExecuteShortCircuit() throws Exception {
		action = new TestAbstractAction() {
			protected Event doPreExecute(RequestContext context) throws Exception {
				return success();
			}
		};
		Event result = action.execute(new MockRequestContext());
		assertEquals("success", result.getId());
	}

	public void testPostExecuteCalled() throws Exception {
		testNormalExecute();
		assertTrue(action.postExecuteCalled);
	}

	public class TestAbstractAction extends AbstractAction {
		private boolean initialized;

		private boolean postExecuteCalled;

		protected void initAction() {
			initialized = true;
		}

		protected Event doExecute(RequestContext context) throws Exception {
			throw new IllegalStateException("Should not be called");
		}

		protected void doPostExecute(RequestContext context) {
			postExecuteCalled = true;
		}
	}

	public void testSuccess() {
		Event event = action.success();
		assertEquals(action.getEventFactorySupport().getSuccessEventId(), event.getId());
	}

	public void testSuccessResult() {
		Object o = new Object();
		Event event = action.success(o);
		assertEquals(action.getEventFactorySupport().getSuccessEventId(), event.getId());
		assertSame(o, event.getAttributes().get(action.getEventFactorySupport().getResultAttributeName()));
	}

	public void testError() {
		Event event = action.error();
		assertEquals(action.getEventFactorySupport().getErrorEventId(), event.getId());
	}

	public void testErrorException() {
		IllegalArgumentException e = new IllegalArgumentException("woops");
		Event event = action.error(e);
		assertEquals(action.getEventFactorySupport().getErrorEventId(), event.getId());
		assertSame(e, event.getAttributes().get(action.getEventFactorySupport().getExceptionAttributeName()));
	}

	public void testYes() {
		Event event = action.yes();
		assertEquals(action.getEventFactorySupport().getYesEventId(), event.getId());
	}

	public void testNo() {
		Event event = action.no();
		assertEquals(action.getEventFactorySupport().getNoEventId(), event.getId());
	}

	public void testTrueResult() {
		Event event = action.result(true);
		assertEquals(action.getEventFactorySupport().getYesEventId(), event.getId());
	}

	public void testFalseResult() {
		Event event = action.result(false);
		assertEquals(action.getEventFactorySupport().getNoEventId(), event.getId());
	}

	public void testCustomResult() {
		Event event = action.result("custom");
		assertEquals("custom", event.getId());
	}

	public void testCustomResultObject() {
		Event event = action.result("custom", "result", "value");
		assertEquals("custom", event.getId());
		assertEquals("value", event.getAttributes().getString("result"));
	}

	public void testCustomResultCollection() {
		LocalAttributeMap collection = new LocalAttributeMap();
		collection.put("result", "value");
		Event event = action.result("custom", collection);
		assertEquals("custom", event.getId());
		assertEquals("value", event.getAttributes().getString("result"));
	}
}