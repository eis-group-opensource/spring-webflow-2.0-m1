/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.executor.support;

import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;
import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.FlowExecutionContext;
import org.springframework.webflow.execution.support.FlowDefinitionRedirect;

/**
 * Flow executor argument handler that extracts arguments from the request path and exposes them in the URL path.
 * <p>
 * This allows for REST-style URLs to launch flows in the general format:
 * <code>http://${host}/${context path}/${dispatcher path}/${flowId}</code>.
 * <p>
 * For example, the URL <code>http://localhost/springair/reservation/booking</code> would launch a new execution of
 * the <code>booking</code> flow, assuming a context path of <code>/springair</code> and a servlet mapping of
 * <code>/reservation/*</code>.
 * <p>
 * This also allows for URLs to resume flow executions in the format:
 * <code>http://${host}/${context path}/${dispatcher path}/${key delimiter}/${flowExecutionKey}</code>.
 * <p>
 * For example, the URL <code>http://localhost/springair/reservation/k/ABC123XYZ</code> would resume flow execution
 * "ABC123XYZ".
 * <p>
 * Note: this implementation only works with <code>ExternalContext</code> implementations that return valid
 * {@link ExternalContext#getRequestPathInfo()} such as the {@link ServletExternalContext}. Furthermore, it assumes
 * that the controller handling flow requests is not identified using request path information. For instance, mapping
 * the dispatcher to "*.html" in web.xml would work since in this case the flow controller will be identified as part of
 * the dispatcher name (e.g. "flows.html"). Mapping the dispatcher to "/html/*" in web.xml will not work since that
 * would require the flow controller to be identified by the extra request path information (e.g. "/html/flows").
 * 
 * @author Keith Donald
 */
public class RequestPathFlowExecutorArgumentHandler extends RequestParameterFlowExecutorArgumentHandler {

	/**
	 * URL path separator ("/").
	 */
	private static final char PATH_SEPARATOR_CHARACTER = '/';

	/**
	 * Default value of the flow execution key delimiter ("k").
	 */
	private static final String KEY_DELIMITER = "k";

	/**
	 * The delimiter that when present in the requestPathInfo indicates the flowExecutionKey follows in the URL.
	 * Defaults to {@link #KEY_DELIMITER}.
	 */
	private String keyDelimiter = KEY_DELIMITER;

	/**
	 * Returns the key delimiter. Defaults to "k".
	 * @return the key delimiter
	 */
	public String getKeyDelimiter() {
		return keyDelimiter;
	}

	/**
	 * Sets the delimiter that when present in the requestPathInfo indicates the flowExecutionKey follows in the URL.
	 * Defaults to "k".
	 * @param keyDelimiter the key delimiter
	 * @see #extractFlowExecutionKey(ExternalContext)
	 */
	public void setKeyDelimiter(String keyDelimiter) {
		this.keyDelimiter = keyDelimiter;
	}

	public boolean isFlowIdPresent(ExternalContext context) {
		String requestPathInfo = getRequestPathInfo(context);
		boolean hasFileName = StringUtils.hasText(WebUtils.extractFilenameFromUrlPath(requestPathInfo));
		return hasFileName || super.isFlowIdPresent(context);
	}

	public String extractFlowId(ExternalContext context) {
		String requestPathInfo = getRequestPathInfo(context);
		String extractedFilename = WebUtils.extractFilenameFromUrlPath(requestPathInfo);
		return StringUtils.hasText(extractedFilename) ? extractedFilename : super.extractFlowId(context);
	}

	public boolean isFlowExecutionKeyPresent(ExternalContext context) {
		String requestPathInfo = getRequestPathInfo(context);
		return requestPathInfo.startsWith(keyPath()) || super.isFlowExecutionKeyPresent(context);
	}

	public String extractFlowExecutionKey(ExternalContext context) throws FlowExecutorArgumentExtractionException {
		String requestPathInfo = getRequestPathInfo(context);
		int index = requestPathInfo.indexOf(keyPath());
		if (index != -1) {
			return requestPathInfo.substring(index + keyPath().length());
		} else {
			return super.extractFlowExecutionKey(context);
		}
	}

	public String createFlowDefinitionUrl(FlowDefinitionRedirect flowDefinitionRedirect, ExternalContext context) {
		StringBuffer flowUrl = new StringBuffer();
		appendFlowExecutorPath(flowUrl, context);
		flowUrl.append(PATH_SEPARATOR_CHARACTER);
		flowUrl.append(flowDefinitionRedirect.getFlowDefinitionId());
		if (!flowDefinitionRedirect.getExecutionInput().isEmpty()) {
			flowUrl.append('?');
			appendQueryParameters(flowUrl, flowDefinitionRedirect.getExecutionInput());
		}
		return flowUrl.toString();
	}

	public String createFlowExecutionUrl(String flowExecutionKey, FlowExecutionContext flowExecution,
			ExternalContext context) {
		StringBuffer flowExecutionUrl = new StringBuffer();
		appendFlowExecutorPath(flowExecutionUrl, context);
		flowExecutionUrl.append(PATH_SEPARATOR_CHARACTER);
		flowExecutionUrl.append(keyDelimiter);
		flowExecutionUrl.append(PATH_SEPARATOR_CHARACTER);
		flowExecutionUrl.append(flowExecutionKey);
		return flowExecutionUrl.toString();
	}

	// internal helpers

	protected void appendFlowExecutorPath(StringBuffer url, ExternalContext context) {
		url.append(context.getContextPath());
		url.append(context.getDispatcherPath());
	}

	/**
	 * Returns the request path info for given external context. Never returns null, an empty string is returned
	 * instead.
	 */
	private String getRequestPathInfo(ExternalContext context) {
		String requestPathInfo = context.getRequestPathInfo();
		return requestPathInfo != null ? requestPathInfo : "";
	}

	/**
	 * Returns the flow execution key path in the request path info, e.g. "/k/".
	 */
	private String keyPath() {
		return PATH_SEPARATOR_CHARACTER + keyDelimiter + PATH_SEPARATOR_CHARACTER;
	}
}