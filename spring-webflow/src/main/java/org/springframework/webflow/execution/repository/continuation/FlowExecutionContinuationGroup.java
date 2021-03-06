/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.repository.continuation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * A group of flow execution continuations. Simple typed data structure backed by a map and linked list. Supports
 * expelling the oldest continuation once a maximum group size is met.
 * 
 * @author Keith Donald
 */
class FlowExecutionContinuationGroup implements Serializable {

	/**
	 * A map of continuations; the key is the continuation id, and the value is the {@link FlowExecutionContinuation}
	 * object.
	 */
	private Map continuations = new HashMap();

	/**
	 * An ordered list of continuation ids. Each continuation id represents an pointer to a continuation in the map. The
	 * first element is the oldest continuation and the last is the youngest.
	 */
	private LinkedList continuationIds = new LinkedList();

	/**
	 * The maximum number of continuations allowed in this group.
	 */
	private int maxContinuations = -1;

	/**
	 * Creates a new flow execution continuation group.
	 * @param maxContinuations the maximum number of continuations that can be stored in this group, -1 for unlimited
	 */
	public FlowExecutionContinuationGroup(int maxContinuations) {
		this.maxContinuations = maxContinuations;
	}

	/**
	 * Returns the count of continuations in this group.
	 */
	public int getContinuationCount() {
		return continuationIds.size();
	}

	/**
	 * Returns the continuation with the provided <code>id</code>, or <code>null</code> if no such continuation
	 * exists with that id.
	 * @param id the continuation id
	 * @return the continuation
	 * @throws ContinuationNotFoundException if the id does not match a continuation in this group
	 */
	public FlowExecutionContinuation get(Serializable id) throws ContinuationNotFoundException {
		FlowExecutionContinuation continuation = (FlowExecutionContinuation) continuations.get(id);
		if (continuation == null) {
			throw new ContinuationNotFoundException(id);
		}
		return continuation;
	}

	/**
	 * Add a flow execution continuation with given id to this group.
	 * @param continuationId the continuation id
	 * @param continuation the continuation
	 */
	public void add(Serializable continuationId, FlowExecutionContinuation continuation) {
		continuations.put(continuationId, continuation);
		if (continuationIds.contains(continuationId)) {
			continuationIds.remove(continuationId);
		}
		continuationIds.add(continuationId);
		// remove the oldest continuation if them maximium number of
		// continuations has been exceeded
		if (maxExceeded()) {
			removeOldestContinuation();
		}
	}

	/**
	 * Has the maximum number of allowed continuations in this group been exceeded?
	 */
	private boolean maxExceeded() {
		return maxContinuations > 0 && continuationIds.size() > maxContinuations;
	}

	/**
	 * Remove the olders continuation from this group.
	 */
	private void removeOldestContinuation() {
		continuations.remove(continuationIds.removeFirst());
	}
}