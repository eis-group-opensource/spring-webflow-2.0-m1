/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.core.collection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.binding.collection.MapAccessor;
import org.springframework.binding.convert.ConversionException;
import org.springframework.binding.convert.ConversionExecutor;
import org.springframework.binding.convert.ConversionService;
import org.springframework.binding.convert.support.DefaultConversionService;
import org.springframework.core.style.StylerUtils;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

/**
 * An immutable parameter map storing String-keyed, String-valued parameters in a backing {@link Map} implementation.
 * This base provides convenient operations for accessing parameters in a typed-manner.
 * 
 * @author Keith Donald
 */
public class LocalParameterMap implements ParameterMap, Serializable {

	/**
	 * The backing map storing the parameters.
	 */
	private Map parameters;

	/**
	 * A helper for accessing parameters. Marked transient and restored on deserialization.
	 */
	private transient MapAccessor parameterAccessor;

	/**
	 * A helper for converting string parameter values. Marked transient and restored on deserialization.
	 */
	private transient ConversionService conversionService;

	/**
	 * Creates a new parameter map from the provided map.
	 * <p>
	 * It is expected that the contents of the backing map adhere to the parameter map contract; that is, map entries
	 * have string keys, string values, and remain unmodifiable.
	 * @param parameters the contents of this parameter map
	 */
	public LocalParameterMap(Map parameters) {
		this(parameters, new DefaultConversionService());
	}

	/**
	 * Creates a new parameter map from the provided map.
	 * <p>
	 * It is expected that the contents of the backing map adhere to the parameter map contract; that is, map entries
	 * have string keys, string values, and remain unmodifiable.
	 * @param parameters the contents of this parameter map
	 * @param conversionService a helper for performing type conversion of map entry values
	 */
	public LocalParameterMap(Map parameters, ConversionService conversionService) {
		initParameters(parameters);
		this.conversionService = conversionService;
	}

	public boolean equals(Object o) {
		if (!(o instanceof LocalParameterMap)) {
			return false;
		}
		LocalParameterMap other = (LocalParameterMap) o;
		return parameters.equals(other.parameters);
	}

	public int hashCode() {
		return parameters.hashCode();
	}

	public Map asMap() {
		return Collections.unmodifiableMap(parameterAccessor.asMap());
	}

	public boolean isEmpty() {
		return parameters.isEmpty();
	}

	public int size() {
		return parameters.size();
	}

	public boolean contains(String parameterName) {
		return parameters.containsKey(parameterName);
	}

	public String get(String parameterName) {
		return get(parameterName, (String) null);
	}

	public String get(String parameterName, String defaultValue) {
		if (!parameters.containsKey(parameterName)) {
			return defaultValue;
		}
		Object value = parameters.get(parameterName);
		if (value.getClass().isArray()) {
			parameterAccessor.assertKeyValueInstanceOf(parameterName, value, String[].class);
			String[] array = (String[]) value;
			if (array.length == 0) {
				return null;
			} else {
				Object first = ((String[]) value)[0];
				parameterAccessor.assertKeyValueInstanceOf(parameterName, first, String.class);
				return (String) first;
			}

		} else {
			parameterAccessor.assertKeyValueInstanceOf(parameterName, value, String.class);
			return (String) value;
		}
	}

	public String[] getArray(String parameterName) {
		if (!parameters.containsKey(parameterName)) {
			return null;
		}
		Object value = parameters.get(parameterName);
		if (value.getClass().isArray()) {
			parameterAccessor.assertKeyValueInstanceOf(parameterName, value, String[].class);
			return (String[]) value;
		} else {
			parameterAccessor.assertKeyValueInstanceOf(parameterName, value, String.class);
			return new String[] { (String) value };
		}
	}

	public Object[] getArray(String parameterName, Class targetElementType) throws ConversionException {
		String[] parameters = getArray(parameterName);
		return parameters != null ? convert(parameters, targetElementType) : null;
	}

	public Object get(String parameterName, Class targetType) throws ConversionException {
		return get(parameterName, targetType, null);
	}

	public Object get(String parameterName, Class targetType, Object defaultValue) throws ConversionException {
		if (defaultValue != null) {
			assertAssignableTo(targetType, defaultValue.getClass());
		}
		String parameter = get(parameterName);
		return parameter != null ? convert(parameter, targetType) : defaultValue;
	}

	public String getRequired(String parameterName) throws IllegalArgumentException {
		parameterAccessor.assertContainsKey(parameterName);
		return get(parameterName);
	}

