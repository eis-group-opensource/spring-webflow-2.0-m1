/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.builder;

import junit.framework.Assert;

import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * Test action used by some unit tests.
 * 
 * @author Erwin Vervaet
 */
public class ParameterizationTestAction extends AbstractAction {

	protected Event doExecute(RequestContext context) throws Exception {
		if ("flowA".equals(context.getActiveFlow().getId())) {
			Flow flowA = (Flow) context.getActiveFlow();
			Assert.assertEquals(2, flowA.getAttributes().size());
			Assert.assertEquals("A", flowA.getAttributes().get("name"));
			Assert.assertEquals("someValue", flowA.getAttributes().get("someKey"));
			Assert.assertNull(flowA.getAttributes().get("someOtherKey"));
		} else if ("flowB".equals(context.getActiveFlow().getId())) {
			Flow flowB = (Flow) context.getActiveFlow();
			Assert.assertEquals(2, flowB.getAttributes().size());
			Assert.assertEquals("B", flowB.getAttributes().get("name"));
			Assert.assertEquals("someOtherValue", flowB.getAttributes().get("someOtherKey"));
			Assert.assertNull(flowB.getAttributes().get("someKey"));
		} else {
			throw new IllegalStateException();
		}
		return success();
	}

}
