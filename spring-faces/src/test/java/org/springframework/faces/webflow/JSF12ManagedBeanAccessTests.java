/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.faces.webflow;

import org.jboss.el.ExpressionFactoryImpl;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.faces.el.Jsf12ELExpressionParser;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.webflow.test.MockFlowServiceLocator;

public class JSF12ManagedBeanAccessTests extends JSF11ManagedBeanAccessTests {

	protected void registerMockServices(MockFlowServiceLocator serviceRegistry) {
		serviceRegistry.setExpressionParser(new Jsf12ELExpressionParser(new ExpressionFactoryImpl()));
		serviceRegistry.registerBean("serviceBean", service);

		ctx = new GenericWebApplicationContext();
		XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(ctx);
		xmlReader.loadBeanDefinitions(new ClassPathResource("org/springframework/faces/webflow/jsf-flow-beans.xml"));
		ctx.refresh();

		jsf.externalContext().getApplicationMap().put(
				GenericWebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, ctx);
	}

}
