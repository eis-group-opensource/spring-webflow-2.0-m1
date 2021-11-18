/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.config;

import junit.framework.TestCase;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.webflow.executor.mvc.FlowController;

/**
 * Test case that illustrates configuration of a FlowController and its associated artefacts using classic spring bean
 * configuration information. This test case does not really test much but serves more as an example.
 * 
 * @author Erwin Vervaet
 */
public class BeanConfigTests extends TestCase {

	private BeanFactory beanFactory;

	protected void setUp() throws Exception {
		beanFactory = new ClassPathXmlApplicationContext("webflow-config-classic.xml", BeanConfigTests.class);
	}

	public void testFlowControllerConfig() {
		FlowController flowController = (FlowController) beanFactory.getBean("flowController");
		assertEquals("test-flow", flowController.getArgumentHandler().getDefaultFlowId());
	}

	public void testFlowControllerBeanConfig() {
		FlowController flowController = (FlowController) beanFactory.getBean("flowController-bean");
		assertEquals("test-flow", flowController.getArgumentHandler().getDefaultFlowId());
	}
}
