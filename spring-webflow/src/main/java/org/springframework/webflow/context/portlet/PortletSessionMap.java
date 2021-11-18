/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.context.portlet;

import java.util.Iterator;

import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

import org.springframework.binding.collection.SharedMap;
import org.springframework.binding.collection.StringKeyedMapAdapter;
import org.springframework.web.util.WebUtils;
import org.springframework.webflow.context.servlet.HttpSessionMapBindingListener;
import org.springframework.webflow.core.collection.AttributeMapBindingListener;
import org.springframework.webflow.core.collection.CollectionUtils;

/**
 * Shared map backed by the Portlet session for accessing session scoped attributes in a Portlet environment.
 * 
 * @author Keith Donald
 */
public class PortletSessionMap extends StringKeyedMapAdapter implements SharedMap {

	/**
	 * The wrapped portlet request, providing access to the session.
	 */
	private PortletRequest request;

	/**
	 * The scope to access in the session, either APPLICATION (global) or PORTLET.
	 */
	private int scope;

	/**
	 * Create a new map wrapping the session associated with given request.
	 * @param request the current portlet request
	 * @param scope the scope to access in the session, either {@link PortletSession#APPLICATION_SCOPE} (global) or
	 * {@link PortletSession#PORTLET_SCOPE}
	 */
	public PortletSessionMap(PortletRequest request, int scope) {
		this.request = request;
		this.scope = scope;
	}

	/**
	 * Return the portlet session associated with the wrapped request, or null if no such session exits.
	 */
	private PortletSession getSession() {
		return request.getPortletSession(false);
	}

	protected Object getAttribute(String key) {
		PortletSession session = getSession();
		if (session == null) {
			return null;
		}
		Object value = session.getAttribute(key, scope);
		if (value instanceof HttpSessionMapBindingListener) {
			// unwrap
			return ((HttpSessionMapBindingListener) value).getListener();
		} else {
			return value;
		}
	}

	protected void setAttribute(String key, Object value) {
		PortletSession session = request.getPortletSession(true);
		if (value instanceof AttributeMapBindingListener) {
			// wrap
			session.setAttribute(key, new HttpSessionMapBindingListener((AttributeMapBindingListener) value, this),
					scope);
		} else {
			session.setAttribute(key, value, scope);
		}
	}

	protected void removeAttribute(String key) {
		PortletSession session = getSession();
		if (session != null) {
			session.removeAttribute(key, scope);
		}
	}

	protected Iterator getAttributeNames() {
		PortletSession session = getSession();
		return session == null ? CollectionUtils.EMPTY_ITERATOR : CollectionUtils.toIterator(session
				.getAttributeNames(scope));
	}

	public Object getMutex() {
		PortletSession session = request.getPortletSession(true);
		Object mutex = session.getAttribute(WebUtils.SESSION_MUTEX_ATTRIBUTE, scope);
		return mutex != null ? mutex : session;
	}
}