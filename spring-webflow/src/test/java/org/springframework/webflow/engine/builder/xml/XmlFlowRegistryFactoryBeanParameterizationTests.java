/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.builder.xml;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.execution.ViewSelection;
import org.springframework.webflow.execution.support.ApplicationView;
import org.springframework.webflow.test.MockRequestControlContext;

/**
 * Test flow parameterization using the XmlFlowRegistryFactoryBean.
 * 
 * @author Erwin Vervaet
 */
public class XmlFlowRegistryFactoryBeanParameterizationTests extends TestCase {

	private ApplicationContext applicationContext;

	protected void setUp() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext("parameterizedFlowContext.xml", getClass());
	}

	public void testNoFlowParameterization() {
		FlowDefinitionRegistry registry = (FlowDefinitionRegistry) applicationContext.getBean("flowRegistry0");
		assertEquals(1, registry.getFlowDefinitionCount());
		assertTrue(registry.containsFlowDefinition("parameterizedFlow"));
		Flow parameterizedFlow = (Flow) registry.getFlowDefinition("parameterizedFlow");
		assertEquals(0, parameterizedFlow.getAttributes().size());
		assertNull(parameterizedFlow.getAttributes().get("foo"));
	}

	public void testSimpleFlowParameterization() {
		FlowDefinitionRegistry registry = (FlowDefinitionRegistry) applicationContext.getBean("flowRegistry1");
		assertEquals(1, registry.getFlowDefinitionCount());
		assertTrue(registry.containsFlowDefinition("parameterizedFlow"));
		Flow parameterizedFlow = (Flow) registry.getFlowDefinition("parameterizedFlow");
		assertEquals(1, parameterizedFlow.getAttributes().size());
		assertEquals("bar", parameterizedFlow.getAttributes().get("foo"));
	}

	public void testAdvancedParameterization() {
		FlowDefinitionRegistry registry = (FlowDefinitionRegistry) applicationContext.getBean("flowRegistry2");

		assertEquals(2, registry.getFlowDefinitionCount());
		assertTrue(registry.containsFlowDefinition("flowA"));
		Flow flowA = (Flow) registry.getFlowDefinition("flowA");
		assertEquals(2, flowA.getAttributes().size());
		assertEquals("A", flowA.getAttributes().get("name"));
		assertEquals("someValue", flowA.getAttributes().get("someKey"));
		assertNull(flowA.getAttributes().get("someOtherKey"));

		assertTrue(registry.containsFlowDefinition("flowB"));
		Flow flowB = (Flow) registry.getFlowDefinition("flowB");
		assertEquals(2, flowB.getAttributes().size());
		assertEquals("B", flowB.getAttributes().get("name"));
		assertEquals("someOtherValue", flowB.getAttributes().get("someOtherKey"));
		assertNull(flowB.getAttributes().get("someKey"));
	}

	public void testAdvancedParameterizationAtRuntime() {
		FlowDefinitionRegistry registry = (FlowDefinitionRegistry) applicationContext.getBean("flowRegistry2");

		Flow flowA = (Flow) registry.getFlowDefinition("flowA");
		ViewSelection viewSelection = flowA.start(new MockRequestControlContext(flowA), null);
		assertEquals("A", ((ApplicationView) viewSelection).getViewName());

		Flow flowB = (Flow) registry.getFlowDefinition("flowB");
		viewSelection = flowB.start(new MockRequestControlContext(flowB), null);
		assertEquals("B", ((ApplicationView) viewSelection).getViewName());
	}
}
