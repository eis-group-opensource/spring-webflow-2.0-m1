/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.builder.xml;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.definition.registry.FlowDefinitionResource;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.test.execution.AbstractXmlFlowExecutionTests;

/**
 * Named action tests.
 * 
 * @author Erwin Vervaet
 */
public class NamedActionXmlFlowBuilderTests extends AbstractXmlFlowExecutionTests {

	private int executionOrderCounter = 0;
	private Action aAction;
	private int aActionExecutionCount = 0;
	private int aActionExecutionOrder;
	private Object bBean;
	private int bBeanExecutionCount = 0;
	private int bBeanExecutionOrder;
	private Action cAction;
	private int cActionExecutionCount = 0;
	private int cActionExecutionOrder;

	protected void setUp() throws Exception {
		aAction = new AbstractAction() {
			protected Event doExecute(RequestContext context) throws Exception {
				aActionExecutionCount++;
				aActionExecutionOrder = executionOrderCounter++;
				return success();
			}
		};
		bBean = new TestBean(this);
		cAction = new AbstractAction() {
			protected Event doExecute(RequestContext context) throws Exception {
				cActionExecutionCount++;
				cActionExecutionOrder = executionOrderCounter++;
				return success();
			}
		};
	}

	protected FlowDefinitionResource getFlowDefinitionResource() {
		return new FlowDefinitionResource(new ClassPathResource("namedActionFlow.xml",
				NamedActionXmlFlowBuilderTests.class));
	}

	protected void registerLocalMockServices(Flow flow, ConfigurableBeanFactory beanFactory) {
		beanFactory.registerSingleton("aAction", aAction);
		beanFactory.registerSingleton("cAction", cAction);
		beanFactory.registerSingleton("bBean", bBean);
	}

	public void testActionExecutionOrder() {
		startFlow();
		assertFlowExecutionEnded();
		assertEquals(1, aActionExecutionCount);
		assertEquals(0, aActionExecutionOrder);
		assertEquals(1, bBeanExecutionCount);
		assertEquals(1, bBeanExecutionOrder);
		assertEquals(1, cActionExecutionCount);
		assertEquals(2, cActionExecutionOrder);
	}

	public static class TestBean {
		private NamedActionXmlFlowBuilderTests testCase;

		public TestBean(NamedActionXmlFlowBuilderTests testCase) {
			this.testCase = testCase;
		}

		public void b() {
			testCase.bBeanExecutionCount++;
			testCase.bBeanExecutionOrder = testCase.executionOrderCounter++;
		}
	}
}
