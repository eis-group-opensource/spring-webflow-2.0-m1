/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.core.collection;

import java.util.EventObject;

/**
 * Holder for information about the binding or unbinding event in an {@link AttributeMap}.
 * 
 * @see AttributeMapBindingListener
 * 
 * @author Ben Hale
 */
public class AttributeMapBindingEvent extends EventObject {

	private String attributeName;

	private Object attributeValue;

	/**
	 * Creates an event for map binding that contains information about the event.
	 * @param source the source map that this attribute was bound in
	 * @param attributeName the name that this attribute was bound with
	 * @param attributeValue the attribute
	 */
	public AttributeMapBindingEvent(AttributeMap source, String attributeName, Object attributeValue) {
		super(source);
		this.source = source;
		this.attributeName = attributeName;
		this.attributeValue = attributeValue;
	}

	/**
	 * Returns the name the attribute was bound with.
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * Returns the value of the attribute.
	 */
	public Object getAttributeValue() {
		return attributeValue;
	}
}