/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.support;

import junit.framework.TestCase;

import org.springframework.context.support.StaticApplicationContext;
import org.springframework.webflow.execution.ScopeType;
import org.springframework.webflow.test.MockRequestContext;

public class BeanFactoryFlowVariableTests extends TestCase {
	private MockRequestContext context = new MockRequestContext();

	public void testCreateValidFlowVariable() {
		StaticApplicationContext beanFactory = new StaticApplicationContext();
		beanFactory.registerPrototype("bean", Object.class);
		BeanFactoryFlowVariable variable = new BeanFactoryFlowVariable("var", "bean", beanFactory, ScopeType.FLOW);
		variable.create(context);
		context.getFlowScope().getRequired("var");
	}
}