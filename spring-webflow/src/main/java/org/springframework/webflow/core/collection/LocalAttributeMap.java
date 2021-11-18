/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.core.collection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.binding.collection.MapAccessor;
import org.springframework.core.style.StylerUtils;
import org.springframework.util.Assert;

/**
 * A generic, mutable attribute map with string keys.
 * 
 * @author Keith Donald
 */
public class LocalAttributeMap implements MutableAttributeMap, Serializable {

	/**
	 * The backing map storing the attributes.
	 */
	private Map attributes;

	/**
	 * A helper for accessing attributes. Marked transient and restored on deserialization.
	 */
	private transient MapAccessor attributeAccessor;

	/**
	 * Creates a new attribute map, initially empty.
	 */
	public LocalAttributeMap() {
		initAttributes(createTargetMap());
	}

	/**
	 * Creates a new attribute map, initially empty.
	 * @param size the initial size
	 * @param loadFactor the load factor
	 */
	public LocalAttributeMap(int size, int loadFactor) {
		initAttributes(createTargetMap(size, loadFactor));
	}

	/**
	 * Creates a new attribute map with a single entry.
	 */
	public LocalAttributeMap(String attributeName, Object attributeValue) {
		initAttributes(createTargetMap(1, 1));
		put(attributeName, attributeValue);
	}

	/**
	 * Creates a new attribute map wrapping the specified map.
	 */
	public LocalAttributeMap(Map map) {
		Assert.notNull(map, "The target map is required");
		initAttributes(map);
	}

	// implementing attribute map

	public Map asMap() {
		return attributeAccessor.asMap();
	}

	public int size() {
		return attributes.size();
	}

	public Object get(String attributeName) {
		return attributes.get(attributeName);
	}

	public boolean isEmpty() {
		return attributes.isEmpty();
	}

	public boolean contains(String attributeName) {
		return attributes.containsKey(attributeName);
	}

	public boolean contains(String attributeName, Class requiredType) throws IllegalArgumentException {
		return attributeAccessor.containsKey(attributeName, requiredType);
	}

	public Object get(String attributeName, Object defaultValue) {
		return attributeAccessor.get(attributeName, defaultValue);
	}

	public Object get(String attributeName, Class requiredType) throws IllegalArgumentException {
		return attributeAccessor.get(attributeName, requiredType);
	}

	public Object get(String attributeName, Class requiredType, Object defaultValue) throws IllegalStateException {
		return attributeAccessor.get(attributeName, requiredType, defaultValue);
	}

	public Object getRequired(String attributeName) throws IllegalArgumentException {
		return attributeAccessor.getRequired(attributeName);
	}

	public Object getRequired(String attributeName, Class requiredType) throws IllegalArgumentException {
		return attributeAccessor.getRequired(attributeName, requiredType);
	}

	public String getString(String attributeName) throws IllegalArgumentException {
		return attributeAccessor.getString(attributeName);
	}

	public String getString(String attributeName, String defaultValue) throws IllegalArgumentException {
		return attributeAccessor.getString(attributeName, defaultValue);
	}

	public String getRequiredString(String attributeName) throws IllegalArgumentException {
		return attributeAccessor.getRequiredString(attributeName);
	}

	public Collection getCollection(String attributeName) throws IllegalArgumentException {
		return attributeAccessor.getCollection(attributeName);
	}

	public Collection getCollection(String attributeName, Class requiredType) throws IllegalArgumentException {
		return attributeAccessor.getCollection(attributeName, requiredType);
	}

	public Collection getRequiredCollection(String attributeName) throws IllegalArgumentException {
		return attributeAccessor.getRequiredCollection(attributeName);
	}

	public Collection getRequiredCollection(String attributeName, Class requiredType) throws IllegalArgumentException {
		return attributeAccessor.getRequiredCollection(attributeName, requiredType);
	}

	public Object[] getArray(String attributeName, Class requiredType) throws IllegalArgumentException {
		return attributeAccessor.getArray(attributeName, requiredType);
	}

	public Object[] getRequiredArray(String attributeName, Class requiredType) throws IllegalArgumentException {
		return attributeAccessor.getRequiredArray(attributeName, requiredType);
	}

