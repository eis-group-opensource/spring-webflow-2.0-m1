/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.collection;

import java.util.Map;

/**
 * A simple subinterface of {@link Map} that exposes a mutex that application code can synchronize on.
 * <p>
 * Expected to be implemented by Maps that are backed by shared objects that require synchronization between multiple
 * threads. An example would be the HTTP session map.
 * 
 * @author Keith Donald
 */
public interface SharedMap extends Map {

	/**
	 * Returns the shared mutex that may be synchronized on using a synchronized block. The returned mutex is guaranteed
	 * to be non-null.
	 * 
	 * Example usage:
	 * 
	 * <pre>
	 * synchronized (sharedMap.getMutex()) {
	 * 	// do synchronized work
	 * }
	 * </pre>
	 * 
	 * @return the mutex
	 */
	public Object getMutex();
}