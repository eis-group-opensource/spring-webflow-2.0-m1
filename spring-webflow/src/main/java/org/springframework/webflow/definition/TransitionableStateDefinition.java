/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.definition;

/**
 * A state that can transition to another state.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public interface TransitionableStateDefinition extends StateDefinition {

	/**
	 * Returns the available transitions out of this state.
	 * @return the available state transitions
	 */
	public TransitionDefinition[] getTransitions();
}