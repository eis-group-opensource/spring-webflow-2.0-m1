/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.action;

import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.support.EventFactorySupport;

/**
 * Default implementation of the resultObject-to-event mapping interface. Always returns the "success" event.
 * 
 * @author Keith Donald
 */
public class SuccessEventFactory extends EventFactorySupport implements ResultEventFactory {

	public Event createResultEvent(Object source, Object resultObject, RequestContext context) {
		return success(source, resultObject);
	}
}