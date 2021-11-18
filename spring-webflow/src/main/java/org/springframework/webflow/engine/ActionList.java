/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.springframework.core.style.StylerUtils;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.RequestContext;

/**
 * An ordered, typed list of actions, mainly for use internally by flow artifacts that can execute groups of actions.
 * 
 * @see Flow#getStartActionList()
 * @see Flow#getEndActionList()
 * @see State#getEntryActionList()
 * @see ActionState#getActionList()
 * @see TransitionableState#getExitActionList()
 * @see ViewState#getRenderActionList()
 * 
 * @author Keith Donald
 */
public class ActionList {

	/**
	 * The lists of actions.
	 */
	private List actions = new LinkedList();

	/**
	 * Add an action to this list.
	 * @param action the action to add
	 * @return true if this list's contents changed as a result of the add operation
	 */
	public boolean add(Action action) {
		return actions.add(action);
	}

	/**
	 * Add a collection of actions to this list.
	 * @param actions the actions to add
	 * @return true if this list's contents changed as a result of the add operation
	 */
	public boolean addAll(Action[] actions) {
		if (actions == null) {
			return false;
		}
		return this.actions.addAll(Arrays.asList(actions));
	}

	/**
	 * Tests if the action is in this list.
	 * @param action the action
	 * @return true if the action is contained in this list, false otherwise
	 */
	public boolean contains(Action action) {
		return actions.contains(action);
	}

	/**
	 * Remove the action instance from this list.
	 * @param action the action to add
	 * @return true if this list's contents changed as a result of the remove operation
	 */
	public boolean remove(Action action) {
		return actions.remove(action);
	}

	/**
	 * Returns the size of this action list.
	 * @return the action list size.
	 */
	public int size() {
		return actions.size();
	}

	/**
	 * Returns the action in this list at the provided index.
	 * @param index the action index
	 * @return the action the action
	 */
	public Action get(int index) throws IndexOutOfBoundsException {
		return (Action) actions.get(index);
	}

	/**
	 * Returns the action in this list at the provided index, exposing it as an annotated action. This allows clients to
	 * access specific properties about a target action instance if they exist.
	 * @return the action, as an annotated action
	 */
	public AnnotatedAction getAnnotated(int index) throws IndexOutOfBoundsException {
		Action action = get(index);
		if (action instanceof AnnotatedAction) {
			return (AnnotatedAction) action;
		} else {
			// wrap the action; no annotations will be available
			return new AnnotatedAction(action);
		}
	}

	/**
	 * Returns an iterator over this action list.
	 */
	public Iterator iterator() {
		return actions.iterator();
	}

	/**
	 * Convert this list to a typed action array.
	 * @return the action list, as a typed array
	 */
	public Action[] toArray() {
		return (Action[]) actions.toArray(new Action[actions.size()]);
	}

	/**
	 * Returns the list of actions in this list as a typed annotated action array. This is a convenience method allowing
	 * clients to access properties about an action if they exist.
	 * @return the annotated action list, as a typed array
	 */
	public AnnotatedAction[] toAnnotatedArray() {
		AnnotatedAction[] annotatedActions = new AnnotatedAction[actions.size()];
		for (int i = 0; i < size(); i++) {
			annotatedActions[i] = getAnnotated(i);
		}
		return annotatedActions;
	}

	/**
	 * Executes the actions contained within this action list. Simply iterates over each action and calls execute.
	 * Action result events are ignored.
	 * @param context the action execution request context
	 */
	public void execute(RequestContext context) {
		Iterator it = actions.iterator();
		while (it.hasNext()) {
			ActionExecutor.execute((Action) it.next(), context);
		}
	}

	public String toString() {
		return StylerUtils.style(actions);
	}
}