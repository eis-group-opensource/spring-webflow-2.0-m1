/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.util;

import org.springframework.core.io.Resource;

/**
 * Simple interface for all objects (typically flow builders) that hold on to a resource defining a flow (e.g. an XML
 * file). Provides a way to access information about the underlying resource like the last modified date.
 * 
 * @see org.springframework.webflow.engine.builder.FlowBuilder
 * 
 * @author Erwin Vervaet
 * @author Keith Donald
 */
public interface ResourceHolder {

	/**
	 * Returns the flow definition resource held by this holder.
	 */
	public Resource getResource();
}