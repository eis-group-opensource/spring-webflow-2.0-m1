/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.support;

import java.io.Serializable;

import org.springframework.util.Assert;
import org.springframework.webflow.engine.TransitionCriteria;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * Simple transition criteria that matches on an eventId and nothing else. Specifically, if the id of the last event
 * that occurred equals {@link #getEventId()} this criteria will return true.
 * 
 * @see RequestContext#getLastEvent()
 * 
 * @author Erwin Vervaet
 * @author Keith Donald
 */
public class EventIdTransitionCriteria implements TransitionCriteria, Serializable {

	/**
	 * The event id to match.
	 */
	private String eventId;

	/**
	 * Whether or not to match case sensitively. Default is true.
	 */
	private boolean caseSensitive = true;

	/**
	 * Create a new event id matching criteria object.
	 * @param eventId the event id
	 */
	public EventIdTransitionCriteria(String eventId) {
		Assert.hasText(eventId, "The event id is required");
		this.eventId = eventId;
	}

	/**
	 * Returns the event id to match.
	 */
	public String getEventId() {
		return eventId;
	}

	/**
	 * Set whether or not the event id should be matched in a case sensitive manner. Defaults to true.
	 */
	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	public boolean test(RequestContext context) {
		Event lastEvent = context.getLastEvent();
		if (lastEvent == null) {
			return false;
		}
		if (caseSensitive) {
			return eventId.equals(lastEvent.getId());
		} else {
			return eventId.equalsIgnoreCase(lastEvent.getId());
		}
	}

	public String toString() {
		return "[eventId = '" + eventId + "']";
	}
}