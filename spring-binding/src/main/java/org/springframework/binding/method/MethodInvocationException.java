/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.method;

import java.lang.reflect.InvocationTargetException;

import org.springframework.core.NestedRuntimeException;
import org.springframework.core.style.StylerUtils;

/**
 * Base class for exceptions that report a method invocation failure.
 * 
 * @author Keith Donald
 */
public class MethodInvocationException extends NestedRuntimeException {

	/**
	 * The method signature. Transient because a MethodSignature is not Serializable.
	 */
	private transient MethodSignature methodSignature;

	/**
	 * The method invocation argument values. Transient because we cannot guarantee that the arguments are Serializable.
	 */
	private transient Object[] arguments;

	/**
	 * Signals that the method with the specified signature could not be invoked with the provided arguments.
	 * @param methodSignature the method signature
	 * @param arguments the arguments
	 * @param cause the root cause
	 */
	public MethodInvocationException(MethodSignature methodSignature, Object[] arguments, Throwable cause) {
		super("Unable to invoke method " + methodSignature + " with arguments " + StylerUtils.style(arguments), cause);
		this.methodSignature = methodSignature;
		this.arguments = arguments;
	}

	/**
	 * Returns the invoked method's signature.
	 */
	public MethodSignature getMethodSignature() {
		return methodSignature;
	}

	/**
	 * Returns the method invocation arguments.
	 */
	public Object[] getArguments() {
		return arguments;
	}

	/**
	 * Returns the target root cause exception of the method invocation failure.
	 * @return the target throwable
	 */
	public Throwable getTargetException() {
		Throwable targetException = getCause();
		while (targetException instanceof InvocationTargetException) {
			targetException = ((InvocationTargetException) targetException).getTargetException();
		}
		return targetException;
	}
}