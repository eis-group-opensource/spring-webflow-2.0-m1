/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.faces.webflow.el;

import java.util.Map;

import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.PropertyResolver;

import org.springframework.webflow.execution.FlowExecution;

/**
 * Custom property resolver that resolves supported properties of the current flow execution. Supports resolving all
 * scopes as java.util.Maps: "flowScope", "conversationScope", and "flashScope". Also supports attribute searching when
 * no scope prefix is specified. The search order is flash, flow, conversation.
 * 
 * @author Keith Donald
 */
public class FlowExecutionPropertyResolver extends AbstractFlowExecutionPropertyResolver {

	/**
	 * The name of the special flash scope execution property.
	 */
	private static final String FLASH_SCOPE_PROPERTY = "flashScope";

	/**
	 * The name of the special flow scope execution property.
	 */
	private static final String FLOW_SCOPE_PROPERTY = "flowScope";

	/**
	 * The name of the special conversation scope execution property.
	 */
	private static final String CONVERSATION_SCOPE_PROPERTY = "conversationScope";

	/**
	 * Creates a new flow executon property resolver that resolves flash, flow, and conversation scope attributes.
	 * @param resolverDelegate the resolver to delegate to when the property is not a flow execution attribute
	 */
	public FlowExecutionPropertyResolver(PropertyResolver resolverDelegate) {
		super(resolverDelegate);
	}

	protected Class doGetAttributeType(FlowExecution execution, String attributeName) {
		if (FLASH_SCOPE_PROPERTY.equals(attributeName)) {
			return Map.class;
		} else if (FLOW_SCOPE_PROPERTY.equals(attributeName)) {
			return Map.class;
		} else if (CONVERSATION_SCOPE_PROPERTY.equals(attributeName)) {
			return Map.class;
		} else {
			// perform an attribute search

			// try flash scope first
			Object value = execution.getFlashScope().get(attributeName);
			if (value != null) {
				return value.getClass();
			}
			// try flow scope
			value = execution.getActiveSession().getScope().get(attributeName);
			if (value != null) {
				return value.getClass();
			}
			// try conversation scope
			value = execution.getConversationScope().get(attributeName);
			if (value != null) {
				return value.getClass();
			}
			// cannot determine
			return null;
		}
	}

	protected Object doGetAttribute(FlowExecution execution, String attributeName) {
		if (FLASH_SCOPE_PROPERTY.equals(attributeName)) {
			return execution.getFlashScope().asMap();
		} else if (FLOW_SCOPE_PROPERTY.equals(attributeName)) {
			return execution.getActiveSession().getScope().asMap();
		} else if (CONVERSATION_SCOPE_PROPERTY.equals(attributeName)) {
			return execution.getConversationScope().asMap();
		} else {
			// perform an attribute search

			// try flash scope
			Object value = execution.getFlashScope().get(attributeName);
			if (value != null) {
				return value;
			}
			// try flow scope
			value = execution.getActiveSession().getScope().get(attributeName);
			if (value != null) {
				return value;
			}
			// try conversation scope
			value = execution.getConversationScope().get(attributeName);
			if (value != null) {
				return value;
			}
			// cannot resolve as expected
			throw new PropertyNotFoundException("Readable flow execution attribute '" + attributeName
					+ "' not found in any scope (flash, flow, or conversation)");
		}
	}

	protected void doSetAttribute(FlowExecution execution, String attributeName, Object attributeValue) {
		// perform a search
		if (execution.getFlashScope().contains(attributeName)) {
			execution.getFlashScope().put(attributeName, attributeValue);
		} else if (execution.getActiveSession().getScope().contains(attributeName)) {
			execution.getActiveSession().getScope().put(attributeName, attributeValue);
		} else if (execution.getConversationScope().contains(attributeName)) {
			execution.getConversationScope().put(attributeName, attributeValue);
		} else {
			// cannot resolve as expected
			throw new PropertyNotFoundException("Settable flow execution attribute '" + attributeName
					+ "' not found in any scope (flash, flow, or conversation)");
		}
	}
}