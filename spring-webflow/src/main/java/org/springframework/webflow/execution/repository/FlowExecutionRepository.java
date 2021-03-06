/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.repository;

import org.springframework.webflow.execution.FlowExecution;

/**
 * Central subsystem interface responsible for the saving and restoring of flow executions, where each flow execution
 * represents a state of an active flow definition.
 * <p>
 * Flow execution repositories are responsible for managing the storage, restoration and removal of flow executions
 * launched by clients of the Spring Web Flow system.
 * <p>
 * When placed in a repository a {@link FlowExecution} object representing the state of a flow at a point in time is
 * indexed under a unique {@link FlowExecutionKey}.
 * 
 * @see FlowExecution
 * @see FlowExecutionKey
 * 
 * @author Erwin Vervaet
 * @author Keith Donald
 */
public interface FlowExecutionRepository {

	/**
	 * Generate a unique flow execution key to be used as the persistent identifier of the flow execution. This method
	 * should be called after a new flow execution is started and remains active; thus needing to be saved. The
	 * FlowExecutionKey is the execution's persistent identity.
	 * @param flowExecution the flow execution
	 * @return the flow execution key
	 * @throws FlowExecutionRepositoryException a problem occurred generating the key
	 */
	public FlowExecutionKey generateKey(FlowExecution flowExecution) throws FlowExecutionRepositoryException;

	/**
	 * Obtain the "next" flow execution key to be used as the flow execution's persistent identity. This method should
	 * be called after a existing flow execution has resumed and remains active; thus needing to be updated. This
	 * repository may choose to return the previous key or generate a new key.
	 * @param flowExecution the flow execution
	 * @param previousKey the <i>current</i> key associated with the flow execution
	 * @throws FlowExecutionRepositoryException a problem occurred generating the key
	 */
	public FlowExecutionKey getNextKey(FlowExecution flowExecution, FlowExecutionKey previousKey)
			throws FlowExecutionRepositoryException;

	/**
	 * Return the lock for the flow execution, allowing for the lock to be acquired or released.
	 * <p>
	 * Caution: care should be made not to allow for a deadlock situation. If you acquire a lock make sure you release
	 * it when you are done.
	 * <p>
	 * The general pattern for safely doing work against a locked conversation follows:
	 * 
	 * <pre>
	 * FlowExecutionLock lock = repository.getLock(key);
	 * lock.lock();
	 * try {
	 * 	FlowExecution execution = repository.getFlowExecution(key);
	 * 	// do work
	 * } finally {
	 * 	lock.unlock();
	 * }
	 * </pre>
	 * 
	 * @param key the identifier of the flow execution to lock
	 * @return the lock
	 * @throws FlowExecutionRepositoryException a problem occurred accessing the lock object
	 */
	public FlowExecutionLock getLock(FlowExecutionKey key) throws FlowExecutionRepositoryException;

	/**
	 * Return the <code>FlowExecution</code> indexed by the provided key. The returned flow execution represents the
	 * restored state of an executing flow from a point in time. This should be called to resume a persistent flow
	 * execution.
	 * <p>
	 * Before calling this method, you should acquire the lock for the keyed flow execution.
	 * @param key the flow execution key
	 * @return the flow execution, fully hydrated and ready to signal an event against
	 * @throws FlowExecutionRepositoryException if no flow execution was indexed with the key provided
	 */
	public FlowExecution getFlowExecution(FlowExecutionKey key) throws FlowExecutionRepositoryException;

	/**
	 * Place the <code>FlowExecution</code> in this repository under the provided key. This should be called to save
	 * or update the persistent state of an active (but paused) flow execution.
	 * <p>
	 * Before calling this method, you should acquire the lock for the keyed flow execution.
	 * @param key the flow execution key
	 * @param flowExecution the flow execution
	 * @throws FlowExecutionRepositoryException the flow execution could not be stored
	 */
	public void putFlowExecution(FlowExecutionKey key, FlowExecution flowExecution)
			throws FlowExecutionRepositoryException;

	/**
	 * Remove the flow execution from the repository. This should be called when the flow execution ends (is no longer
	 * active).
	 * <p>
	 * Before calling this method, you should acquire the lock for the keyed flow execution.
	 * @param key the flow execution key
	 * @throws FlowExecutionRepositoryException the flow execution could not be removed.
	 */
	public void removeFlowExecution(FlowExecutionKey key) throws FlowExecutionRepositoryException;

	/**
	 * Parse the string-encoded flow execution key into its object form. Essentially, the reverse of
	 * {@link FlowExecutionKey#toString()}.
	 * @param encodedKey the string encoded key
	 * @return the parsed flow execution key, the persistent identifier for exactly one flow execution
	 */
	public FlowExecutionKey parseFlowExecutionKey(String encodedKey) throws FlowExecutionRepositoryException;

}