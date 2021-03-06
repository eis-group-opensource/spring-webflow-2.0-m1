/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.definition;

/**
 * The definition of a flow, a program that when executed carries out the orchestration of a task on behalf of a single
 * client.
 * <p>
 * A flow definition is a reusable, self-contained controller module that defines a blue print for an executable user
 * task. Flows typically orchestrate controlled navigations or dialogs within web applications to guide users through
 * fulfillment of a business process/goal that takes place over a series of steps, modeled as states.
 * <p>
 * Structurally a flow definition is composed of a set of states. A {@link StateDefinition state} is a point in a flow
 * where a behavior is executed; for example, showing a view, executing an action, spawning a subflow, or terminating
 * the flow. Different types of states execute different behaviors in a polymorphic fashion. Most states are
 * {@link TransitionableStateDefinition transitionable states}, meaning they can respond to events by taking the flow
 * from one state to another.
 * <p>
 * Each flow has exactly one {@link #getStartState() start state} which defines the starting point of the program.
 * <p>
 * This interface exposes the flow's identifier, states, and other definitional attributes. It is suitable for
 * introspection by tools as well as user-code at flow execution time.
 * <p>
 * Flow definitions may be annotated with attributes.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public interface FlowDefinition extends Annotated {

	/**
	 * Returns the unique id of this flow.
	 * @return the flow id
	 */
	public String getId();

	/**
	 * Return this flow's starting point.
	 * @return the start state
	 */
	public StateDefinition getStartState();

	/**
	 * Returns the state definition with the specified id.
	 * @param id the state id
	 * @return the state definition
	 * @throws IllegalArgumentException if a state with this id does not exist
	 */
	public StateDefinition getState(String id) throws IllegalArgumentException;
}