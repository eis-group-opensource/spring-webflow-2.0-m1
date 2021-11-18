/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.context.servlet;

import java.util.Iterator;

import javax.servlet.ServletContext;

import org.springframework.binding.collection.SharedMap;
import org.springframework.binding.collection.StringKeyedMapAdapter;
import org.springframework.webflow.core.collection.CollectionUtils;

/**
 * Map backed by the Servlet context for accessing application scoped attributes.
 * 
 * @author Keith Donald
 */
public class HttpServletContextMap extends StringKeyedMapAdapter implements SharedMap {

	/**
	 * The wrapped servlet context.
	 */
	private ServletContext context;

	/**
	 * Create a map wrapping given servlet context.
	 */
	public HttpServletContextMap(ServletContext context) {
		this.context = context;
	}

	protected Object getAttribute(String key) {
		return context.getAttribute(key);
	}

	protected void setAttribute(String key, Object value) {
		context.setAttribute(key, value);
	}

	protected void removeAttribute(String key) {
		context.removeAttribute(key);
	}

	protected Iterator getAttributeNames() {
		return CollectionUtils.toIterator(context.getAttributeNames());
	}

	public Object getMutex() {
		return context;
	}
}