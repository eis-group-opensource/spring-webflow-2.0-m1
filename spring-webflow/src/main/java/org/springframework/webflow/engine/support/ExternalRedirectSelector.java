/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.support;

import java.io.Serializable;

import org.springframework.binding.expression.Expression;
import org.springframework.core.style.ToStringCreator;
import org.springframework.webflow.engine.ViewSelector;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.ViewSelection;
import org.springframework.webflow.execution.support.ExternalRedirect;

/**
 * Makes view selections requesting a client side redirect to an <i>external</i> URL outside of the flow.
 * <p>
 * This selector is usefull when you wish to request a <i>redirect after conversation completion</i> as part of
 * entering an EndState.
 * <p>
 * This selector may also be used to redirect to an external URL from a ViewState of an active conversation. The
 * external system redirected to will be provided the flow execution context necessary to allow it to communicate back
 * to the executing flow at a later time.
 * 
 * @see org.springframework.webflow.execution.support.ExternalRedirect
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public class ExternalRedirectSelector implements ViewSelector, Serializable {

	/**
	 * The parsed, evaluatable redirect URL expression.
	 */
	private Expression urlExpression;

	/**
	 * Create a new redirecting view selector that takes given URL expression as input. The expression is the parsed
	 * form (expression-tokenized) of the encoded view (e.g. "/pathInfo?param0=value0&param1=value1").
	 * @param urlExpression the url expression
	 */
	public ExternalRedirectSelector(Expression urlExpression) {
		this.urlExpression = urlExpression;
	}

	/**
	 * Returns the expression used by this view selector.
	 */
	public Expression getUrlExpression() {
		return urlExpression;
	}

	public boolean isEntrySelectionRenderable(RequestContext context) {
		return true;
	}

	public ViewSelection makeEntrySelection(RequestContext context) {
		String url = (String) urlExpression.evaluate(context, null);
		return new ExternalRedirect(url);
	}

	public ViewSelection makeRefreshSelection(RequestContext context) {
		return makeEntrySelection(context);
	}

	public String toString() {
		return new ToStringCreator(this).append("urlExpression", urlExpression).toString();
	}
}