/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.core.collection;

/**
 * Causes an object to be notified when it is bound or unbound from an {@link AttributeMap}.
 * <p>
 * Note that this is an optional feature and not all {@link AttributeMap} implementations support it.
 * 
 * @see AttributeMap
 * 
 * @author Ben Hale
 */
public interface AttributeMapBindingListener {

	/**
	 * Called when the implementing instance is bound into an <code>AttributeMap</code>.
	 * @param event information about the binding event
	 */
	void valueBound(AttributeMapBindingEvent event);

	/**
	 * Called when the implementing instance is unbound from an <code>AttributeMap</code>.
	 * @param event information about the unbinding event
	 */
	void valueUnbound(AttributeMapBindingEvent event);
}