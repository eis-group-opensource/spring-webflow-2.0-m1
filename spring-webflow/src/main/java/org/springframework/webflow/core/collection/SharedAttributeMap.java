/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.core.collection;

/**
 * An interface to be implemented by mutable attribute maps accessed by multiple threads that need to be synchronized.
 * 
 * @author Keith Donald
 */
public interface SharedAttributeMap extends MutableAttributeMap {

	/**
	 * Returns the shared map's mutex, which may be synchronized on to block access to the map by other threads.
	 */
	public Object getMutex();
}