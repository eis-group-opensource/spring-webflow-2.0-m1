/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine;

import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.ViewSelection;

/**
 * Factory that produces a new, configured {@link ViewSelection} object on each invocation, taking into account the
 * information in the provided flow execution request context.
 * <p>
 * Note: this class is a runtime factory. Instances are used at flow execution time by objects like the
 * {@link ViewState} to produce new {@link ViewSelection view selections}.
 * <p>
 * This class allows for easy insertion of dynamic view selection logic, for instance, letting you determine the view to
 * render or the available model data for rendering based on contextual information.
 * 
 * @see org.springframework.webflow.execution.ViewSelection
 * @see org.springframework.webflow.engine.ViewState
 * @see org.springframework.webflow.engine.EndState
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public interface ViewSelector {

	/**
	 * Will the primary selection returned by 'makeEntrySelection' for the given request context be renderable in this
	 * request?
	 * <p>
	 * "Renderable" view selections typically can have 'render-actions' execute before they are created. An example
	 * would be an ApplicationView that forwards to a view template like a JSP. "Non-renderable" view selections are
	 * things like a flow execution redirect--no render actually occurs, but only a redirect--rendering happens on the
	 * new redirect request.
	 * @param context the current request context of the executing flow
	 * @return true if yes, false otherwise
	 */
	public boolean isEntrySelectionRenderable(RequestContext context);

	/**
	 * Make a new "entry" view selection for the given request context. Called when a view-state, end-state, or other
	 * interactive state type is entered.
	 * @param context the current request context of the executing flow
	 * @return the entry view selection
	 */
	public ViewSelection makeEntrySelection(RequestContext context);

	/**
	 * Reconstitute a renderable view selection for the given request context to support a ViewState 'refresh'
	 * operation.
	 * @param context the current request context of the executing flow
	 * @return the view selection
	 */
	public ViewSelection makeRefreshSelection(RequestContext context);

}