/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.engine.builder;

import org.springframework.webflow.core.FlowException;

/**
 * Exception thrown to indicate a problem while building a flow.
 * 
 * @see org.springframework.webflow.engine.builder.FlowBuilder
 * 
 * @author Erwin Vervaet
 */
public class FlowBuilderException extends FlowException {

	/**
	 * Create a new flow builder exception.
	 * @param message descriptive message
	 */
	public FlowBuilderException(String message) {
		super(message);
	}

	/**
	 * Create a new flow builder exception.
	 * @param message descriptive message
	 * @param cause the underlying cause of this exception
	 */
	public FlowBuilderException(String message, Throwable cause) {
		super(message, cause);
	}
}