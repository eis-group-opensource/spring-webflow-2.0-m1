/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.repository.continuation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.springframework.webflow.execution.FlowExecution;

/**
 * A factory that creates new instances of flow execution continuations based on standard Java serialization.
 * 
 * @see SerializedFlowExecutionContinuation
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public class SerializedFlowExecutionContinuationFactory implements FlowExecutionContinuationFactory {

	/**
	 * Flag to toggle continuation compression; compression is on by default.
	 */
	private boolean compress = true;

	/**
	 * Returns whether or not the continuations should be compressed.
	 */
	public boolean getCompress() {
		return compress;
	}

	/**
	 * Set whether or not the continuations should be compressed.
	 */
	public void setCompress(boolean compress) {
		this.compress = compress;
	}

	public FlowExecutionContinuation createContinuation(FlowExecution flowExecution)
			throws ContinuationCreationException {
		return new SerializedFlowExecutionContinuation(flowExecution, compress);
	}

	public FlowExecutionContinuation createContinuation(byte[] bytes) throws ContinuationUnmarshalException {
		try {
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
			try {
				return (FlowExecutionContinuation) ois.readObject();
			} finally {
				ois.close();
			}
		} catch (IOException e) {
			throw new ContinuationUnmarshalException("IO problem while creating a flow execution continuation", e);
		} catch (ClassNotFoundException e) {
			throw new ContinuationUnmarshalException("Class not found while creating a flow execution continuation", e);
		}
	}
}