/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.definition.registry;

/**
 * A strategy to use to populate a flow definition registry with one or more flow definitions.
 * <p>
 * Flow definition registrars encapsulate the knowledge about the source of a set of flow definition resources and the
 * behavior necessary to add those resources to a flow definition registry.
 * <p>
 * The typical usage pattern is as follows:
 * <ol>
 * <li>Create a new (initially empty) flow definition registry.
 * <li>Use any number of flow definition registrars to populate the registry by calling
 * {@link #registerFlowDefinitions(FlowDefinitionRegistry)}.
 * </ol>
 * <p>
 * This design where various registrars populate a generic registry was inspired by Spring's GenericApplicationContext,
 * which can use any number of BeanDefinitionReaders to drive context population.
 * 
 * @see FlowDefinitionRegistry
 * 
 * @author Keith Donald
 */
public interface FlowDefinitionRegistrar {

	/**
	 * Register flow definition resources managed by this registrar in the registry provided.
	 * @param registry the registry to register flow definitions in
	 */
	public void registerFlowDefinitions(FlowDefinitionRegistry registry);
}