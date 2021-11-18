/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.builder;

import org.springframework.webflow.core.collection.AttributeMap;
import org.springframework.webflow.definition.registry.FlowDefinitionHolder;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.definition.registry.StaticFlowDefinitionHolder;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.engine.builder.AbstractFlowBuilder;
import org.springframework.webflow.engine.builder.AbstractFlowBuildingFlowRegistryFactoryBean;
import org.springframework.webflow.engine.builder.FlowAssembler;

/**
 * Base class for factory beans that create flow definition registries containing flows built using Java based
 * {@link AbstractFlowBuilder flow builders}.
 * <p>
 * Subclasses only need to define the {@link #doPopulate(FlowDefinitionRegistry)} method and use the
 * {@link #registerFlowDefinition(FlowDefinitionRegistry, String, AbstractFlowBuilder)} convenience methods provided by
 * this class to register all relevant flows:
 * 
 * <pre class="code">
 * public class MyFlowRegistryFactoryBean extends AbstractFlowBuilderFlowRegistryFactoryBean {
 * 	protected void doPopulate(FlowDefinitionRegistry registry) {
 * 		registerFlowDefinition(registry, &quot;my-flow&quot;, new MyFlowBuilder());
 * 		registerFlowDefinition(registry, &quot;my-other-flow&quot;, new MyOtherFlowBuilder());
 * 	}
 * }
 * </pre>
 * 
 * @see AbstractFlowBuilder
 * 
 * @since 1.0.2
 * 
 * @author Erwin Vervaet
 */
public abstract class AbstractFlowBuilderFlowRegistryFactoryBean extends AbstractFlowBuildingFlowRegistryFactoryBean {

	/**
	 * Register the flow built by given flow builder in specified flow definition registry.
	 * <p>
	 * Note that this method will set the {@link #getFlowServiceLocator() flow service locator} of this class on given
	 * flow builder.
	 * @param registry the registry to register the flow in
	 * @param flowId the flow id to assign
	 * @param flowBuilder the builder used to build the flow
	 */
	protected void registerFlowDefinition(FlowDefinitionRegistry registry, String flowId,
			AbstractFlowBuilder flowBuilder) {
		registerFlowDefinition(registry, flowId, null, flowBuilder);
	}

	/**
	 * Register the flow built by given flow builder in specified flow definition registry.
	 * <p>
	 * Note that this method will set the {@link #getFlowServiceLocator() flow service locator} of this class on given
	 * flow builder.
	 * @param registry the registry to register the flow in
	 * @param flowId the flow id to assign
	 * @param flowAttributes externally assigned flow attributes that can affect flow construction
	 * @param flowBuilder the builder used to build the flow
	 */
	protected void registerFlowDefinition(FlowDefinitionRegistry registry, String flowId, AttributeMap flowAttributes,
			AbstractFlowBuilder flowBuilder) {
		flowBuilder.setFlowServiceLocator(getFlowServiceLocator());
		Flow flow = new FlowAssembler(flowId, flowAttributes, flowBuilder).assembleFlow();
		FlowDefinitionHolder flowHolder = new StaticFlowDefinitionHolder(flow);
		registry.registerFlowDefinition(flowHolder);
	}
}
