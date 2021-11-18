/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.context.portlet;

import java.util.Iterator;

import javax.portlet.PortletRequest;

import org.springframework.binding.collection.StringKeyedMapAdapter;
import org.springframework.webflow.core.collection.CollectionUtils;

/**
 * Map backed by the Portlet request for accessing request scoped attributes.
 * 
 * @author Keith Donald
 */
public class PortletRequestMap extends StringKeyedMapAdapter {

	/**
	 * The wrapped portlet request.
	 */
	private PortletRequest request;

	/**
	 * Create a new map wrapping the attributes of given portlet request.
	 */
	public PortletRequestMap(PortletRequest request) {
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