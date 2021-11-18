/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.collection;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.core.style.ToStringCreator;

/**
 * A map decorator that implements <code>SharedMap</code>. By default, simply returns the map itself as the mutex.
 * Subclasses may override to return a different mutex object.
 * 
 * @author Keith Donald
 */
public class SharedMapDecorator implements SharedMap, Serializable {

	/**
	 * The wrapped, target map.
	 */
	private Map map;

	/**
	 * Creates a new shared map decorator.
	 * @param map the map that is shared by multiple threads, to be synced
	 */
	public SharedMapDecorator(Map map) {
		this.map = map;
	}

	// implementing Map

	public void clear() {
		map.clear();
	}

	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	public Set entrySet() {
		return map.entrySet();
	}

	public Object get(Object key) {
		return map.get(key);
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public Set keySet() {
		return map.keySet();
	}

	public Object put(Object key, Object value) {
		return map.put(key, value);
	}

	public void putAll(Map map) {
		this.map.putAll(map);
	}

	public Object remove(Object key) {
		return map.remove(key);
	}

	public int size() {
		return map.size();
	}

	public Collection values() {
		return map.values();
	}

	// implementing SharedMap

	public Object getMutex() {
		return map;
	}

	public String toString() {
		return new ToStringCreator(this).append("map", map).append("mutex", getMutex()).toString();
	}
}