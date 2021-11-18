/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.convert;

/**
 * A type converter converts objects from one type to another. They may support conversion of multiple source types to
 * multiple target types.
 * <p>
 * Implementations of this interface are thread-safe.
 * 
 * @author Keith Donald
 */
public interface Converter {

	/**
	 * The source classes this converter can convert from.
	 * @return the supported source classes
	 */
	public Class[] getSourceClasses();

	/**
	 * The target classes this converter can convert to.
	 * @return the supported target classes
	 */
	public Class[] getTargetClasses();

	/**
	 * Convert the provided source object argument to an instance of the specified target class.
	 * @param source the source object to convert, its class must be one of the supported <code>sourceClasses</code>
	 * @param targetClass the target class to convert the source to, must be one of the supported
	 * <code>targetClasses</code>
	 * @param context an optional conversion context that may be used to influence the conversion process
	 * @return the converted object, an instance of the target type
	 * @throws ConversionException an exception occured during the conversion
	 */
	public Object convert(Object source, Class targetClass, ConversionContext context) throws ConversionException;

}