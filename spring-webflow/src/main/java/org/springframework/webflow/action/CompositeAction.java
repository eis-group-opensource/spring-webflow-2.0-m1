/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.style.ToStringCreator;
import org.springframework.util.Assert;
import org.springframework.webflow.core.collection.LocalAttributeMap;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * An action that will execute an ordered chain of other actions when executed.
 * <p>
 * The event id of the last not-null result returned by the executed actions will be used as the result event id of the
 * composite action. Lacking that, the action will return the "success" event.
 * <p>
 * The resulting event will have an "actionResults" event attribute with a list of all events returned by the executed
 * actions, including the null events. This allows you to relate an executed action and its result event by their index
 * in the list.
 * <p>
 * This is the classic GoF composite design pattern.
 * 
 * @author Keith Donald
 */
public class CompositeAction extends AbstractAction {

	/**
	 * The resulting event whill have an attribute of this name which holds a list of all events returned by the
	 * executed actions. ("actionResults")
	 */
	public static final String ACTION_RESULTS_ATTRIBUTE_NAME = "actionResults";

	/**
	 * The actions to execute.
	 */
	private Action[] actions;

	/**
	 * Should execution stop if one action returns an error event?
	 */
	private boolean stopOnError;

	/**
	 * Create a composite action composed of given actions.
	 * @param actions the actions
	 */
	public CompositeAction(Action[] actions) {
		Assert.notEmpty(actions, "At least one action is required");
		this.actions = actions;
	}

	/**
	 * Returns the actions contained by this composite action.
	 * @return the actions
	 */
	protected Action[] getActions() {
		return actions;
	}

	/**
	 * Returns the stop on error flag.
	 */
	public boolean isStopOnError() {
		return stopOnError;
	}

	/**
	 * Sets the stop on error flag. This determines whether or not execution should stop with the first action that
	 * returns an error event. In the error case, the composite action will also return the "error" event.
	 */
	public void setStopOnError(boolean stopOnError) {
		this.stopOnError = stopOnError;
	}

	public Event doExecute(RequestContext context) throws Exception {
		Action[] actions = getActions();
		String eventId = getEventFactorySupport().getSuccessEventId();
		MutableAttributeMap eventAttributes = new LocalAttributeMap();
		List actionResults = new ArrayList(actions.length);
		for (int i = 0; i < actions.length; i++) {
			Event result = actions[i].execute(context);
			actionResults.add(result);
			if (result != null) {
				eventId = result.getId();
				if (isStopOnError() && result.getId().equals(getEventFactorySupport().getErrorEventId())) {
					break;
				}
			}
		}
		eventAttributes.put(ACTION_RESULTS_ATTRIBUTE_NAME, actionResults);
		return new Event(this, eventId, eventAttributes);
	}

	public String toString() {
		return new ToStringCreator(this).append("actions", getActions()).append("stopOnError", isStopOnError())
				.toString();
	}
}