	public Number getNumber(String attributeName, Class requiredType) throws IllegalArgumentException {
		return attributeAccessor.getNumber(attributeName, requiredType);
	}

	public Number getNumber(String attributeName, Class requiredType, Number defaultValue)
			throws IllegalArgumentException {
		return attributeAccessor.getNumber(attributeName, requiredType, defaultValue);
	}

	public Number getRequiredNumber(String attributeName, Class requiredType) throws IllegalArgumentException {
		return attributeAccessor.getRequiredNumber(attributeName, requiredType);
	}

	public Integer getInteger(String attributeName) throws IllegalArgumentException {
		return attributeAccessor.getInteger(attributeName);
	}

	public Integer getInteger(String attributeName, Integer defaultValue) throws IllegalArgumentException {
		return attributeAccessor.getInteger(attributeName, defaultValue);
	}

	public Integer getRequiredInteger(String attributeName) throws IllegalArgumentException {
		return attributeAccessor.getRequiredInteger(attributeName);
	}

	public Long getLong(String attributeName) throws IllegalArgumentException {
		return attributeAccessor.getLong(attributeName);
	}

	public Long getLong(String attributeName, Long defaultValue) throws IllegalArgumentException {
		return attributeAccessor.getLong(attributeName, defaultValue);
	}

	public Long getRequiredLong(String attributeName) throws IllegalArgumentException {
		return attributeAccessor.getRequiredLong(attributeName);
	}

	public Boolean getBoolean(String attributeName) throws IllegalArgumentException {
		return attributeAccessor.getBoolean(attributeName);
	}

	public Boolean getBoolean(String attributeName, Boolean defaultValue) throws IllegalArgumentException {
		return attributeAccessor.getBoolean(attributeName, defaultValue);
	}

	public Boolean getRequiredBoolean(String attributeName) throws IllegalArgumentException {
		return attributeAccessor.getRequiredBoolean(attributeName);
	}

	public AttributeMap union(AttributeMap attributes) {
		if (attributes == null) {
			return new LocalAttributeMap(getMapInternal());
		} else {
			Map map = createTargetMap();
			map.putAll(getMapInternal());
			map.putAll(attributes.asMap());
			return new LocalAttributeMap(map);
		}
	}

	// implementing MutableAttributeMap

	public Object put(String attributeName, Object attributeValue) {
		return getMapInternal().put(attributeName, attributeValue);
	}

	public MutableAttributeMap putAll(AttributeMap attributes) {
		if (attributes == null) {
			return this;
		}
		getMapInternal().putAll(attributes.asMap());
		return this;
	}

	public Object remove(String attributeName) {
		return getMapInternal().remove(attributeName);
	}

	public MutableAttributeMap clear() throws UnsupportedOperationException {
		getMapInternal().clear();
		return this;
	}

	public MutableAttributeMap replaceWith(AttributeMap attributes) throws UnsupportedOperationException {
		clear();
		putAll(attributes);
		return this;
	}

	// helpers for subclasses

	/**
	 * Initializes this attribute map.
	 * @param attributes the attributes
	 */
	protected void initAttributes(Map attributes) {
		this.attributes = attributes;
		attributeAccessor = new MapAccessor(this.attributes);
	}

	/**
	 * Returns the wrapped, modifiable map implementation.
	 */
	protected Map getMapInternal() {
		return attributes;
	}

	// helpers

	/**
	 * Factory method that returns the target map storing the data in this attribute map.
	 * @return the target map
	 */
	protected Map createTargetMap() {
		return new HashMap();
	}

	/**
	 * Factory method that returns the target map storing the data in this attribute map.
	 * @param size the initial size of the map
	 * @param loadFactor the load factor
	 * @return the target map
	 */
	protected Map createTargetMap(int size, int loadFactor) {
		return new HashMap(size, loadFactor);
	}

	public boolean equals(Object o) {
		if (!(o instanceof LocalAttributeMap)) {
			return false;
		}
		LocalAttributeMap other = (LocalAttributeMap) o;
		return getMapInternal().equals(other.getMapInternal());
	}

	public int hashCode() {
		return getMapInternal().hashCode();
	}

	// custom serialization

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		attributeAccessor = new MapAccessor(attributes);
	}

	public String toString() {
		return StylerUtils.style(attributes);
	}
}