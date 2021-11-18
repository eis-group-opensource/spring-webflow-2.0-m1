/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution;

import org.springframework.util.StringUtils;
import org.springframework.webflow.action.AbstractAction;

/**
 * Test action for use in unit tests.
 */
public class TestAction extends AbstractAction {

	private Event result = new Event(this, "success");

	private boolean executed;

	private int executionCount;

	public TestAction() {

	}

	public TestAction(String result) {
		if (StringUtils.hasText(result)) {
			this.result = new Event(this, result);
		} else {
			this.result = null;
		}
	}

	public boolean isExecuted() {
		return executed;
	}

	public int getExecutionCount() {
		return executionCount;
	}

	protected Event doExecute(RequestContext context) throws Exception {
		executed = true;
		executionCount++;
		return result;
	}
}