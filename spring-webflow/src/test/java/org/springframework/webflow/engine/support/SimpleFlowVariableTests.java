/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.support;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.springframework.webflow.execution.ScopeType;
import org.springframework.webflow.test.MockRequestContext;

public class SimpleFlowVariableTests extends TestCase {
	private MockRequestContext context = new MockRequestContext();

	public void testCreateValidFlowVariableCustomScope() {
		SimpleFlowVariable variable = new SimpleFlowVariable("var", ArrayList.class, ScopeType.REQUEST);
		variable.create(context);
		assertTrue(context.getRequestScope().contains("var"));
		context.getRequestScope().getRequired("var", ArrayList.class);
	}

	public void testCreateVariableNoDefaultConstructor() {
		SimpleFlowVariable variable = new SimpleFlowVariable("var", Integer.class, ScopeType.FLOW);
		try {
			variable.create(context);
			fail("should have failed");
		} catch (Exception e) {

		}
	}
}
