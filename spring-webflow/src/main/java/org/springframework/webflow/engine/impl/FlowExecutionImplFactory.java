/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.impl;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.webflow.core.collection.AttributeMap;
import org.springframework.webflow.core.collection.CollectionUtils;
import org.springframework.webflow.core.collection.LocalAttributeMap;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.execution.FlowExecution;
import org.springframework.webflow.execution.FlowExecutionFactory;
import org.springframework.webflow.execution.FlowExecutionListener;
import org.springframework.webflow.execution.factory.FlowExecutionListenerLoader;
import org.springframework.webflow.execution.factory.StaticFlowExecutionListenerLoader;

/**
 * A factory for instances of the {@link FlowExecutionImpl default flow execution} implementation.
 * 
 * @author Keith Donald
 */
public class FlowExecutionImplFactory implements FlowExecutionFactory {

	private static final Log logger = LogFactory.getLog(FlowExecutionImplFactory.class);

	/**
	 * The strategy for loading listeners that should observe executions of a flow definition. The default simply loads
	 * an empty static listener list.
	 */
	private FlowExecutionListenerLoader executionListenerLoader = StaticFlowExecutionListenerLoader.EMPTY_INSTANCE;

	/**
	 * System execution attributes that may influence flow execution behavior. The default is an empty map.
	 */
	private AttributeMap executionAttributes = CollectionUtils.EMPTY_ATTRIBUTE_MAP;

	/**
	 * Returns the attributes to apply to flow executions created by this factory. Execution attributes may affect flow
	 * execution behavior.
	 * @return flow execution attributes
	 */
	public AttributeMap getExecutionAttributes() {
		return executionAttributes;
	}

	/**
	 * Sets the attributes to apply to flow executions created by this factory. Execution attributes may affect flow
	 * execution behavior.
	 * @param executionAttributes flow execution system attributes
	 */
	public void setExecutionAttributes(AttributeMap executionAttributes) {
		Assert.notNull(executionAttributes, "The execution attributes map is required");
		this.executionAttributes = executionAttributes;
	}

	/**
	 * Sets the attributes to apply to flow executions created by this factory. Execution attributes may affect flow
	 * execution behavior.
	 * <p>
	 * Convenience setter that takes a simple <code>java.util.Map</code> to ease bean style configuration.
	 * @param executionAttributes flow execution system attributes
	 */
	public void setExecutionAttributesMap(Map executionAttributes) {
		Assert.notNull(executionAttributes, "The execution attributes map is required");
		this.executionAttributes = new LocalAttributeMap(executionAttributes);
	}

	/**
	 * Returns the strategy for loading listeners that should observe executions of a flow definition. Allows full
	 * control over what listeners should apply for executions of a flow definition.
	 */
	public FlowExecutionListenerLoader getExecutionListenerLoader() {
		return executionListenerLoader;
	}

	/**
	 * Sets the strategy for loading listeners that should observe executions of a flow definition. Allows full control
	 * over what listeners should apply for executions of a flow definition.
	 */
	public void setExecutionListenerLoader(FlowExecutionListenerLoader listenerLoader) {
		Assert.notNull(listenerLoader, "The listener loader is required");
		this.executionListenerLoader = listenerLoader;
	}

	public FlowExecution createFlowExecution(FlowDefinition flowDefinition) {
		Assert.isInstanceOf(Flow.class, flowDefinition, "Flow definition is of wrong type: ");
		if (logger.isDebugEnabled()) {
			logger.debug("Creating flow execution for flow definition with id '" + flowDefinition.getId() + "'");
		}
		FlowExecutionListener[] listeners = executionListenerLoader.getListeners(flowDefinition);
		return new FlowExecutionImpl((Flow) flowDefinition, listeners, executionAttributes);
	}
}