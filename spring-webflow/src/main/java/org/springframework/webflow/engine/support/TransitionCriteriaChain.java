/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.support;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.springframework.core.style.ToStringCreator;
import org.springframework.webflow.engine.AnnotatedAction;
import org.springframework.webflow.engine.TransitionCriteria;
import org.springframework.webflow.engine.WildcardTransitionCriteria;
import org.springframework.webflow.execution.RequestContext;

/**
 * An ordered chain of <code>TransitionCriteria</code>. Iterates over each element in the chain, continues until one
 * returns false or the list is exhausted. So in effect it will do a logical AND between the contained criteria.
 * 
 * @author Keith Donald
 */
public class TransitionCriteriaChain implements TransitionCriteria {

	/**
	 * The ordered chain of TransitionCriteria objects.
	 */
	private List criteriaChain = new LinkedList();

	/**
	 * Creates an initially empty transition criteria chain.
	 * @see #add(TransitionCriteria)
	 */
	public TransitionCriteriaChain() {
	}

	/**
	 * Creates a transition criteria chain with the specified criteria.
	 * @param criteria the criteria
	 */
	public TransitionCriteriaChain(TransitionCriteria[] criteria) {
		criteriaChain.addAll(Arrays.asList(criteria));
	}

	/**
	 * Add given criteria object to the end of the chain.
	 * @param criteria the criteria
	 * @return this object, so multiple criteria can be added in a single statement
	 */
	public TransitionCriteriaChain add(TransitionCriteria criteria) {
		this.criteriaChain.add(criteria);
		return this;
	}

	public boolean test(RequestContext context) {
		Iterator it = criteriaChain.iterator();
		while (it.hasNext()) {
			TransitionCriteria criteria = (TransitionCriteria) it.next();
			if (!criteria.test(context)) {
				return false;
			}
		}
		return true;
	}

	public String toString() {
		return new ToStringCreator(this).append("criteriaChain", criteriaChain).toString();
	}

	// static helpers

	/**
	 * Create a transition criteria chain chaining given list of actions.
	 * @param actions the actions (and their execution properties) to chain together
	 */
	public static TransitionCriteria criteriaChainFor(AnnotatedAction[] actions) {
		if (actions == null || actions.length == 0) {
			return WildcardTransitionCriteria.INSTANCE;
		}
		TransitionCriteriaChain chain = new TransitionCriteriaChain();
		for (int i = 0; i < actions.length; i++) {
			chain.add(new ActionTransitionCriteria(actions[i]));
		}
		return chain;
	}
}