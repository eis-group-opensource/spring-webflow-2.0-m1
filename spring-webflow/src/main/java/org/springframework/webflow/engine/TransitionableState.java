/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine;

import org.springframework.core.style.StylerUtils;
import org.springframework.core.style.ToStringCreator;
import org.springframework.webflow.definition.TransitionDefinition;
import org.springframework.webflow.definition.TransitionableStateDefinition;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.ViewSelection;

/**
 * Abstract superclass for states that can execute a transition in response to an event.
 * 
 * @see org.springframework.webflow.engine.Transition
 * @see org.springframework.webflow.engine.TransitionCriteria
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public abstract class TransitionableState extends State implements TransitionableStateDefinition {

	/**
	 * The set of possible transitions out of this state.
	 */
	private TransitionSet transitions = new TransitionSet();

	/**
	 * An actions to execute when exiting this state.
	 */
	private ActionList exitActionList = new ActionList();

	/**
	 * Create a new transitionable state.
	 * @param flow the owning flow
	 * @param id the state identifier (must be unique to the flow)
	 * @throws IllegalArgumentException when this state cannot be added to given flow, for instance when the id is not
	 * unique
	 * @see State#State(Flow, String)
	 * @see #getTransitionSet()
	 */
	protected TransitionableState(Flow flow, String id) throws IllegalArgumentException {
		super(flow, id);
	}

	// implementing TranstionableStateDefinition

	public TransitionDefinition[] getTransitions() {
		return getTransitionSet().toArray();
	}

	/**
	 * Returns the set of transitions. The returned set is mutable.
	 */
	public TransitionSet getTransitionSet() {
		return transitions;
	}

	/**
	 * Get a transition in this state for given flow execution request context. Throws and exception when there is no
	 * corresponding transition.
	 * @throws NoMatchingTransitionException when a matching transition cannot be found
	 */
	public Transition getRequiredTransition(RequestContext context) throws NoMatchingTransitionException {
		Transition transition = getTransitionSet().getTransition(context);
		if (transition == null) {
			throw new NoMatchingTransitionException(getFlow().getId(), getId(), context.getLastEvent(),
					"No transition found on occurence of event '" + context.getLastEvent() + "' in state '" + getId()
							+ "' of flow '" + getFlow().getId() + "' -- valid transitional criteria are "
							+ StylerUtils.style(getTransitionSet().getTransitionCriterias())
							+ " -- likely programmer error, check the set of TransitionCriteria for this state");
		}
		return transition;
	}

	/**
	 * Returns the list of actions executed by this state when it is exited. The returned list is mutable.
	 * @return the state exit action list
	 */
	public ActionList getExitActionList() {
		return exitActionList;
	}

	// behavioral methods

	/**
	 * Inform this state definition that an event was signaled in it. The signaled event is the last event available in
	 * given request context ({@link RequestContext#getLastEvent()}).
	 * @param context the flow execution control context
	 * @return the selected view
	 * @throws NoMatchingTransitionException when a matching transition cannot be found
	 */
	public ViewSelection onEvent(RequestControlContext context) throws NoMatchingTransitionException {
		return getRequiredTransition(context).execute(this, context);
	}

	/**
	 * Re-enter this state. This is typically called when a transition out of this state is selected, but transition
	 * execution rolls back and as a result the flow reenters the source state.
	 * <p>
	 * By default, this just calls <code>enter()</code>.
	 * @param context the flow control context in an executing flow (a client instance of a flow)
	 * @return a view selection containing model and view information needed to render the results of the state
	 * processing
	 */
	public ViewSelection reenter(RequestControlContext context) {
		return enter(context);
	}

	/**
	 * Exit this state. This is typically called when a transition takes the flow out of this state into another state.
	 * By default just executes any registered exit actions.
	 * @param context the flow control context
	 */
	public void exit(RequestControlContext context) {
		exitActionList.execute(context);
	}

	protected void appendToString(ToStringCreator creator) {
		creator.append("transitions", transitions).append("exitActionList", exitActionList);
	}
}