	public String[] getRequiredArray(String parameterName) throws IllegalArgumentException {
		parameterAccessor.assertContainsKey(parameterName);
		return getArray(parameterName);
	}

	public Object[] getRequiredArray(String parameterName, Class targetElementType) throws IllegalArgumentException,
			ConversionException {
		String[] parameters = getRequiredArray(parameterName);
		return convert(parameters, targetElementType);
	}

	public Object getRequired(String parameterName, Class targetType) throws IllegalArgumentException,
			ConversionException {
		return convert(getRequired(parameterName), targetType);
	}

	public Number getNumber(String parameterName, Class targetType) throws ConversionException {
		assertAssignableTo(Number.class, targetType);
		return (Number) get(parameterName, targetType);
	}

	public Number getNumber(String parameterName, Class targetType, Number defaultValue) throws ConversionException {
		assertAssignableTo(Number.class, targetType);
		return (Number) get(parameterName, targetType, defaultValue);
	}

	public Number getRequiredNumber(String parameterName, Class targetType) throws IllegalArgumentException,
			ConversionException {
		assertAssignableTo(Number.class, targetType);
		return (Number) getRequired(parameterName, targetType);
	}

	public Integer getInteger(String parameterName) throws ConversionException {
		return (Integer) get(parameterName, Integer.class);
	}

	public Integer getInteger(String parameterName, Integer defaultValue) throws ConversionException {
		return (Integer) get(parameterName, Integer.class, defaultValue);
	}

	public Integer getRequiredInteger(String parameterName) throws IllegalArgumentException, ConversionException {
		return (Integer) getRequired(parameterName, Integer.class);
	}

	public Long getLong(String parameterName) throws ConversionException {
		return (Long) get(parameterName, Long.class);
	}

	public Long getLong(String parameterName, Long defaultValue) throws ConversionException {
		return (Long) get(parameterName, Long.class, defaultValue);
	}

	public Long getRequiredLong(String parameterName) throws IllegalArgumentException, ConversionException {
		return (Long) getRequired(parameterName, Long.class);
	}

	public Boolean getBoolean(String parameterName) throws ConversionException {
		return (Boolean) get(parameterName, Boolean.class);
	}

	public Boolean getBoolean(String parameterName, Boolean defaultValue) throws ConversionException {
		return (Boolean) get(parameterName, Boolean.class, defaultValue);
	}

	public Boolean getRequiredBoolean(String parameterName) throws IllegalArgumentException, ConversionException {
		return (Boolean) getRequired(parameterName, Boolean.class);
	}

	public MultipartFile getMultipartFile(String parameterName) {
		return (MultipartFile) parameterAccessor.get(parameterName, MultipartFile.class);
	}

	public MultipartFile getRequiredMultipartFile(String parameterName) throws IllegalArgumentException {
		return (MultipartFile) parameterAccessor.getRequired(parameterName, MultipartFile.class);
	}

	public AttributeMap asAttributeMap() {
		return new LocalAttributeMap(getMapInternal());
	}

	/**
	 * Initializes this parameter map.
	 * @param parameters the parameters
	 */
	protected void initParameters(Map parameters) {
		this.parameters = parameters;
		parameterAccessor = new MapAccessor(this.parameters);
	}

	/**
	 * Returns the wrapped, modifiable map implementation.
	 */
	protected Map getMapInternal() {
		return parameters;
	}

	// internal helpers

	/**
	 * Convert given String parameter to specified target type.
	 */
	private Object convert(String parameter, Class targetType) throws ConversionException {
		return conversionService.getConversionExecutor(String.class, targetType).execute(parameter);
	}

	/**
	 * Convert given array of String parameters to specified target type and return the resulting array.
	 */
	private Object[] convert(String[] parameters, Class targetElementType) throws ConversionException {
		List list = new ArrayList(parameters.length);
		ConversionExecutor converter = conversionService.getConversionExecutor(String.class, targetElementType);
		for (int i = 0; i < parameters.length; i++) {
			list.add(converter.execute(parameters[i]));
		}
		return list.toArray((Object[]) Array.newInstance(targetElementType, parameters.length));
	}

	/**
	 * Make sure clazz is assignable from requiredType.
	 */
	private void assertAssignableTo(Class clazz, Class requiredType) {
		Assert.isTrue(clazz.isAssignableFrom(requiredType), "The provided required type must be assignable to ["
				+ clazz + "]");
	}

	// custom serialization

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		parameterAccessor = new MapAccessor(parameters);
		conversionService = new DefaultConversionService();
	}

	public String toString() {
		return StylerUtils.style(parameters);
	}
}