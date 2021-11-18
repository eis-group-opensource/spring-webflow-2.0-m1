/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.builder.xml;

import junit.framework.TestCase;

import org.springframework.core.io.ClassPathResource;
import org.springframework.webflow.engine.ActionState;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.engine.builder.FlowAssembler;
import org.springframework.webflow.engine.impl.FlowExecutionImplFactory;
import org.springframework.webflow.execution.FlowExecution;
import org.springframework.webflow.execution.FlowSessionStatus;
import org.springframework.webflow.execution.support.ApplicationView;
import org.springframework.webflow.test.MockExternalContext;

public class EvaluateActionXmlFlowBuilderTests extends TestCase {
	private Flow flow;

	protected void setUp() throws Exception {
		XmlFlowBuilder builder = new XmlFlowBuilder(new ClassPathResource("evaluateActionFlow.xml",
				XmlFlowBuilderTests.class), new TestFlowServiceLocator());
		flow = new FlowAssembler("evaluateActionFlow", builder).assembleFlow();
	}

	public void testActionStateConfiguration() {
		assertTrue(flow.getState("actionState1") instanceof ActionState);
	}

	public void testFlowExecution() {
		FlowExecutionImplFactory factory = new FlowExecutionImplFactory();
		FlowExecution execution = factory.createFlowExecution(flow);
		ApplicationView selection = (ApplicationView) execution.start(null, new MockExternalContext());
		assertEquals(FlowSessionStatus.CREATED, execution.getActiveSession().getScope().get("sessionStatus"));
		assertNotNull(selection.getModel().get("hashCode"));
		assertEquals(new Integer(FlowSessionStatus.CREATED.hashCode()), selection.getModel().get("hashCode"));
	}
}