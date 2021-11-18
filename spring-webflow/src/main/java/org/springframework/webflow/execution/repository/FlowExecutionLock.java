/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.repository;

/**
 * A pessmistic lock to obtain exclusive rights to a flow execution. Used to prevent conflicts when multiple requests to
 * manipulate a flow execution arrive from different threads concurrently.
 * 
 * @author Keith Donald
 */
public interface FlowExecutionLock {

	/**
	 * Acquire the flow execution lock. This method will block until the lock becomes available for acquisition.
	 */
	public void lock();

	/**
	 * Release the flow execution lock.
	 */
	public void unlock();
}