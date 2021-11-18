/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.support;

import java.io.ObjectStreamException;

import org.springframework.webflow.engine.ViewState;
import org.springframework.webflow.execution.ViewSelection;

/**
 * Concrete response type that refreshes an application view by redirecting to an <i>existing</i>, active Spring Web
 * Flow execution at a unique SWF-specific <i>flow execution URL</i>. This enables the triggering of post-redirect-get
 * semantics from within an <i>active</i> flow execution.
 * <p>
 * Once the redirect response is issued a new request is initiated by the browser targeted at the flow execution URL.
 * The URL is stabally refreshable (and bookmarkable) while the conversation remains active, safely triggering a
 * {@link ViewState#refresh(org.springframework.webflow.execution.RequestContext)} on each access.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public final class FlowExecutionRedirect extends ViewSelection {

	/**
	 * The single instance of this class.
	 */
	public static final FlowExecutionRedirect INSTANCE = new FlowExecutionRedirect();

	/**
	 * Avoid instantiation.
	 */
	private FlowExecutionRedirect() {
	}

	// resolve the singleton instance
	private Object readResolve() throws ObjectStreamException {
		return INSTANCE;
	}

	public String toString() {
		return "redirect:";
	}
}