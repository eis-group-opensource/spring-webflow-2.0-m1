/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.repository;

import java.io.Serializable;

/**
 * A key that uniquely identifies a flow execution in a managed {@link FlowExecutionRepository}. Serves as a flow
 * execution's persistent identity.
 * <p>
 * This class is abstract. The repository subsystem encapsulates the structure of concrete key implementations.
 * 
 * @author Keith Donald
 */
public abstract class FlowExecutionKey implements Serializable {

	/**
	 * Subclasses should override toString to return a parseable string form of the key.
	 * @see java.lang.Object#toString()
	 * @see FlowExecutionRepository#parseFlowExecutionKey(String)
	 */
	public abstract String toString();
}