/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.executor.support;

import org.springframework.webflow.core.FlowException;

/**
 * An exception thrown by a flow executor argument extractor when an argument could not be extracted.
 * 
 * @see org.springframework.webflow.executor.support.FlowExecutorArgumentExtractor
 * 
 * @author Keith Donald
 */
public class FlowExecutorArgumentExtractionException extends FlowException {

	/**
	 * Creates a new argument extraction exception.
	 * @param msg a descriptive message
	 */
	public FlowExecutorArgumentExtractionException(String msg) {
		super(msg);
	}

	/**
	 * Creates a new argument extraction exception.
	 * @param msg a descriptive message
	 * @param cause the cause
	 */
	public FlowExecutorArgumentExtractionException(String msg, Throwable cause) {
		super(msg, cause);
	}
}