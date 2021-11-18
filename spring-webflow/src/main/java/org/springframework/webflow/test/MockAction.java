/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.test;

import org.springframework.webflow.core.collection.AttributeMap;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.test.execution.AbstractExternalizedFlowExecutionTests;
import org.springframework.webflow.test.execution.AbstractXmlFlowExecutionTests;

/**
 * A trivial stub action implementation that can be parameterized to return a particular action execution result. Useful
 * for simulating different action results a flow should be capable of responding to.
 * 
 * Instances of this class can be conveniently installed in the bean factory that hosts the Web Flow action
 * implementations in a test environment by overriding <code>registerMockServices</code> on
 * {@link AbstractExternalizedFlowExecutionTests}. If you are using a XML-based flow definition with a flow-local
 * context to host your actions, consider overriding <code>registerLocalMockServices</code> on
 * {@link AbstractXmlFlowExecutionTests} to install mock instances.
 * 
 * @author Keith Donald
 */
public class MockAction implements Action {

	private String resultEventId;

	private AttributeMap resultAttributes;

	/**
	 * Constructs a new mock action that returns the default <code>success</code> execution result.
	 */
	public MockAction() {
		setResultEventId("success");
	}

	/**
	 * Constructs a new mock action that returns the provided execution result.
	 * @param resultEventId the execution result identifier that will be returned
	 */
	public MockAction(String resultEventId) {
		setResultEventId(resultEventId);
	}

	/**
	 * Sets the event identifier this mock action will use as its execution outcome.
	 * @param resultEventId the action execution result identifier
	 */
	public void setResultEventId(String resultEventId) {
		this.resultEventId = resultEventId;
	}

	/**
	 * Sets attributes to associate with a returned action execution outcome.
	 * @param resultAttributes the action execution result attributes
	 */
	public void setResultAttributes(AttributeMap resultAttributes) {
		this.resultAttributes = resultAttributes;
	}

	public Event execute(RequestContext context) {
		return new Event(this, resultEventId, resultAttributes);
	}

}
