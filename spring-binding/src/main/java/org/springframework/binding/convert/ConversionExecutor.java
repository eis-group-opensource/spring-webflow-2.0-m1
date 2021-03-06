/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.convert;

import org.springframework.core.style.ToStringCreator;
import org.springframework.util.Assert;

/**
 * A command object that is parameterized with the information necessary to perform a conversion of a source input to a
 * target output.
 * <p>
 * Specifically, encapsulates knowledge about how to convert source objects to a specific target type using a specific
 * converter.
 * 
 * @author Keith Donald
 */
public class ConversionExecutor {

	/**
	 * The source value type this executor will attempt to convert from.
	 */
	private final Class sourceClass;

	/**
	 * The target value type this executor will attempt to convert to.
	 */
	private final Class targetClass;

	/**
	 * The converter that will perform the conversion.
	 */
	private final Converter converter;

	/**
	 * Creates a conversion executor.
	 * @param sourceClass the source type that the converter will convert from
	 * @param targetClass the target type that the converter will convert to
	 * @param converter the converter that will perform the conversion
	 */
	public ConversionExecutor(Class sourceClass, Class targetClass, Converter converter) {
		Assert.notNull(sourceClass, "The source class is required");
		Assert.notNull(targetClass, "The target class is required");
		Assert.notNull(converter, "The converter is required");
		this.sourceClass = sourceClass;
		this.targetClass = targetClass;
		this.converter = converter;
	}

	/**
	 * Returns the source class of conversions performed by this executor.
	 * @return the source class
	 */
	public Class getSourceClass() {
		return sourceClass;
	}

	/**
	 * Returns the target class of conversions performed by this executor.
	 * @return the target class
	 */
	public Class getTargetClass() {
		return targetClass;
	}

	/**
	 * Returns the converter that will perform the conversion.
	 * @return the converter
	 */
	public Converter getConverter() {
		return converter;
	}

	/**
	 * Execute the conversion for the provided source object.
	 * @param source the source object to convert
	 */
	public Object execute(Object source) throws ConversionException {
		return execute(source, null);
	}

	/**
	 * Execute the conversion for the provided source object.
	 * @param source the source object to convert
	 * @param context the conversion context, useful for influencing the behavior of the converter
	 */
	public Object execute(Object source, ConversionContext context) throws ConversionException {
		if (getTargetClass().isInstance(source)) {
			// source is already assignment compatible with target class
			return source;
		}
		if (source != null && !getSourceClass().isInstance(source)) {
			throw new ConversionException(getSourceClass(), source, getTargetClass(), "Source object '" + source
					+ "' is expected to be an instance of " + getSourceClass());
		}
		return converter.convert(source, targetClass, context);
	}

	public boolean equals(Object o) {
		if (!(o instanceof ConversionExecutor)) {
			return false;
		}
		ConversionExecutor other = (ConversionExecutor) o;
		return sourceClass.equals(other.sourceClass) && targetClass.equals(other.targetClass);
	}

	public int hashCode() {
		return sourceClass.hashCode() + targetClass.hashCode();
	}

	public String toString() {
		return new ToStringCreator(this).append("sourceClass", sourceClass).append("targetClass", targetClass)
				.toString();
	}
}