/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.context.servlet;

import java.util.Map;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.springframework.webflow.core.collection.AttributeMapBindingEvent;
import org.springframework.webflow.core.collection.AttributeMapBindingListener;
import org.springframework.webflow.core.collection.LocalAttributeMap;

/**
 * Helper class that adapts a generic {@link AttributeMapBindingListener} to a HTTP specific
 * {@link HttpSessionBindingListener}. Calls will be forwarded to the wrapped listener.
 * 
 * @author Keith Donald
 */
public class HttpSessionMapBindingListener implements HttpSessionBindingListener {

	private AttributeMapBindingListener listener;

	private Map sessionMap;

	/**
	 * Create a new wrapper for given listener.
	 * @param listener the listener to wrap
	 * @param sessionMap the session map containing the listener
	 */
	public HttpSessionMapBindingListener(AttributeMapBindingListener listener, Map sessionMap) {
		this.listener = listener;
		this.sessionMap = sessionMap;
	}

	/**
	 * Returns the wrapped listener.
	 */
	public AttributeMapBindingListener getListener() {
		return listener;
	}

	/**
	 * Returns the session map containing the listener.
	 */
	public Map getSessionMap() {
		return sessionMap;
	}

	public void valueBound(HttpSessionBindingEvent event) {
		listener.valueBound(getContextBindingEvent(event));
	}

	public void valueUnbound(HttpSessionBindingEvent event) {
		listener.valueUnbound(getContextBindingEvent(event));
	}

	/**
	 * Create a attribute map binding event for given HTTP session binding event.
	 */
	private AttributeMapBindingEvent getContextBindingEvent(HttpSessionBindingEvent event) {
		return new AttributeMapBindingEvent(new LocalAttributeMap(sessionMap), event.getName(), listener);
	}
}