/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.convert;

import org.springframework.core.NestedRuntimeException;

/**
 * Base class for exceptions thrown by the type conversion system.
 * 
 * @author Keith Donald
 */
public class ConversionException extends NestedRuntimeException {

	/**
	 * The source type we tried to convert from
	 */
	private Class sourceClass;

	/**
	 * The value we tried to convert. Transient because we cannot guarantee that the value is Serializable.
	 */
	private transient Object value;

	/**
	 * The target type we tried to convert to.
	 */
	private Class targetClass;

	/**
	 * Creates a new conversion exception.
	 * @param value the value we tried to convert
	 * @param targetClass the target type
	 */
	public ConversionException(Object value, Class targetClass) {
		super("Unable to convert value '" + value + "' of type '" + (value != null ? value.getClass().getName() : null)
				+ "' to class '" + targetClass.getName() + "'");
		this.value = value;
		this.targetClass = targetClass;
	}

	/**
	 * Creates a new conversion exception.
	 * @param value the value we tried to convert
	 * @param targetClass the target type
	 * @param cause underlying cause of this exception
	 */
	public ConversionException(Object value, Class targetClass, Throwable cause) {
		super("Unable to convert value '" + value + "' of type '" + (value != null ? value.getClass().getName() : null)
				+ "' to class '" + targetClass.getName() + "'", cause);
		this.value = value;
		this.targetClass = targetClass;
	}

	/**
	 * Creates a new conversion exception.
	 * @param value the value we tried to convert
	 * @param targetClass the target type
	 * @param message a descriptive message
	 * @param cause underlying cause of this exception
	 */
	public ConversionException(Object value, Class targetClass, String message, Throwable cause) {
		super(message, cause);
		this.value = value;
		this.targetClass = targetClass;
	}

	/**
	 * Creates a new conversion exception.
	 * @param sourceClass the source type
	 * @param targetClass the target type
	 * @param message a descriptive message
	 */
	public ConversionException(Class sourceClass, Class targetClass, String message) {
		super(message);
		this.sourceClass = sourceClass;
		this.value = null; // not available
		this.targetClass = targetClass;
	}

	/**
	 * Creates a new conversion exception.
	 * @param sourceClass the source type
	 * @param message a descriptive message
	 */
	public ConversionException(Class sourceClass, String message) {
		super(message);
		this.sourceClass = sourceClass;
		this.value = null; // not available
		this.targetClass = null; // not available
	}

	/**
	 * Creates a new conversion exception.
	 * @param sourceClass the source type
	 * @param value the value we tried to convert
	 * @param targetClass the target type
	 * @param message a descriptive message
	 */
	public ConversionException(Class sourceClass, Object value, Class targetClass, String message) {
		super(message);
		this.sourceClass = sourceClass;
		this.value = value;
		this.targetClass = targetClass;
	}

	/**
	 * Returns the source type.
	 */
	public Class getSourceClass() {
		return sourceClass;
	}

	/**
	 * Returns the value we tried to convert.
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Returns the target type.
	 */
	public Class getTargetClass() {
		return targetClass;
	}
}