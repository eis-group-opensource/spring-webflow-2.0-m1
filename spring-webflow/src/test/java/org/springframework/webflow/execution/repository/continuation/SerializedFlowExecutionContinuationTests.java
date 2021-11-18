/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.repository.continuation;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

import junit.framework.TestCase;

import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.engine.SimpleFlow;
import org.springframework.webflow.engine.impl.FlowExecutionImplFactory;
import org.springframework.webflow.execution.FlowExecution;
import org.springframework.webflow.test.MockExternalContext;

/**
 * Unit tests for {@link SerializedFlowExecutionContinuation}.
 * 
 * @author Keith Donald
 */
public class SerializedFlowExecutionContinuationTests extends TestCase {

	public void testCreate() throws Exception {
		FlowDefinition flow = new SimpleFlow();
		FlowExecution execution = new FlowExecutionImplFactory().createFlowExecution(flow);
		execution.start(null, new MockExternalContext());
		SerializedFlowExecutionContinuation c = new SerializedFlowExecutionContinuation(execution, true);
		assertTrue(c.isCompressed());
		byte[] array = c.toByteArray();
		execution = c.unmarshal();

		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(array));
		try {
			c = (SerializedFlowExecutionContinuation) ois.readObject();
			assertTrue(c.isCompressed());
			execution = c.unmarshal();
		} finally {
			ois.close();
		}
	}

}
