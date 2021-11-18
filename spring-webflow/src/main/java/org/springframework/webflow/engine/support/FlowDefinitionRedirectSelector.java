/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.support;

import java.util.HashMap;
import java.util.Map;

import org.springframework.binding.expression.Expression;
import org.springframework.util.StringUtils;
import org.springframework.webflow.engine.ViewSelector;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.ViewSelection;
import org.springframework.webflow.execution.support.FlowDefinitionRedirect;

/**
 * Makes a {@link FlowDefinitionRedirect} selection when requested, calculating the <code>flowDefinitionId</code> and
 * <code>executionInput</code> by evaluating an expression against the request context.
 * 
 * @see org.springframework.webflow.execution.support.FlowDefinitionRedirect
 * 
 * @author Keith Donald
 */
public class FlowDefinitionRedirectSelector implements ViewSelector {

	/**
	 * The parsed flow expression, evaluatable to the string format:
	 * flowDefinitionId?param1Name=parmValue&param2Name=paramValue.
	 */
	private Expression expression;

	/**
	 * Creates a new launch flow redirect selector.
	 * @param expression the parsed flow redirect expression, evaluatable to the string format:
	 * flowDefinitionId?param1Name=parmValue&param2Name=paramValue
	 */
	public FlowDefinitionRedirectSelector(Expression expression) {
		this.expression = expression;
	}

	public boolean isEntrySelectionRenderable(RequestContext context) {
		return true;
	}

	public ViewSelection makeEntrySelection(RequestContext context) {
		String encodedRedirect = (String) expression.evaluate(context, null);
		if (encodedRedirect == null) {
			throw new IllegalStateException(
					"Flow definition redirect expression evaluated to [null], the expression was " + expression);
		}
		// the encoded FlowDefinitionRedirect should look something like
		// "flowDefinitionId?param0=value0&param1=value1"
		// now parse that and build a corresponding view selection
		int index = encodedRedirect.indexOf('?');
		String flowDefinitionId;
		Map executionInput = null;
		if (index != -1) {
			flowDefinitionId = encodedRedirect.substring(0, index);
			String[] parameters = StringUtils.delimitedListToStringArray(encodedRedirect.substring(index + 1), "&");
			executionInput = new HashMap(parameters.length, 1);
			for (int i = 0; i < parameters.length; i++) {
				String nameAndValue = parameters[i];
				index = nameAndValue.indexOf('=');
				if (index != -1) {
					executionInput.put(nameAndValue.substring(0, index), nameAndValue.substring(index + 1));
				} else {
					executionInput.put(nameAndValue, "");
				}
			}
		} else {
			flowDefinitionId = encodedRedirect;
		}
		if (!StringUtils.hasText(flowDefinitionId)) {
			// equivalent to restart
			flowDefinitionId = context.getFlowExecutionContext().getDefinition().getId();
		}
		return new FlowDefinitionRedirect(flowDefinitionId, executionInput);
	}

	public ViewSelection makeRefreshSelection(RequestContext context) {
		return makeEntrySelection(context);
	}
}