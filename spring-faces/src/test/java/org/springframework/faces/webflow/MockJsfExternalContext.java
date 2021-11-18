/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.faces.webflow;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.faces.context.ExternalContext;

public class MockJsfExternalContext extends ExternalContext {

	private Map applicationMap = new HashMap();

	private Map sessionMap = new HashMap();

	private Map requestMap = new HashMap();

	private Map requestParameterMap = Collections.EMPTY_MAP;

	public void dispatch(String arg0) throws IOException {
	}

	public String encodeActionURL(String arg0) {
		return null;
	}

	public String encodeNamespace(String arg0) {
		return null;
	}

	public String encodeResourceURL(String arg0) {
		return null;
	}

	public Map getApplicationMap() {
		return applicationMap;
	}

	public String getAuthType() {
		return null;
	}

	public Object getContext() {
		return null;
	}

	public String getInitParameter(String arg0) {
		return null;
	}

	public Map getInitParameterMap() {
		return null;
	}

	public String getRemoteUser() {
		return null;
	}

	public Object getRequest() {
		return null;
	}

	public String getRequestContextPath() {
		return null;
	}

	public Map getRequestCookieMap() {
		return null;
	}

	public Map getRequestHeaderMap() {
		return null;
	}

	public Map getRequestHeaderValuesMap() {
		return null;
	}

	public Locale getRequestLocale() {
		return null;
	}

	public Iterator getRequestLocales() {
		return null;
	}

	public Map getRequestMap() {
		return requestMap;
	}

	/**
	 * Set the request map for this external context.
	 * @param requestMap The requestMap to set.
	 */
	public void setRequestMap(Map requestMap) {
		this.requestMap = requestMap;
	}

	public Map getRequestParameterMap() {
		return requestParameterMap;
	}

	/**
	 * Set the request parameter map for this external context.
	 * @param requestParameterMap the request parameter map to set.
	 */
	public void setRequestParameterMap(Map requestParameterMap) {
		this.requestParameterMap = requestParameterMap;
	}

	public Iterator getRequestParameterNames() {
		return requestParameterMap.keySet().iterator();
	}

	public Map getRequestParameterValuesMap() {
		return null;
	}

	public String getRequestPathInfo() {
		return null;
	}

	public String getRequestServletPath() {
		return null;
	}

	public URL getResource(String arg0) throws MalformedURLException {
		return null;
	}

	public InputStream getResourceAsStream(String arg0) {
		return null;
	}

	public Set getResourcePaths(String arg0) {
		return null;
	}

	public Object getResponse() {
		return null;
	}

	public Object getSession(boolean arg0) {
		return null;
	}

	public Map getSessionMap() {
		return sessionMap;
	}

	public Principal getUserPrincipal() {
		return null;
	}

	public boolean isUserInRole(String arg0) {
		return false;
	}

	public void log(String arg0) {
	}

	public void log(String arg0, Throwable arg1) {
	}

	public void redirect(String arg0) throws IOException {
	}
}