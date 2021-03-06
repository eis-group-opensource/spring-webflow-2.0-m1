/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.core.collection;

import org.springframework.binding.collection.MapAdaptable;
import org.springframework.binding.convert.ConversionException;
import org.springframework.web.multipart.MultipartFile;

/**
 * An interface for accessing parameters in a backing map. Parameters are immutable and have string keys and string
 * values.
 * <p>
 * The accessor methods offered by this class taking a target type argument only need to support conversions to well
 * know types like String, Number subclasses, Boolean and so on.
 * 
 * @author Keith Donald
 */
public interface ParameterMap extends MapAdaptable {

	/**
	 * Is this parameter map empty, with a size of 0?
	 * @return true if empty, false if not
	 */
	public boolean isEmpty();

	/**
	 * Returns the number of parameters in this map.
	 * @return the parameter count
	 */
	public int size();

	/**
	 * Does the parameter with the provided name exist in this map?
	 * @param parameterName the parameter name
	 * @return true if so, false otherwise
	 */
	public boolean contains(String parameterName);

	/**
	 * Get a parameter value, returning <code>null</code> if no value is found.
	 * @param parameterName the parameter name
	 * @return the parameter value
	 */
	public String get(String parameterName);

	/**
	 * Get a parameter value, returning the defaultValue if no value is found.
	 * @param parameterName the parameter name
	 * @param defaultValue the default
	 * @return the parameter value
	 */
	public String get(String parameterName, String defaultValue);

	/**
	 * Get a multi-valued parameter value, returning <code>null</code> if no value is found. If the parameter is
	 * single valued an array with a single element is returned.
	 * @param parameterName the parameter name
	 * @return the parameter value array
	 */
	public String[] getArray(String parameterName);

	/**
	 * Get a multi-valued parameter value, converting each value to the target type or returning <code>null</code> if
	 * no value is found.
	 * @param parameterName the parameter name
	 * @param targetElementType the target type of the array's elements
	 * @return the converterd parameter value array
	 * @throws ConversionException when the value could not be converted
	 */
	public Object[] getArray(String parameterName, Class targetElementType) throws ConversionException;

	/**
	 * Get a parameter value, converting it from <code>String</code> to the target type.
	 * @param parameterName the name of the parameter
	 * @param targetType the target type of the parameter value
	 * @return the converted parameter value, or null if not found
	 * @throws ConversionException when the value could not be converted
	 */
	public Object get(String parameterName, Class targetType) throws ConversionException;

	/**
	 * Get a parameter value, converting it from <code>String</code> to the target type or returning the defaultValue
	 * if not found.
	 * @param parameterName name of the parameter to get
	 * @param targetType the target type of the parameter value
	 * @param defaultValue the default value
	 * @return the converted parameter value, or the default if not found
	 * @throws ConversionException when a value could not be converted
	 */
	public Object get(String parameterName, Class targetType, Object defaultValue) throws ConversionException;

	/**
	 * Get the value of a required parameter.
	 * @param parameterName the name of the parameter
	 * @return the parameter value
	 * @throws IllegalArgumentException when the parameter is not found
	 */
	public String getRequired(String parameterName) throws IllegalArgumentException;

	/**
	 * Get a required multi-valued parameter value.
	 * @param parameterName the name of the parameter
	 * @return the parameter value
	 * @throws IllegalArgumentException when the parameter is not found
	 */
	public String[] getRequiredArray(String parameterName) throws IllegalArgumentException;

	/**
	 * Get a required multi-valued parameter value, converting each value to the target type.
	 * @param parameterName the name of the parameter
	 * @return the parameter value
	 * @throws IllegalArgumentException when the parameter is not found
	 * @throws ConversionException when a value could not be converted
	 */
	public Object[] getRequiredArray(String parameterName, Class targetElementType) throws IllegalArgumentException,
			ConversionException;

	/**
	 * Get the value of a required parameter and convert it to the target type.
	 * @param parameterName the name of the parameter
	 * @param targetType the target type of the parameter value
	 * @return the converted parameter value
	 * @throws IllegalArgumentException when the parameter is not found
	 * @throws ConversionException when the value could not be converted
	 */
	public Object getRequired(String parameterName, Class targetType) throws IllegalArgumentException,
			ConversionException;

	/**
	 * Returns a number parameter value in the map that is of the specified type, returning <code>null</code> if no
	 * value was found.
	 * @param parameterName the parameter name
	 * @param targetType the target number type
	 * @return the number parameter value
	 * @throws ConversionException when the value could not be converted
	 */
	public Number getNumber(String parameterName, Class targetType) throws ConversionException;

