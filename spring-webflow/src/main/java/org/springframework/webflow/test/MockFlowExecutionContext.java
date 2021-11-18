/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.test;

import org.springframework.webflow.core.collection.AttributeMap;
import org.springframework.webflow.core.collection.LocalAttributeMap;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.execution.FlowExecutionContext;
import org.springframework.webflow.execution.FlowSession;

/**
 * A stub implementation of the flow execution context interface.
 * 
 * @see FlowExecutionContext
 * 
 * @author Keith Donald
 */
public class MockFlowExecutionContext implements FlowExecutionContext {

	private FlowDefinition flow;

	private FlowSession activeSession;

	private MutableAttributeMap flashScope = new LocalAttributeMap();

	private MutableAttributeMap conversationScope = new LocalAttributeMap();

	private MutableAttributeMap attributes = new LocalAttributeMap();

	/**
	 * Creates a new mock flow execution context -- automatically installs a root flow definition and active flow
	 * session.
	 */
	public MockFlowExecutionContext() {
		activeSession = new MockFlowSession();
		this.flow = activeSession.getDefinition();
	}

	/**
	 * Creates a new mock flow execution context for the specified root flow definition.
	 */
	public MockFlowExecutionContext(Flow rootFlow) {
		this.flow = rootFlow;
		activeSession = new MockFlowSession(rootFlow);
	}

	public String getCaption() {
		return "Mock flow execution context";
	}

	// implementing flow execution context

	public FlowDefinition getDefinition() {
		return flow;
	}

	public boolean isActive() {
		return activeSession != null;
	}

	public FlowSession getActiveSession() throws IllegalStateException {
		if (activeSession == null) {
			throw new IllegalStateException("No flow session is active");
		}
		return activeSession;
	}

	public MutableAttributeMap getFlashScope() {
		return flashScope;
	}

	public MutableAttributeMap getConversationScope() {
		return conversationScope;
	}

	public AttributeMap getAttributes() {
		return attributes;
	}

	// mutators

	/**
	 * Sets the top-level flow definition.
	 */
	public void setFlow(Flow rootFlow) {
		this.flow = rootFlow;
	}

	/**
	 * Sets the mock session to be the <i>active session</i>.
	 */
	public void setActiveSession(FlowSession activeSession) {
		this.activeSession = activeSession;
	}

	/**
	 * Sets the flow execution flash scope.
	 */
	public void setFlashScope(MutableAttributeMap scope) {
		this.flashScope = scope;
	}

	/**
	 * Sets the flow execution conversation scope.
	 */
	public void setConversationScope(MutableAttributeMap scope) {
		this.conversationScope = scope;
	}

	// convenience accessors

	/**
	 * Returns the mock active flow session.
	 */
	public MockFlowSession getMockActiveSession() {
		return (MockFlowSession) activeSession;
	}

	/**
	 * Returns the mutable execution attribute map.
	 * @return the execution attribute map
	 */
	public MutableAttributeMap getAttributeMap() {
		return attributes;
	}

	/**
	 * Puts a execution attribute into the context.
	 * @param attributeName the attribute name
	 * @param attributeValue the attribute value
	 */
	public void putAttribute(String attributeName, Object attributeValue) {
		attributes.put(attributeName, attributeValue);
	}
}