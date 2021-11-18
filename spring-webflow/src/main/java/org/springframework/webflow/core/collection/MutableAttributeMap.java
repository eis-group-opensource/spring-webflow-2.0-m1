/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.core.collection;

/**
 * An interface for accessing and modifying attributes in a backing map with string keys.
 * <p>
 * Implementations can optionally support {@link AttributeMapBindingListener listeners} that will be notified when
 * they're bound in or unbound from the map.
 * 
 * @author Keith Donald
 */
public interface MutableAttributeMap extends AttributeMap {

	/**
	 * Put the attribute into this map.
	 * <p>
	 * If the attribute value is an {@link AttributeMapBindingListener} this map will publish
	 * {@link AttributeMapBindingEvent binding events} such as on "bind" and "unbind" if supported.
	 * <p>
	 * <b>Note</b>: not all <code>MutableAttributeMap</code> implementations support this.
	 * @param attributeName the attribute name
	 * @param attributeValue the attribute value
	 * @return the previous value of the attribute, or <tt>null</tt> of there was no previous value
	 */
	public Object put(String attributeName, Object attributeValue);

	/**
	 * Put all the attributes into this map.
	 * @param attributes the attributes to put into this map
	 * @return this, to support call chaining
	 */
	public MutableAttributeMap putAll(AttributeMap attributes);

	/**
	 * Remove an attribute from this map.
	 * @param attributeName the name of the attribute to remove
	 * @return previous value associated with specified attribute name, or <tt>null</tt> if there was no mapping for
	 * the name
	 */
	public Object remove(String attributeName);

	/**
	 * Remove all attributes in this map.
	 * @return this, to support call chaining
	 */
	public MutableAttributeMap clear();

	/**
	 * Replace the contents of this attribute map with the contents of the provided collection.
	 * @param attributes the attribute collection
	 * @return this, to support call chaining
	 */
	public MutableAttributeMap replaceWith(AttributeMap attributes) throws UnsupportedOperationException;

}