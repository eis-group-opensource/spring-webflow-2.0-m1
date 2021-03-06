/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.style.ToStringCreator;
import org.springframework.util.Assert;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.definition.StateDefinition;
import org.springframework.webflow.execution.FlowExecutionException;
import org.springframework.webflow.execution.ViewSelection;

/**
 * A point in a flow where something happens. What happens is determined by a state's type. Standard types of states
 * include action states, view states, subflow states, and end states.
 * <p>
 * Each state is associated with exactly one owning flow definition. Specializations of this class capture all the
 * configuration information needed for a specific kind of state.
 * <p>
 * Subclasses should implement the <code>doEnter</code> method to execute the processing that should occur when this
 * state is entered, acting on its configuration information. The ability to plugin custom state types that execute
 * different behaviour polymorphically is the classic GoF state pattern.
 * <p>
 * Equality: Two states are equal if they have the same id and are part of the same flow.
 * 
 * @see org.springframework.webflow.engine.TransitionableState
 * @see org.springframework.webflow.engine.ActionState
 * @see org.springframework.webflow.engine.ViewState
 * @see org.springframework.webflow.engine.SubflowState
 * @see org.springframework.webflow.engine.EndState
 * @see org.springframework.webflow.engine.DecisionState
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public abstract class State extends AnnotatedObject implements StateDefinition {

	/**
	 * Logger, for use in subclasses.
	 */
	protected final Log logger = LogFactory.getLog(getClass());

	/**
	 * The state's owning flow.
	 */
	private Flow flow;

	/**
	 * The state identifier, unique to the owning flow.
	 */
	private String id;

	/**
	 * The list of actions to invoke when this state is entered.
	 */
	private ActionList entryActionList = new ActionList();

	/**
	 * The set of exception handlers for this state.
	 */
	private FlowExecutionExceptionHandlerSet exceptionHandlerSet = new FlowExecutionExceptionHandlerSet();

	/**
	 * Creates a state for the provided <code>flow</code> identified by the provided <code>id</code>. The id must
	 * be locally unique to the owning flow. The state will be automatically added to the flow.
	 * @param flow the owning flow
	 * @param id the state identifier (must be unique to the flow)
	 * @throws IllegalArgumentException if this state cannot be added to the flow, for instance when the provided id is
	 * not unique in the owning flow
	 * @see #getEntryActionList()
	 * @see #getExceptionHandlerSet()
	 */
	protected State(Flow flow, String id) throws IllegalArgumentException {
		setId(id);
		setFlow(flow);
	}

	// implementing StateDefinition

	public FlowDefinition getOwner() {
		return flow;
	}

	public String getId() {
		return id;
	}

	// implementation specific

	/**
	 * Returns the owning flow.
	 */
	public Flow getFlow() {
		return flow;
	}

	/**
	 * Set the owning flow.
	 * @throws IllegalArgumentException if this state cannot be added to the flow
	 */
	private void setFlow(Flow flow) throws IllegalArgumentException {
		Assert.hasText(getId(), "The id of the state should be set before adding the state to a flow");
		Assert.notNull(flow, "The owning flow is required");
		this.flow = flow;
		flow.add(this);
	}

	/**
	 * Set the state identifier, unique to the owning flow.
	 * @param id the state identifier
	 */
	private void setId(String id) {
		Assert.hasText(id, "This state must have a valid identifier");
		this.id = id;
	}

	/**
	 * Returns the list of actions executed by this state when it is entered. The returned list is mutable.
	 * @return the state entry action list
	 */
	public ActionList getEntryActionList() {
		return entryActionList;
	}

	/**
	 * Returns a mutable set of exception handlers, allowing manipulation of how exceptions are handled when thrown
	 * within this state.
	 * <p>
	 * Exception handlers are invoked when an exception occurs when this state is entered, and can execute custom
	 * exception handling logic as well as select an error view to display.
	 * @return the state exception handler set
	 */
	public FlowExecutionExceptionHandlerSet getExceptionHandlerSet() {
		return exceptionHandlerSet;
	}

	/**
	 * Returns a flag indicating if this state is the start state of its owning flow.
	 * @return true if the flow is the start state, false otherwise
	 */
	public boolean isStartState() {
		return flow.getStartState() == this;
	}

	// id and flow based equality

	public boolean equals(Object o) {
		if (!(o instanceof State)) {
			return false;
		}
		State other = (State) o;
		return id.equals(other.id) && flow.equals(other.flow);
	}

	public int hashCode() {
		return id.hashCode() + flow.hashCode();
	}

	// behavioral methods

	/**
	 * Enter this state in the provided flow control context. This implementation just calls the
	 * {@link #doEnter(RequestControlContext)} hook method, which should be implemented by subclasses, after executing
	 * the entry actions.
	 * @param context the control context for the currently executing flow, used by this state to manipulate the flow
	 * execution
	 * @return a view selection containing model and view information needed to render the results of the state
	 * processing
	 * @throws FlowExecutionException if an exception occurs in this state
	 */
	public final ViewSelection enter(RequestControlContext context) throws FlowExecutionException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entering state '" + getId() + "' of flow '" + getFlow().getId() + "'");
		}
		context.setCurrentState(this);
		entryActionList.execute(context);
		return doEnter(context);
	}

	/**
	 * Hook method to execute custom behaviour as a result of entering this state. By implementing this method
	 * subclasses specialize the behaviour of the state.
	 * @param context the control context for the currently executing flow, used by this state to manipulate the flow
	 * execution
	 * @return a view selection containing model and view information needed to render the results of the state
	 * processing
	 * @throws FlowExecutionException if an exception occurs in this state
	 */
	protected abstract ViewSelection doEnter(RequestControlContext context) throws FlowExecutionException;

	/**
	 * Handle an exception that occured in this state during the context of the current flow execution request.
	 * @param exception the exception that occured
	 * @param context the flow execution control context
	 * @return the selected error view, or <code>null</code> if no handler matched or returned a non-null view
	 * selection
	 */
	public ViewSelection handleException(FlowExecutionException exception, RequestControlContext context) {
		return getExceptionHandlerSet().handleException(exception, context);
	}

	public String toString() {
		String flowName = (flow == null ? "<not set>" : flow.getId());
		ToStringCreator creator = new ToStringCreator(this).append("id", getId()).append("flow", flowName).append(
				"entryActionList", entryActionList).append("exceptionHandlerSet", exceptionHandlerSet);
		appendToString(creator);
		return creator.toString();
	}

	/**
	 * Subclasses may override this hook method to stringify their internal state. This default implementation does
	 * nothing.
	 * @param creator the toString creator, to stringify properties
	 */
	protected void appendToString(ToStringCreator creator) {
	}
}