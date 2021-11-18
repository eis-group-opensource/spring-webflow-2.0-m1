/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.builder.xml;

import junit.framework.TestCase;

import org.springframework.core.io.ClassPathResource;
import org.springframework.webflow.engine.builder.FlowAssembler;

/**
 * Test case for XML flow builder using namespaces.
 * 
 * @see org.springframework.webflow.engine.builder.xml.XmlFlowBuilder
 * 
 * @author Erwin Vervaet
 */
public class NamespaceXmlFlowBuilderTests extends TestCase {

	public void testBuildFlow() {
		XmlFlowBuilder builder = new XmlFlowBuilder(new ClassPathResource("nsFlow.xml",
				NamespaceXmlFlowBuilderTests.class), new TestFlowServiceLocator());
		new FlowAssembler("nsFlow", builder).assembleFlow();
	}

}