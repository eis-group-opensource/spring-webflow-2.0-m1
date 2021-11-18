/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.impl;

import java.util.ListIterator;
import java.util.Map;

import org.springframework.util.Assert;
import org.springframework.webflow.core.collection.AttributeMap;
import org.springframework.webflow.core.collection.CollectionUtils;
import org.springframework.webflow.core.collection.LocalAttributeMap;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.definition.registry.FlowDefinitionLocator;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.execution.FlowExecution;
import org.springframework.webflow.execution.factory.FlowExecutionListenerLoader;
import org.springframework.webflow.execution.factory.StaticFlowExecutionListenerLoader;
import org.springframework.webflow.execution.repository.support.FlowExecutionStateRestorer;

/**
 * Restores the transient state of deserialized {@link FlowExecutionImpl} objects.
 * 
 * @author Keith Donald
 */
public class FlowExecutionImplStateRestorer implements FlowExecutionStateRestorer {

	/**
	 * Used to restore the flow execution's flow definition.
	 */
	private FlowDefinitionLocator definitionLocator;

	/**
	 * Used to restore the flow execution's listeners.
	 */
	private FlowExecutionListenerLoader executionListenerLoader = StaticFlowExecutionListenerLoader.EMPTY_INSTANCE;

	/**
	 * Used to restore the flow execution's system attributes.
	 */
	private AttributeMap executionAttributes = CollectionUtils.EMPTY_ATTRIBUTE_MAP;

	/**
	 * Creates a new execution transient state restorer.
	 * @param definitionLocator the flow definition locator
	 */
	public FlowExecutionImplStateRestorer(FlowDefinitionLocator definitionLocator) {
		Assert.notNull(definitionLocator, "The flow definition locator is required");
		this.definitionLocator = definitionLocator;
	}

	/**
	 * Sets the attributes to apply to restored flow executions. Execution attributes may affect flow execution
	 * behavior.
	 * @param executionAttributes flow execution system attributes
	 */
	public void setExecutionAttributes(AttributeMap executionAttributes) {
		Assert.notNull(executionAttributes, "The execution attributes map is required");
		this.executionAttributes = executionAttributes;
	}

	/**
	 * Sets the attributes to apply to restored flow executions. Execution attributes may affect flow execution
	 * behavior.
	 * <p>
	 * Convenience setter that takes a simple <code>java.util.Map</code> to ease bean style configuration.
	 * @param executionAttributes flow execution system attributes
	 */
	public void setExecutionAttributesMap(Map executionAttributes) {
		Assert.notNull(executionAttributes, "The execution attributes map is required");
		this.executionAttributes = new LocalAttributeMap(executionAttributes);
	}

	/**
	 * Sets the strategy for loading listeners that should observe executions of a flow definition. Allows full control
	 * over what listeners should apply. for executions of a flow definition.
	 */
	public void setExecutionListenerLoader(FlowExecutionListenerLoader executionListenerLoader) {
		Assert.notNull(executionListenerLoader, "The listener loader is required");
		this.executionListenerLoader = executionListenerLoader;
	}

	public FlowExecution restoreState(FlowExecution flowExecution, MutableAttributeMap conversationScope) {
		FlowExecutionImpl impl = (FlowExecutionImpl) flowExecution;
		// the root flow should be a top-level flow visible by the flow def locator
		Flow flow = (Flow) definitionLocator.getFlowDefinition(impl.getFlowId());
		impl.setFlow(flow);
		if (impl.hasSessions()) {
			FlowSessionImpl root = impl.getRootSession();
			root.setFlow(flow);
			root.setState(flow.getStateInstance(root.getStateId()));
			if (impl.hasSubflowSessions()) {
				Flow parent = flow;
				for (ListIterator it = impl.getSubflowSessionIterator(); it.hasNext();) {
					FlowSessionImpl subflow = (FlowSessionImpl) it.next();
					Flow definition;
					if (parent.containsInlineFlow(subflow.getFlowId())) {
						// subflow is an inline flow of it's parent
						definition = parent.getInlineFlow(subflow.getFlowId());
					} else {
						// subflow is a top-level flow
						definition = (Flow) definitionLocator.getFlowDefinition(subflow.getFlowId());
					}
					subflow.setFlow(definition);
					subflow.setState(definition.getStateInstance(subflow.getStateId()));
					parent = definition;
				}
			}
		}
		if (conversationScope == null) {
			conversationScope = new LocalAttributeMap();
		}
		impl.setConversationScope(conversationScope);
		impl.setListeners(new FlowExecutionListeners(executionListenerLoader.getListeners(flow)));
		impl.setAttributes(executionAttributes);
		return flowExecution;
	}
}