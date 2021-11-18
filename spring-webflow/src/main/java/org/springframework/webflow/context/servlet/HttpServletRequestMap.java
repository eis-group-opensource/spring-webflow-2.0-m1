/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.context.servlet;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.binding.collection.StringKeyedMapAdapter;
import org.springframework.webflow.core.collection.CollectionUtils;

/**
 * Map backed by the Servlet HTTP request attribute map for accessing request local attributes.
 * 
 * @author Keith Donald
 */
public class HttpServletRequestMap extends StringKeyedMapAdapter {

	/**
	 * The wrapped HTTP request.
	 */
	private HttpServletRequest request;

	/**
	 * Create a new map wrapping the attributes of given request.
	 */
	public HttpServletRequestMap(HttpServletRequest request) {
		this.request = request;
	}

	protected Object getAttribute(String key) {
		return request.getAttribute(key);
	}

	protected void setAttribute(String key, Object value) {
		request.setAttribute(key, value);
	}

	protected void removeAttribute(String key) {
		request.removeAttribute(key);
	}

	protected Iterator getAttributeNames() {
		return CollectionUtils.toIterator(request.getAttributeNames());
	}
}