	/**
	 * Returns a number parameter value in the map of the specified type, returning the defaultValue if no value was
	 * found.
	 * @param parameterName the parameter name
	 * @param defaultValue the default
	 * @return the number parameter value
	 * @throws ConversionException when the value could not be converted
	 */
	public Number getNumber(String parameterName, Class targetType, Number defaultValue) throws ConversionException;

	/**
	 * Returns a number parameter value in the map, throwing an exception if the parameter is not present or could not
	 * be converted.
	 * @param parameterName the parameter name
	 * @return the number parameter value
	 * @throws IllegalArgumentException if the parameter is not present
	 * @throws ConversionException when the value could not be converted
	 */
	public Number getRequiredNumber(String parameterName, Class targetType) throws IllegalArgumentException,
			ConversionException;

	/**
	 * Returns an integer parameter value in the map, returning <code>null</code> if no value was found.
	 * @param parameterName the parameter name
	 * @return the integer parameter value
	 * @throws ConversionException when the value could not be converted
	 */
	public Integer getInteger(String parameterName) throws ConversionException;

	/**
	 * Returns an integer parameter value in the map, returning the defaultValue if no value was found.
	 * @param parameterName the parameter name
	 * @param defaultValue the default
	 * @return the integer parameter value
	 * @throws ConversionException when the value could not be converted
	 */
	public Integer getInteger(String parameterName, Integer defaultValue) throws ConversionException;

	/**
	 * Returns an integer parameter value in the map, throwing an exception if the parameter is not present or could not
	 * be converted.
	 * @param parameterName the parameter name
	 * @return the integer parameter value
	 * @throws IllegalArgumentException if the parameter is not present
	 * @throws ConversionException when the value could not be converted
	 */
	public Integer getRequiredInteger(String parameterName) throws IllegalArgumentException, ConversionException;

	/**
	 * Returns a long parameter value in the map, returning <code>null</code> if no value was found.
	 * @param parameterName the parameter name
	 * @return the long parameter value
	 * @throws ConversionException when the value could not be converted
	 */
	public Long getLong(String parameterName) throws ConversionException;

	/**
	 * Returns a long parameter value in the map, returning the defaultValue if no value was found.
	 * @param parameterName the parameter name
	 * @param defaultValue the default
	 * @return the long parameter value
	 * @throws ConversionException when the value could not be converted
	 */
	public Long getLong(String parameterName, Long defaultValue) throws ConversionException;

	/**
	 * Returns a long parameter value in the map, throwing an exception if the parameter is not present or could not be
	 * converted.
	 * @param parameterName the parameter name
	 * @return the long parameter value
	 * @throws IllegalArgumentException if the parameter is not present
	 * @throws ConversionException when the value could not be converted
	 */
	public Long getRequiredLong(String parameterName) throws IllegalArgumentException, ConversionException;

	/**
	 * Returns a boolean parameter value in the map, returning <code>null</code> if no value was found.
	 * @param parameterName the parameter name
	 * @return the long parameter value
	 * @throws ConversionException when the value could not be converted
	 */
	public Boolean getBoolean(String parameterName) throws ConversionException;

	/**
	 * Returns a boolean parameter value in the map, returning the defaultValue if no value was found.
	 * @param parameterName the parameter name
	 * @param defaultValue the default
	 * @return the boolean parameter value
	 * @throws ConversionException when the value could not be converted
	 */
	public Boolean getBoolean(String parameterName, Boolean defaultValue) throws ConversionException;

	/**
	 * Returns a boolean parameter value in the map, throwing an exception if the parameter is not present or could not
	 * be converted.
	 * @param parameterName the parameter name
	 * @return the boolean parameter value
	 * @throws IllegalArgumentException if the parameter is not present
	 * @throws ConversionException when the value could not be converted
	 */
	public Boolean getRequiredBoolean(String parameterName) throws IllegalArgumentException, ConversionException;

	/**
	 * Get a multi-part file parameter value, returning <code>null</code> if no value is found.
	 * @param parameterName the parameter name
	 * @return the multipart file
	 */
	public MultipartFile getMultipartFile(String parameterName);

	/**
	 * Get the value of a required multipart file parameter.
	 * @param parameterName the name of the parameter
	 * @return the parameter value
	 * @throws IllegalArgumentException when the parameter is not found
	 */
	public MultipartFile getRequiredMultipartFile(String parameterName);

	/**
	 * Adapts this parameter map to an {@link AttributeMap}.
	 * @return the underlying map as a unmodifiable attribute map
	 */
	public AttributeMap asAttributeMap();

}