/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.action;

import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * A strategy for creating an {@link Event} object from an arbitrary object such as an expression evaluation result or
 * bean method return value.
 * 
 * @author Keith Donald
 */
public interface ResultEventFactory {

	/**
	 * Create an event instance from the result object.
	 * @param source the source of the event
	 * @param resultObject the result object, typically the return value of a bean method
	 * @param context a flow execution request context
	 * @return the event
	 */
	public Event createResultEvent(Object source, Object resultObject, RequestContext context);
}