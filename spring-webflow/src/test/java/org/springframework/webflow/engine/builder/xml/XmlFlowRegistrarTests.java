/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.builder.xml;

import junit.framework.TestCase;

import org.springframework.core.io.ClassPathResource;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistryImpl;
import org.springframework.webflow.definition.registry.FlowDefinitionResource;
import org.springframework.webflow.engine.builder.BaseFlowServiceLocator;

public class XmlFlowRegistrarTests extends TestCase {
	private XmlFlowRegistrar registrar;

	private FlowDefinitionRegistry registry = new FlowDefinitionRegistryImpl();

	protected void setUp() {
		BaseFlowServiceLocator locator = new BaseFlowServiceLocator();
		registrar = new XmlFlowRegistrar(locator);
	}

	public void testAddLocation() {
		assertEquals(0, registry.getFlowDefinitionCount());
		registrar.addLocation(new ClassPathResource("flow.xml", getClass()));
		registrar.registerFlowDefinitions(registry);
		assertEquals(1, registry.getFlowDefinitionCount());
		assertEquals("flow", registry.getFlowDefinition("flow").getId());
	}

	public void testAddResource() {
		assertEquals(0, registry.getFlowDefinitionCount());
		registrar.addResource(new FlowDefinitionResource("foo", new ClassPathResource("flow.xml", getClass())));
		registrar.registerFlowDefinitions(registry);
		assertEquals(1, registry.getFlowDefinitionCount());
		assertEquals("foo", registry.getFlowDefinition("foo").getId());
	}
}