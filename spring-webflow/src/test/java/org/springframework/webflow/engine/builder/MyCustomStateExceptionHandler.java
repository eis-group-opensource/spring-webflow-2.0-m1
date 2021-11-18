/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.builder;

import org.springframework.webflow.engine.FlowExecutionExceptionHandler;
import org.springframework.webflow.engine.RequestControlContext;
import org.springframework.webflow.execution.FlowExecutionException;
import org.springframework.webflow.execution.ViewSelection;

public class MyCustomStateExceptionHandler implements FlowExecutionExceptionHandler {

	public boolean handles(FlowExecutionException e) {
		return false;
	}

	public ViewSelection handle(FlowExecutionException e, RequestControlContext context) {
		return null;
	}

}
