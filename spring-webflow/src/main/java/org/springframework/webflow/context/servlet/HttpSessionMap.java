/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.context.servlet;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.binding.collection.SharedMap;
import org.springframework.binding.collection.StringKeyedMapAdapter;
import org.springframework.web.util.WebUtils;
import org.springframework.webflow.core.collection.AttributeMapBindingListener;
import org.springframework.webflow.core.collection.CollectionUtils;

/**
 * A Shared Map backed by the Servlet HTTP session, for accessing session scoped attributes.
 * 
 * @author Keith Donald
 */
public class HttpSessionMap extends StringKeyedMapAdapter implements SharedMap {

	/**
	 * The wrapped HTTP request, providing access to the session.
	 */
	private HttpServletRequest request;

	/**
	 * Create a map wrapping the session of given request.
	 */
	public HttpSessionMap(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * Internal helper to get the HTTP session associated with the wrapped request, or null if there is no such session.
	 * <p>
	 * Note that this method will not force session creation.
	 */
	private HttpSession getSession() {
		return request.getSession(false);
	}

	protected Object getAttribute(String key) {
		HttpSession session = getSession();
		if (session == null) {
			return null;
		}
		Object value = session.getAttribute(key);
		if (value instanceof HttpSessionMapBindingListener) {
			// unwrap
			return ((HttpSessionMapBindingListener) value).getListener();
		} else {
			return value;
		}
	}

	protected void setAttribute(String key, Object value) {
		// force session creation
		HttpSession session = request.getSession(true);
		if (value instanceof AttributeMapBindingListener) {
			// wrap
			session.setAttribute(key, new HttpSessionMapBindingListener((AttributeMapBindingListener) value, this));
		} else {
			session.setAttribute(key, value);
		}
	}

	protected void removeAttribute(String key) {
		HttpSession session = getSession();
		if (session != null) {
			session.removeAttribute(key);
		}
	}

	protected Iterator getAttributeNames() {
		HttpSession session = getSession();
		return session == null ? CollectionUtils.EMPTY_ITERATOR : CollectionUtils.toIterator(session
				.getAttributeNames());
	}

	public Object getMutex() {
		// force session creation
		HttpSession session = request.getSession(true);
		Object mutex = session.getAttribute(WebUtils.SESSION_MUTEX_ATTRIBUTE);
		return mutex != null ? mutex : session;
	}
}