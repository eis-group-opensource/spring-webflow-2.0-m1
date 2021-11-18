/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.repository.continuation;

import java.io.Serializable;

import org.springframework.webflow.execution.FlowExecution;

/**
 * A snapshot of a flow execution that can be restored from and serialized to a byte array.
 * 
 * @see FlowExecutionContinuationFactory
 * 
 * @author Erwin Vervaet
 * @author Keith Donald
 */
public abstract class FlowExecutionContinuation implements Serializable {

	/**
	 * Restores the flow execution wrapped in this continuation.
	 * @return the unmarshalled flow execution
	 * @throws ContinuationUnmarshalException when there is a problem unmarshalling this continuation
	 */
	public abstract FlowExecution unmarshal() throws ContinuationUnmarshalException;

	/**
	 * Converts this continuation to a byte array for convenient serialization.
	 * @return this as a byte array
	 */
	public abstract byte[] toByteArray();

}