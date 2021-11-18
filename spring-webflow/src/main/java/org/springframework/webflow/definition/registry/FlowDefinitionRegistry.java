/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.definition.registry;

/**
 * A container of flow definitions. Extends the {@link FlowDefinitionRegistryMBean} management interface exposing
 * registry monitoring and management operations. Also extends {@link FlowDefinitionLocator} for accessing registered
 * Flow definitions for execution at runtime.
 * <p>
 * Flow definition registries can be configured with a "parent" registry to provide a hook into a larger flow definition
 * registry hierarchy.
 * 
 * @author Keith Donald
 * @author Ben Hale
 */
public interface FlowDefinitionRegistry extends FlowDefinitionLocator, FlowDefinitionRegistryMBean {

	/**
	 * Sets this registry's parent registry. When asked by a client to locate a flow definition this registry will query
	 * it's parent if it cannot fulfill the lookup request itself.
	 * @param parent the parent flow definition registry, may be null
	 */
	public void setParent(FlowDefinitionRegistry parent);

	/**
	 * Register a flow definition in this registry. Registers a "holder", not the Flow definition itself. This allows
	 * the actual Flow definition to be loaded lazily only when needed, and also rebuilt at runtime when its underlying
	 * resource changes without re-deploy.
	 * @param flowHolder a holder holding the flow definition to register
	 */
	public void registerFlowDefinition(FlowDefinitionHolder flowHolder);

	/**
	 * Register a flow definition in this registry under a specific namespace. Registers a "holder", not the Flow
	 * definition itself. This allows the actual Flow definition to be loaded lazily only when needed, and also rebuilt
	 * at runtime when its underlying resource changes without re-deploy.
	 * @param flowHolder a holder holding the flow definition to register
	 * @param namespace the namespace to register the flow definition in
	 */
	public void registerFlowDefinition(FlowDefinitionHolder flowHolder, String namespace);

}