/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.definition;

/**
 * A transition takes a flow from one state to another.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public interface TransitionDefinition extends Annotated {

	/**
	 * The identifier of this transition. This id value should be unique among all other transitions in a set.
	 * @return the transition identifier
	 */
	public String getId();

	/**
	 * Returns an identification of the target state of this transition. This could be an actual static state id or
	 * something more dynamic, like a string representation of an expression evaluating the target state id at flow
	 * execution time.
	 * @return the target state identifier
	 */
	public String getTargetStateId();
}