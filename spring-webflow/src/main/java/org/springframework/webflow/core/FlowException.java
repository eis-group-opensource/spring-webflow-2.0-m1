/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.core;

import org.springframework.core.NestedRuntimeException;

/**
 * Root class for exceptions thrown by the Spring Web Flow system. All other exceptions within the system should be
 * assignable to this class.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public abstract class FlowException extends NestedRuntimeException {

	/**
	 * Creates a new flow exception.
	 * @param msg the message
	 * @param cause the cause
	 */
	public FlowException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * Creates a new flow exception.
	 * @param msg the message
	 */
	public FlowException(String msg) {
		super(msg);
	}

}