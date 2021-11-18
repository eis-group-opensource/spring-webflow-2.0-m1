/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.builder.xml;

import java.util.Properties;

import junit.framework.TestCase;

import org.springframework.beans.factory.support.StaticListableBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;

public class XmlFlowRegistryFactoryBeanTests extends TestCase {
	private XmlFlowRegistryFactoryBean factoryBean = new XmlFlowRegistryFactoryBean();

	public void testCreateFromLocations() throws Exception {
		ClassPathResource[] locations = new ClassPathResource[] { new ClassPathResource("flow.xml", getClass()) };
		factoryBean.setFlowLocations(locations);
		factoryBean.setBeanFactory(new StaticListableBeanFactory());
		factoryBean.afterPropertiesSet();
		FlowDefinitionRegistry registry = (FlowDefinitionRegistry) factoryBean.getObject();
		assertEquals(1, registry.getFlowDefinitionCount());
		assertEquals("flow", registry.getFlowDefinition("flow").getId());
	}

	public void testCreateFromDefinitions() throws Exception {
		Properties properties = new Properties();
		properties.put("foo", "classpath:/org/springframework/webflow/engine/builder/xml/flow.xml");
		factoryBean.setFlowDefinitions(properties);
		factoryBean.setBeanFactory(new StaticListableBeanFactory());
		factoryBean.afterPropertiesSet();
		FlowDefinitionRegistry registry = (FlowDefinitionRegistry) factoryBean.getObject();
		assertEquals(1, registry.getFlowDefinitionCount());
		assertEquals("foo", registry.getFlowDefinition("foo").getId());
	}
}
