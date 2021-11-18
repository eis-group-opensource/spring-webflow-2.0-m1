/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine;

import java.io.ObjectStreamException;
import java.io.Serializable;

import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.ViewSelection;

/**
 * Makes a null view selection, indicating no response should be issued.
 * 
 * @see org.springframework.webflow.execution.ViewSelection#NULL_VIEW
 * 
 * @author Keith Donald
 */
public final class NullViewSelector implements ViewSelector, Serializable {

	/*
	 * Implementation note: not located in webflow.execution.support package to avoid a cyclic dependency between
	 * webflow.execution and webflow.execution.support.
	 */

	/**
	 * The shared singleton {@link NullViewSelector} instance.
	 */
	public static final ViewSelector INSTANCE = new NullViewSelector();

	/**
	 * Private constructor since this is a singleton.
	 */
	private NullViewSelector() {
	}

	public boolean isEntrySelectionRenderable(RequestContext context) {
		return true;
	}

	public ViewSelection makeEntrySelection(RequestContext context) {
		return ViewSelection.NULL_VIEW;
	}

	public ViewSelection makeRefreshSelection(RequestContext context) {
		return makeEntrySelection(context);
	}

	// resolve the singleton instance
	private Object readResolve() throws ObjectStreamException {
		return INSTANCE;
	}

}