/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.executor.support;

import java.util.Map;

import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.execution.FlowExecutionContext;
import org.springframework.webflow.execution.support.ExternalRedirect;
import org.springframework.webflow.execution.support.FlowDefinitionRedirect;
import org.springframework.webflow.execution.support.FlowExecutionRedirect;
import org.springframework.webflow.executor.FlowExecutor;

/**
 * Helper strategy that can expose {@link FlowExecutor} method arguments in a response (view) so that subsequent
 * requests resulting from the response can have those arguments extracted again, typically using a
 * {@link FlowExecutorArgumentExtractor}.
 * <p>
 * Arguments can either be exposed in the model of a view that will be rendered or in a URL that will be used to trigger
 * a new request into Spring Web Flow, for instance using a redirect.
 * 
 * @author Erwin Vervaet
 */
public interface FlowExecutorArgumentExposer {

	/**
	 * Expose the flow execution context and it's key in given model map.
	 * @param flowExecutionKey the flow execution key (may be null if the conversation has ended)
	 * @param context the flow execution context
	 * @param model the model map
	 */
	public void exposeFlowExecutionContext(String flowExecutionKey, FlowExecutionContext context, Map model);

	/**
	 * Create a URL that when redirected to launches a entirely new execution of a flow definition (starts a new
	 * conversation). Used to support the <i>restart flow</i> and <i>redirect to flow</i> use cases.
	 * @param flowDefinitionRedirect the flow definition redirect view selection
	 * @param context the external context
	 * @return the relative flow URL path to redirect to
	 */
	public String createFlowDefinitionUrl(FlowDefinitionRedirect flowDefinitionRedirect, ExternalContext context);

	/**
	 * Create a URL path that when redirected to renders the <i>current</i> (or last) view selection made by the flow
	 * execution identified by the flow execution key. Used to support the <i>flow execution redirect</i> use case.
	 * @param flowExecutionKey the flow execution key
	 * @param flowExecution the flow execution
	 * @param context the external context
	 * @return the relative conversation URL path
	 * @see FlowExecutionRedirect
	 */
	public String createFlowExecutionUrl(String flowExecutionKey, FlowExecutionContext flowExecution,
			ExternalContext context);

	/**
	 * Create a URL path that when redirected to communicates with an external system outside of Spring Web Flow.
	 * @param redirect the external redirect request
	 * @param flowExecutionKey the flow execution key to send through the redirect (optional)
	 * @param context the external context
	 * @return the external URL
	 */
	public String createExternalUrl(ExternalRedirect redirect, String flowExecutionKey, ExternalContext context);

}
