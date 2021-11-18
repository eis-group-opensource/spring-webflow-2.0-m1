/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.core.collection;

import org.springframework.binding.collection.SharedMap;

/**
 * An attribute map that exposes a mutex that application code can synchronize on. This class wraps another shared map
 * in an attribute map.
 * <p>
 * The mutex can be used to serialize concurrent access to the shared map's contents by multiple threads.
 * 
 * @author Keith Donald
 */
public class LocalSharedAttributeMap extends LocalAttributeMap implements SharedAttributeMap {

	/**
	 * Creates a new shared attribute map.
	 * @param sharedMap the shared map
	 */
	public LocalSharedAttributeMap(SharedMap sharedMap) {
		super(sharedMap);
	}

	public Object getMutex() {
		return getSharedMap().getMutex();
	}

	/**
	 * Returns the wrapped shared map.
	 */
	protected SharedMap getSharedMap() {
		return (SharedMap) getMapInternal();
	}
}