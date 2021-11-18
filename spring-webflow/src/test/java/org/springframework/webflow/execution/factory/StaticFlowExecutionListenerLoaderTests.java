/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.factory;

import junit.framework.TestCase;

import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.execution.FlowExecutionListener;
import org.springframework.webflow.execution.FlowExecutionListenerAdapter;

/**
 * Unit tests for {@link StaticFlowExecutionListenerLoader}.
 */
public class StaticFlowExecutionListenerLoaderTests extends TestCase {

	private FlowExecutionListenerLoader loader = StaticFlowExecutionListenerLoader.EMPTY_INSTANCE;

	public void testEmptyListenerArray() {
		assertEquals(0, loader.getListeners(new Flow("foo")).length);
		assertEquals(0, loader.getListeners(null).length);
	}

	public void testStaticListener() {
		final FlowExecutionListener listener1 = new FlowExecutionListenerAdapter() {
		};
		loader = new StaticFlowExecutionListenerLoader(listener1);
		assertEquals(listener1, loader.getListeners(new Flow("foo"))[0]);
	}

	public void testStaticListeners() {
		final FlowExecutionListener listener1 = new FlowExecutionListenerAdapter() {
		};
		final FlowExecutionListener listener2 = new FlowExecutionListenerAdapter() {
		};

		loader = new StaticFlowExecutionListenerLoader(new FlowExecutionListener[] { listener1, listener2 });
		assertEquals(listener1, loader.getListeners(new Flow("foo"))[0]);
		assertEquals(listener2, loader.getListeners(new Flow("foo"))[1]);
	}

}