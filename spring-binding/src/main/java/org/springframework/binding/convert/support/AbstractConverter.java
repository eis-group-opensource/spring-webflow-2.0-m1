/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.convert.support;

import org.springframework.binding.convert.ConversionContext;
import org.springframework.binding.convert.ConversionException;
import org.springframework.binding.convert.Converter;

/**
 * Base class for converters provided as a convenience to implementors.
 * 
 * @author Keith Donald
 */
public abstract class AbstractConverter implements Converter {

	/**
	 * Convenience convert method that converts the provided source to the first target object supported by this
	 * converter. Useful when a converter only supports conversion to a single target.
	 * @param source the source to convert
	 * @return the converted object
	 * @throws ConversionException an exception occured converting the source value
	 */
	public Object convert(Object source) throws ConversionException {
		return convert(source, getTargetClasses()[0], null);
	}

	/**
	 * Convenience convert method that converts the provided source to the target class specified with an empty
	 * conversion context.
	 * @param source the source to convert
	 * @param targetClass the target class to convert the source to, must be one of the supported
	 * <code>targetClasses</code>
	 * @return the converted object
	 * @throws ConversionException an exception occured converting the source value
	 */
	public Object convert(Object source, Class targetClass) throws ConversionException {
		return convert(source, targetClass, null);
	}

	/**
	 * Convenience convert method that converts the provided source to the first target object supported by this
	 * converter. Useful when a converter only supports conversion to a single target.
	 * @param source the source to convert
	 * @param context the conversion context, useful for influencing the behavior of the converter
	 * @return the converted object
	 * @throws ConversionException an exception occured converting the source value
	 */
	public Object convert(Object source, ConversionContext context) throws ConversionException {
		return convert(source, getTargetClasses()[0], context);
	}

	public Object convert(Object source, Class targetClass, ConversionContext context) throws ConversionException {
		try {
			return doConvert(source, targetClass, context);
		} catch (ConversionException e) {
			throw e;
		} catch (Throwable e) {
			// wrap in a ConversionException
			if (targetClass == null) {
				targetClass = getTargetClasses()[0];
			}
			throw new ConversionException(source, targetClass, e);
		}
	}

	/**
	 * Template method subclasses should override to actually perform the type conversion.
	 * @param source the source to convert from
	 * @param targetClass the target type to convert to
	 * @param context an optional conversion context that may be used to influence the conversion process, could be null
	 * @return the converted source value
	 * @throws Exception an exception occured, will be wrapped in a conversion exception if necessary
	 */
	protected abstract Object doConvert(Object source, Class targetClass, ConversionContext context) throws Exception;

}