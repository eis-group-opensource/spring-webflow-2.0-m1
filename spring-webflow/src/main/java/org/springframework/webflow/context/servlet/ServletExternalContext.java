/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.context.servlet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.style.ToStringCreator;
import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.core.collection.LocalAttributeMap;
import org.springframework.webflow.core.collection.LocalParameterMap;
import org.springframework.webflow.core.collection.LocalSharedAttributeMap;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.core.collection.ParameterMap;
import org.springframework.webflow.core.collection.SharedAttributeMap;

/**
 * Provides contextual information about an HTTP Servlet environment that has interacted with Spring Web Flow.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public class ServletExternalContext implements ExternalContext {

	/**
	 * The context.
	 */
	private ServletContext context;

	/**
	 * The request.
	 */
	private HttpServletRequest request;

	/**
	 * The response.
	 */
	private HttpServletResponse response;

	/**
	 * An accessor for the HTTP request parameter map.
	 */
	private ParameterMap requestParameterMap;

	/**
	 * An accessor for the HTTP request attribute map.
	 */
	private MutableAttributeMap requestMap;

	/**
	 * An accessor for the HTTP session map.
	 */
	private SharedAttributeMap sessionMap;

	/**
	 * An accessor for the servlet context application map.
	 */
	private SharedAttributeMap applicationMap;

	/**
	 * Create a new external context wrapping given servlet HTTP request and response and given servlet context.
	 * @param context the servlet context
	 * @param request the HTTP request
	 * @param response the HTTP response
	 */
	public ServletExternalContext(ServletContext context, HttpServletRequest request, HttpServletResponse response) {
		this.context = context;
		this.request = request;
		this.response = response;
		this.requestParameterMap = new LocalParameterMap(new HttpServletRequestParameterMap(request));
		this.requestMap = new LocalAttributeMap(new HttpServletRequestMap(request));
		this.sessionMap = new LocalSharedAttributeMap(new HttpSessionMap(request));
		this.applicationMap = new LocalSharedAttributeMap(new HttpServletContextMap(context));
	}

	public String getContextPath() {
		return request.getContextPath();
	}

	public String getDispatcherPath() {
		return request.getServletPath();
	}

	public String getRequestPathInfo() {
		return request.getPathInfo();
	}

	public ParameterMap getRequestParameterMap() {
		return requestParameterMap;
	}

	public MutableAttributeMap getRequestMap() {
		return requestMap;
	}

	public SharedAttributeMap getSessionMap() {
		return sessionMap;
	}

	public SharedAttributeMap getGlobalSessionMap() {
		return getSessionMap();
	}

	public SharedAttributeMap getApplicationMap() {
		return applicationMap;
	}

	/**
	 * Return the wrapped HTTP servlet context.
	 */
	public ServletContext getContext() {
		return context;
	}

	/**
	 * Return the wrapped HTTP servlet request.
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * Return the wrapped HTTP servlet response.
	 */
	public HttpServletResponse getResponse() {
		return response;
	}

	public String toString() {
		return new ToStringCreator(this).append("requestParameterMap", getRequestParameterMap()).toString();
	}
}