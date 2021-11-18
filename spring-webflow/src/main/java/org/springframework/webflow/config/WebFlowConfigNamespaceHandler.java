/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.config;

import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * <code>NamespaceHandler</code> for the <code>webflow-config</code> namespace.
 * <p>
 * Provides {@link BeanDefinitionParser bean definition parsers} for the <code>&lt;executor&gt;</code> and
 * <code>&lt;registry&gt;</code> tags. An <code>executor</code> tag can include an <code>execution-listeners</code>
 * tag and a <code>registry</code> tag can include <code>location</code> tags.
 * <p>
 * Using the <code>executor</code> tag you can configure a {@link FlowExecutorFactoryBean} that creates a
 * {@link org.springframework.webflow.executor.FlowExecutor}. The <code>executor</code> tag allows you to specify the
 * repository type and a reference to a registry.
 * 
 * <pre class="code">
 *       &lt;flow:executor id=&quot;registry&quot; registry-ref=&quot;registry&quot; repository-type=&quot;continuation&quot; &gt;
 *           &lt;flow:execution-listeners&gt;
 *               &lt;flow:listener ref=&quot;listener1&quot; /&gt;
 *               &lt;flow:listener ref=&quot;listener2&quot; ref=&quot;*&quot; /&gt;
 *               &lt;flow:listener ref=&quot;listener3&quot; ref=&quot;flow1, flow2, flow3&quot; /&gt;
 *           &lt;flow:execution-listeners /&gt;
 *       &lt;/flow:executor&gt;
 * </pre>
 * 
 * <p>
 * Using the <code>registry</code> tag you can configure an
 * {@link org.springframework.webflow.engine.builder.xml.XmlFlowRegistryFactoryBean} to create a registry for use by any
 * number of <code>executor</code>s. The <code>registry</code> tag supports in-line flow definition locations.
 * 
 * <pre class="code">
 *       &lt;flow:registry id=&quot;registry&quot;&gt;
 *           &lt;flow:location path=&quot;/path/to/flow.xml&quot; /&gt;
 *           &lt;flow:location path=&quot;/path/with/wildcards/*-flow.xml&quot; /&gt;
 *       &lt;/flow:registry&gt;
 * </pre>
 * 
 * @author Ben Hale
 */
public class WebFlowConfigNamespaceHandler extends NamespaceHandlerSupport {

	public void init() {
		registerBeanDefinitionParser("execution-attributes", new ExecutionAttributesBeanDefinitionParser());
		registerBeanDefinitionParser("execution-listeners", new ExecutionListenersBeanDefinitionParser());
		registerBeanDefinitionParser("executor", new ExecutorBeanDefinitionParser());
		registerBeanDefinitionParser("registry", new RegistryBeanDefinitionParser());
		registerBeanDefinitionParser("enable-scopes", new EnableScopesBeanDefinitionParser());
	}
}