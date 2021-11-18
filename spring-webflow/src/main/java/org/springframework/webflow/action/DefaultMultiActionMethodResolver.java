/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.action;

import org.springframework.webflow.action.MultiAction.MethodResolver;
import org.springframework.webflow.execution.RequestContext;

/**
 * Default method resolver used by the MultiAction class. It uses the following algorithm to calculate a method name:
 * <ol>
 * <li>If the currently executing action has a "method" property defined, use the value as method name.</li>
 * <li>Else use the name of the current state of the flow execution as a method name.</li>
 * </ol>
 * 
 * @see org.springframework.webflow.action.MultiAction
 * 
 * @author Erwin Vervaet
 */
public class DefaultMultiActionMethodResolver implements MethodResolver {

	public String resolveMethod(RequestContext context) {
		// implementation note: not using AnnotatedAction.METHOD_ATTRIBUTE since
		// that resides in the engine subsystem
		String method = context.getAttributes().getString("method");
		if (method == null) {
			if (context.getCurrentState() != null) {
				// default to the state id
				method = context.getCurrentState().getId();
			} else {
				throw new IllegalStateException("Unable to resolve action method; no 'method' context attribute set");
			}
		}
		return method;
	}
}