/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.springframework.core.style.StylerUtils;
import org.springframework.webflow.core.collection.CollectionUtils;
import org.springframework.webflow.execution.RequestContext;

/**
 * A typed set of transitions for use internally by artifacts that can apply transition execution logic.
 * 
 * @see TransitionableState#getTransitionSet()
 * @see Flow#getGlobalTransitionSet()
 * 
 * @author Keith Donald
 */
public class TransitionSet {

	/**
	 * The set of transitions.
	 */
	private List transitions = new LinkedList();

	/**
	 * Add a transition to this set.
	 * @param transition the transition to add
	 * @return true if this set's contents changed as a result of the add operation
	 */
	public boolean add(Transition transition) {
		if (contains(transition)) {
			return false;
		}
		return transitions.add(transition);
	}

	/**
	 * Add a collection of transition instances to this set.
	 * @param transitions the transitions to add
	 * @return true if this set's contents changed as a result of the add operation
	 */
	public boolean addAll(Transition[] transitions) {
		return CollectionUtils.addAllNoDuplicates(this.transitions, transitions);
	}

	/**
	 * Tests if this transition is in this set.
	 * @param transition the transition
	 * @return true if the transition is contained in this set, false otherwise
	 */
	public boolean contains(Transition transition) {
		return transitions.contains(transition);
	}

	/**
	 * Remove the transition instance from this set.
	 * @param transition the transition to remove
	 * @return true if this list's contents changed as a result of the remove operation
	 */
	public boolean remove(Transition transition) {
		return transitions.remove(transition);
	}

	/**
	 * Returns the size of this transition set.
	 * @return the exception handler set size
	 */
	public int size() {
		return transitions.size();
	}

	/**
	 * Convert this set to a typed transition array.
	 * @return the transition set as a typed array
	 */
	public Transition[] toArray() {
		return (Transition[]) transitions.toArray(new Transition[transitions.size()]);
	}

	/**
	 * Returns a list of the supported transitional criteria used to match transitions in this state.
	 * @return the list of transitional criteria
	 */
	public TransitionCriteria[] getTransitionCriterias() {
		TransitionCriteria[] criterias = new TransitionCriteria[transitions.size()];
		int i = 0;
		Iterator it = transitions.iterator();
		while (it.hasNext()) {
			criterias[i++] = ((Transition) it.next()).getMatchingCriteria();
		}
		return criterias;
	}

	/**
	 * Gets a transition for given flow execution request context. The first matching transition will be returned.
	 * @param context a flow execution context
	 * @return the transition, or null if no transition matches
	 */
	public Transition getTransition(RequestContext context) {
		Iterator it = transitions.iterator();
		while (it.hasNext()) {
			Transition transition = (Transition) it.next();
			if (transition.matches(context)) {
				return transition;
			}
		}
		return null;
	}

	/**
	 * Returns whether or not this list has a transition that will fire for given flow execution request context.
	 * @param context a flow execution context
	 */
	public boolean hasMatchingTransition(RequestContext context) {
		return getTransition(context) != null;
	}

	public String toString() {
		return StylerUtils.style(transitions);
	}
}