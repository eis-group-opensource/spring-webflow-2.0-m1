/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * A simple static helper that performs action execution that encapsulates common logging and exception handling logic.
 * This is an internal helper class that is not normally used by application code.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public class ActionExecutor {

	private static final Log logger = LogFactory.getLog(ActionExecutor.class);

	/**
	 * Private constructor to avoid instantiation.
	 */
	private ActionExecutor() {
	}

	/**
	 * Execute the given action.
	 * @param action the action to execute
	 * @param context the flow execution request context
	 * @return result of action execution
	 * @throws ActionExecutionException if the action threw an exception while executing, the orginal exception is
	 * available as the cause if this exception
	 */
	public static Event execute(Action action, RequestContext context) throws ActionExecutionException {
		try {
			if (logger.isDebugEnabled()) {
				if (context.getCurrentState() == null) {
					logger.debug("Executing start " + action + " for flow '" + context.getActiveFlow().getId() + "'");
				} else {
					logger.debug("Executing " + action + " in state '" + context.getCurrentState().getId()
							+ "' of flow '" + context.getActiveFlow().getId() + "'");
				}
			}
			return action.execute(context);
		} catch (ActionExecutionException e) {
			throw e;
		} catch (Exception e) {
			// wrap the exception as an ActionExecutionException
			throw new ActionExecutionException(context.getActiveFlow().getId(),
					context.getCurrentState() != null ? context.getCurrentState().getId() : null, action, context
							.getAttributes(), e);
		}
	}
}