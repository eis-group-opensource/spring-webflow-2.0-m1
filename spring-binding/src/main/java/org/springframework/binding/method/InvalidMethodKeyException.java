/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.method;

import org.springframework.core.NestedRuntimeException;

/**
 * Thrown when a method key could not be resolved to an invokable java Method on a Class.
 * 
 * @author Keith Donald
 */
public class InvalidMethodKeyException extends NestedRuntimeException {

	/**
	 * The method key that could not be resolved.
	 */
	private MethodKey methodKey;

	/**
	 * Creates an exception signaling an invalid method signature.
	 * @param methodKey the class method key
	 * @param cause the cause
	 */
	public InvalidMethodKeyException(MethodKey methodKey, Exception cause) {
		super("Could not resolve method with key " + methodKey, cause);
		this.methodKey = methodKey;
	}

	/**
	 * Returns the invalid method key.
	 * @return the method key.
	 */
	public MethodKey getMethodKey() {
		return methodKey;
	}
}