/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.definition;

/**
 * A step within a {@link FlowDefinition flow definition} where behavior is executed.
 * <p>
 * States have identifiers that are local to their containing flow definitions. They may also be annotated with
 * attributes.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public interface StateDefinition extends Annotated {

	/**
	 * Returns the flow definition this state belongs to.
	 * @return the owning flow definition
	 */
	public FlowDefinition getOwner();

	/**
	 * Returns this state's identifier, locally unique to is containing flow definition.
	 * @return the state identifier
	 */
	public String getId();
}