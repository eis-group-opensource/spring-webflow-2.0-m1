/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.test;

import java.util.HashMap;

import org.springframework.binding.collection.SharedMapDecorator;
import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.core.collection.LocalAttributeMap;
import org.springframework.webflow.core.collection.LocalSharedAttributeMap;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.core.collection.ParameterMap;
import org.springframework.webflow.core.collection.SharedAttributeMap;

/**
 * Mock implementation of the {@link ExternalContext} interface.
 * 
 * @see ExternalContext
 * 
 * @author Keith Donald
 */
public class MockExternalContext implements ExternalContext {

	private String contextPath;

	private String dispatcherPath;

	private String requestPathInfo;

	private ParameterMap requestParameterMap = new MockParameterMap();

	private MutableAttributeMap requestMap = new LocalAttributeMap();

	private SharedAttributeMap sessionMap = new LocalSharedAttributeMap(new SharedMapDecorator(new HashMap()));

	private SharedAttributeMap globalSessionMap = sessionMap;

	private SharedAttributeMap applicationMap = new LocalSharedAttributeMap(new SharedMapDecorator(new HashMap()));

	/**
	 * Creates a mock external context with an empty request parameter map. Allows for bean style usage.
	 */
	public MockExternalContext() {
	}

	/**
	 * Creates a mock external context with the specified parameters in the request parameter map. All other properties
	 * of the external context can be set using the appropriate setter.
	 * @param requestParameterMap the request parameters
	 */
	public MockExternalContext(ParameterMap requestParameterMap) {
		if (requestParameterMap != null) {
			this.requestParameterMap = requestParameterMap;
		}
	}

	// implementing external context

	public String getContextPath() {
		return contextPath;
	}

	public String getDispatcherPath() {
		return dispatcherPath;
	}

	public String getRequestPathInfo() {
		return requestPathInfo;
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
		return globalSessionMap;
	}

	public SharedAttributeMap getApplicationMap() {
		return applicationMap;
	}

	// helper setters

	/**
	 * Set the context path.
	 * @see ExternalContext#getContextPath()
	 */
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	/**
	 * Set the dispatcher path.
	 * @see ExternalContext#getDispatcherPath()
	 */
	public void setDispatcherPath(String dispatcherPath) {
		this.dispatcherPath = dispatcherPath;
	}

	/**
	 * Set the request path info.
	 * @see ExternalContext#getRequestPathInfo()
	 */
	public void setRequestPathInfo(String requestPathInfo) {
		this.requestPathInfo = requestPathInfo;
	}

	/**
	 * Set the request parameter map.
	 * @see ExternalContext#getRequestParameterMap()
	 */
	public void setRequestParameterMap(ParameterMap requestParameterMap) {
		this.requestParameterMap = requestParameterMap;
	}

	/**
	 * Set the request attribute map.
	 * @see ExternalContext#getRequestMap()
	 */
	public void setRequestMap(MutableAttributeMap requestMap) {
		this.requestMap = requestMap;
	}

	/**
	 * Set the session attribute map.
	 * @see ExternalContext#getSessionMap()
	 */
	public void setSessionMap(SharedAttributeMap sessionMap) {
		this.sessionMap = sessionMap;
	}

	/**
	 * Set the global session attribute map. By default the session attribute map and the global session attribute map
	 * are one and the same.
	 * @see ExternalContext#getGlobalSessionMap()
	 */
	public void setGlobalSessionMap(SharedAttributeMap globalSessionMap) {
		this.globalSessionMap = globalSessionMap;
	}

	/**
	 * Set the application attribute map.
	 * @see ExternalContext#getApplicationMap()
	 */
	public void setApplicationMap(SharedAttributeMap applicationMap) {
		this.applicationMap = applicationMap;
	}

	// convenience helpers

	/**
	 * Returns the request parameter map as a {@link MockParameterMap} for convenient access in a unit test.
	 * @see #getRequestParameterMap()
	 */
	public MockParameterMap getMockRequestParameterMap() {
		return (MockParameterMap) requestParameterMap;
	}

	/**
	 * Puts a request parameter into the mock parameter map.
	 * @param parameterName the parameter name
	 * @param parameterValue the parameter value
	 */
	public void putRequestParameter(String parameterName, String parameterValue) {
		getMockRequestParameterMap().put(parameterName, parameterValue);
	}

	/**
	 * Puts a multi-valued request parameter into the mock parameter map.
	 * @param parameterName the parameter name
	 * @param parameterValues the parameter values
	 */
	public void putRequestParameter(String parameterName, String[] parameterValues) {
		getMockRequestParameterMap().put(parameterName, parameterValues);
	}
}