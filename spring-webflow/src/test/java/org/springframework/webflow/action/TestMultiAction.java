/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.action;

import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * Simple multi-action implementation used by the unit tests.
 * 
 * @author Erwin Vervaet
 */
public class TestMultiAction extends MultiAction {

	int counter = 0;

	public Event increment(RequestContext context) throws Exception {
		counter++;
		return success();
	}

	public Event decrement(RequestContext context) throws Exception {
		counter--;
		return success();
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}
}