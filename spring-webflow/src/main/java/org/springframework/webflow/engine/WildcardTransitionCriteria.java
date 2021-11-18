/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine;

import java.io.ObjectStreamException;
import java.io.Serializable;

import org.springframework.webflow.execution.RequestContext;

/**
 * Transition criteria that always returns true.
 * 
 * @author Keith Donald
 */
public class WildcardTransitionCriteria implements TransitionCriteria, Serializable {

	/*
	 * Implementation note: not located in webflow.execution.support package to avoid a cyclic dependency between
	 * webflow.execution and webflow.execution.support.
	 */

	/**
	 * Event id value ("*") that will cause the transition to match on any event.
	 */
	public static final String WILDCARD_EVENT_ID = "*";

	/**
	 * Shared instance of a TransitionCriteria that always returns true.
	 */
	public static final WildcardTransitionCriteria INSTANCE = new WildcardTransitionCriteria();

	/**
	 * Private constructor because this is a singleton.
	 */
	private WildcardTransitionCriteria() {
	}

	public boolean test(RequestContext context) {
		return true;
	}

	// resolve the singleton instance
	private Object readResolve() throws ObjectStreamException {
		return INSTANCE;
	}

	public String toString() {
		return WILDCARD_EVENT_ID;
	}
}