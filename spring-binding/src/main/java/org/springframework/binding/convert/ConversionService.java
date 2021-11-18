/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.convert;

/**
 * A service interface for retrieving type conversion executors. The returned command objects are thread-safe and may be
 * safely cached for use by client code.
 * 
 * @author Keith Donald
 */
public interface ConversionService {

	/**
	 * Return a conversion executor command object capable of converting source objects of the specified
	 * <code>sourceClass</code> to instances of the <code>targetClass</code>.
	 * <p>
	 * The returned ConversionExecutor is thread-safe and may safely be cached for use in client code.
	 * @param sourceClass the source class to convert from
	 * @param targetClass the target class to convert to
	 * @return the executor that can execute instance conversion, never null
	 * @throws ConversionException an exception occured retrieving a converter for the source-to-target pair
	 */
	public ConversionExecutor getConversionExecutor(Class sourceClass, Class targetClass) throws ConversionException;

	/**
	 * Return a conversion executor command object capable of converting source objects of the specified
	 * <code>sourceClass</code> to target objects of the type associated with the specified alias.
	 * @param sourceClass the sourceClass
	 * @param targetAlias the target alias
	 * @return the conversion executor, or null if the alias cannot be found
	 * @throws ConversionException an exception occured retrieving a converter for the source-to-target pair
	 */
	public ConversionExecutor getConversionExecutorByTargetAlias(Class sourceClass, String targetAlias)
			throws ConversionException;

	/**
	 * Return all conversion executors capable of converting source objects of the the specified
	 * <code>sourceClass</code>.
	 * @param sourceClass the source class to convert from
	 * @return the matching conversion executors
	 * @throws ConversionException an exception occured retrieving the converters
	 */
	public ConversionExecutor[] getConversionExecutorsForSource(Class sourceClass) throws ConversionException;

	/**
	 * Return the class with the specified alias.
	 * @param alias the class alias
	 * @return the class, or null if not aliased
	 * @throws ConversionException when an error occurs looking up the class by alias
	 */
	public Class getClassByAlias(String alias) throws ConversionException;

}