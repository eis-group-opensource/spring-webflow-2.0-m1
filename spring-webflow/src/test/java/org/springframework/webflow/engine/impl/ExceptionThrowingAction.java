/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.impl;

import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * Test action that throws an exception.
 * 
 * @author Erwin Vervaet
 */
public class ExceptionThrowingAction extends AbstractAction {

	protected Event doExecute(RequestContext context) throws Exception {
		Class exceptionType = Class.forName(context.getAttributes().getString("exceptionType"));
		throw (Exception) exceptionType.newInstance();
	}

}
