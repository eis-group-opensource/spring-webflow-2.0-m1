/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.builder;

import org.springframework.binding.mapping.AttributeMapper;
import org.springframework.webflow.core.collection.AttributeMap;
import org.springframework.webflow.engine.ActionState;
import org.springframework.webflow.engine.DecisionState;
import org.springframework.webflow.engine.EndState;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.engine.FlowAttributeMapper;
import org.springframework.webflow.engine.FlowExecutionExceptionHandler;
import org.springframework.webflow.engine.State;
import org.springframework.webflow.engine.SubflowState;
import org.springframework.webflow.engine.TargetStateResolver;
import org.springframework.webflow.engine.Transition;
import org.springframework.webflow.engine.TransitionCriteria;
import org.springframework.webflow.engine.TransitionableState;
import org.springframework.webflow.engine.ViewSelector;
import org.springframework.webflow.engine.ViewState;
import org.springframework.webflow.execution.Action;

/**
 * A factory for core web flow elements such as {@link Flow flows}, {@link State states}, and
 * {@link Transition transitions}.
 * <p>
 * This factory encapsulates the construction of each Flow implementation as well as each core artifact type. Subclasses
 * may customize how the core elements are created, useful for plugging in custom implementations.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public class FlowArtifactFactory {

	/**
	 * Factory method that creates a new {@link Flow} definition object.
	 * <p>
	 * Note this method does not return a fully configured Flow instance, it only encapsulates the selection of
	 * implementation. A {@link FlowAssembler} delegating to a calling {@link FlowBuilder} is expected to assemble the
	 * Flow fully before returning it to external clients.
	 * @param id the flow identifier, should be unique to all flows in an application (required)
	 * @param attributes attributes to assign to the Flow, which may also be used to affect flow construction; may be
	 * null
	 * @return the initial flow instance, ready for assembly by a FlowBuilder
	 * @throws FlowArtifactLookupException an exception occured creating the Flow instance
	 */
	public Flow createFlow(String id, AttributeMap attributes) throws FlowArtifactLookupException {
		Flow flow = new Flow(id);
		flow.getAttributeMap().putAll(attributes);
		return flow;
	}

	/**
	 * Factory method that creates a new view state, a state where a user is allowed to participate in the flow. This
	 * method is an atomic operation that returns a fully initialized state. It encapsulates the selection of the view
	 * state implementation as well as the state assembly.
	 * @param id the identifier to assign to the state, must be unique to its owning flow (required)
	 * @param flow the flow that will own (contain) this state (required)
	 * @param entryActions any state entry actions; may be null
	 * @param viewSelector the state view selector strategy; may be null
	 * @param renderActions any 'render actions' to execute on entry and refresh; may be null
	 * @param transitions any transitions (paths) out of this state; may be null
	 * @param exceptionHandlers any exception handlers; may be null
	 * @param exitActions any state exit actions; may be null
	 * @param attributes attributes to assign to the State, which may also be used to affect state construction; may be
	 * null
	 * @return the fully initialized view state instance
	 * @throws FlowArtifactLookupException an exception occured creating the state
	 */
	public State createViewState(String id, Flow flow, Action[] entryActions, ViewSelector viewSelector,
			Action[] renderActions, Transition[] transitions, FlowExecutionExceptionHandler[] exceptionHandlers,
			Action[] exitActions, AttributeMap attributes) throws FlowArtifactLookupException {
		ViewState viewState = new ViewState(flow, id);
		if (viewSelector != null) {
			viewState.setViewSelector(viewSelector);
		}
		viewState.getRenderActionList().addAll(renderActions);
		configureCommonProperties(viewState, entryActions, transitions, exceptionHandlers, exitActions, attributes);
		return viewState;
	}

	/**
	 * Factory method that creates a new action state, a state where a system action is executed. This method is an
	 * atomic operation that returns a fully initialized state. It encapsulates the selection of the action state
	 * implementation as well as the state assembly.
	 * @param id the identifier to assign to the state, must be unique to its owning flow (required)
	 * @param flow the flow that will own (contain) this state (required)
	 * @param entryActions any state entry actions; may be null
	 * @param actions the actions to execute when the state is entered (required)
	 * @param transitions any transitions (paths) out of this state; may be null
	 * @param exceptionHandlers any exception handlers; may be null
	 * @param exitActions any state exit actions; may be null
	 * @param attributes attributes to assign to the State, which may also be used to affect state construction; may be
	 * null
	 * @return the fully initialized action state instance
	 * @throws FlowArtifactLookupException an exception occured creating the state
	 */
	public State createActionState(String id, Flow flow, Action[] entryActions, Action[] actions,
			Transition[] transitions, FlowExecutionExceptionHandler[] exceptionHandlers, Action[] exitActions,
			AttributeMap attributes) throws FlowArtifactLookupException {
		ActionState actionState = new ActionState(flow, id);
		actionState.getActionList().addAll(actions);
		configureCommonProperties(actionState, entryActions, transitions, exceptionHandlers, exitActions, attributes);
		return actionState;
	}

	/**
	 * Factory method that creates a new decision state, a state where a flow routing decision is made. This method is
	 * an atomic operation that returns a fully initialized state. It encapsulates the selection of the decision state
	 * implementation as well as the state assembly.
	 * @param id the identifier to assign to the state, must be unique to its owning flow (required)
	 * @param flow the flow that will own (contain) this state (required)
	 * @param entryActions any state entry actions; may be null
	 * @param transitions any transitions (paths) out of this state
	 * @param exceptionHandlers any exception handlers; may be null
	 * @param exitActions any state exit actions; may be null
	 * @param attributes attributes to assign to the State, which may also be used to affect state construction; may be
	 * null
	 * @return the fully initialized decision state instance
	 * @throws FlowArtifactLookupException an exception occured creating the state
	 */
	public State createDecisionState(String id, Flow flow, Action[] entryActions, Transition[] transitions,
			FlowExecutionExceptionHandler[] exceptionHandlers, Action[] exitActions, AttributeMap attributes)
			throws FlowArtifactLookupException {
		DecisionState decisionState = new DecisionState(flow, id);
		configureCommonProperties(decisionState, entryActions, transitions, exceptionHandlers, exitActions, attributes);
		return decisionState;
	}

	/**
	 * Factory method that creates a new subflow state, a state where a parent flow spawns another flow as a subflow.
	 * This method is an atomic operation that returns a fully initialized state. It encapsulates the selection of the
	 * subflow state implementation as well as the state assembly.
	 * @param id the identifier to assign to the state, must be unique to its owning flow (required)
	 * @param flow the flow that will own (contain) this state (required)
	 * @param entryActions any state entry actions; may be null
	 * @param subflow the subflow definition (required)
	 * @param attributeMapper the subflow input and output attribute mapper; may be null
	 * @param transitions any transitions (paths) out of this state
	 * @param exceptionHandlers any exception handlers; may be null
	 * @param exitActions any state exit actions; may be null
	 * @param attributes attributes to assign to the State, which may also be used to affect state construction; may be
	 * null
	 * @return the fully initialized subflow state instance
	 * @throws FlowArtifactLookupException an exception occured creating the state
	 */
	public State createSubflowState(String id, Flow flow, Action[] entryActions, Flow subflow,
			FlowAttributeMapper attributeMapper, Transition[] transitions,
			FlowExecutionExceptionHandler[] exceptionHandlers, Action[] exitActions, AttributeMap attributes)
			throws FlowArtifactLookupException {
		SubflowState subflowState = new SubflowState(flow, id, subflow);
		if (attributeMapper != null) {
			subflowState.setAttributeMapper(attributeMapper);
		}
		configureCommonProperties(subflowState, entryActions, transitions, exceptionHandlers, exitActions, attributes);
		return subflowState;
	}

	/**
	 * Factory method that creates a new end state, a state where an executing flow session terminates. This method is
	 * an atomic operation that returns a fully initialized state. It encapsulates the selection of the end state
	 * implementation as well as the state assembly.
	 * @param id the identifier to assign to the state, must be unique to its owning flow (required)
	 * @param flow the flow that will own (contain) this state (required)
	 * @param entryActions any state entry actions; may be null
	 * @param viewSelector the state confirmation view selector strategy; may be null
	 * @param outputMapper the state output mapper; may be null
	 * @param exceptionHandlers any exception handlers; may be null
	 * @param attributes attributes to assign to the State, which may also be used to affect state construction; may be
	 * null
	 * @return the fully initialized subflow state instance
	 * @throws FlowArtifactLookupException an exception occured creating the state
	 */
	public State createEndState(String id, Flow flow, Action[] entryActions, ViewSelector viewSelector,
			AttributeMapper outputMapper, FlowExecutionExceptionHandler[] exceptionHandlers, AttributeMap attributes)
			throws FlowArtifactLookupException {
		EndState endState = new EndState(flow, id);
		if (viewSelector != null) {
			endState.setViewSelector(viewSelector);
		}
		if (outputMapper != null) {
			endState.setOutputMapper(outputMapper);
		}
		configureCommonProperties(endState, entryActions, exceptionHandlers, attributes);
		return endState;
	}

	/**
	 * Factory method that creates a new transition, a path from one step in a flow to another. This method is an atomic
	 * operation that returns a fully initialized transition. It encapsulates the selection of the transition
	 * implementation as well as the transition assembly.
	 * @param targetStateResolver the resolver of the target state of the transition (required)
	 * @param matchingCriteria the criteria that matches the transition; may be null
	 * @param executionCriteria the criteria that governs execution of the transition after match; may be null
	 * @param attributes attributes to assign to the transition, which may also be used to affect transition
	 * construction; may be null
	 * @return the fully initialized transition instance
	 * @throws FlowArtifactLookupException an exception occured creating the transition
	 */
	public Transition createTransition(TargetStateResolver targetStateResolver, TransitionCriteria matchingCriteria,
			TransitionCriteria executionCriteria, AttributeMap attributes) throws FlowArtifactLookupException {
		Transition transition = new Transition(targetStateResolver);
		if (matchingCriteria != null) {
			transition.setMatchingCriteria(matchingCriteria);
		}
		if (executionCriteria != null) {
			transition.setExecutionCriteria(executionCriteria);
		}
		transition.getAttributeMap().putAll(attributes);
		return transition;
	}

	// internal helpers

	/**
	 * Configure common properties for a transitionable state.
	 */
	private void configureCommonProperties(TransitionableState state, Action[] entryActions, Transition[] transitions,
			FlowExecutionExceptionHandler[] exceptionHandlers, Action[] exitActions, AttributeMap attributes) {
		configureCommonProperties(state, entryActions, exceptionHandlers, attributes);
		state.getTransitionSet().addAll(transitions);
		state.getExitActionList().addAll(exitActions);
	}

	/**
	 * Configure common properties for a state.
	 */
	private void configureCommonProperties(State state, Action[] entryActions,
			FlowExecutionExceptionHandler[] exceptionHandlers, AttributeMap attributes) {
		state.getEntryActionList().addAll(entryActions);
		state.getExceptionHandlerSet().addAll(exceptionHandlers);
		state.getAttributeMap().putAll(attributes);
	}
}