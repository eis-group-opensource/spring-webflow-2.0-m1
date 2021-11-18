/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine;

import org.springframework.webflow.execution.FlowExecutionException;
import org.springframework.webflow.execution.ViewSelection;

/**
 * A strategy for handling an exception that occurs at runtime during the execution of a flow definition.
 * 
 * @author Keith Donald
 */
public interface FlowExecutionExceptionHandler {

	/**
	 * Can this handler handle the given exception?
	 * @param exception the exception that occured
	 * @return true if yes, false if no
	 */
	public boolean handles(FlowExecutionException exception);

	/**
	 * Handle the exception in the context of the current request, optionally making an error view selection that should
	 * be rendered.
	 * @param exception the exception that occured
	 * @param context the execution control context for this request
	 * @return the selected error view that should be displayed (may be null if the handler chooses not to select a
	 * view, in which case other exception handlers may be given a chance to handle the exception)
	 */
	public ViewSelection handle(FlowExecutionException exception, RequestControlContext context);
}
