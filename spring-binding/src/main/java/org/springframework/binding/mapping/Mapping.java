/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.mapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.binding.convert.ConversionExecutor;
import org.springframework.binding.expression.Expression;
import org.springframework.binding.expression.SettableExpression;
import org.springframework.core.style.ToStringCreator;
import org.springframework.util.Assert;

/**
 * A single mapping definition, encapulating the information neccessary to map the result of evaluating an expression on
 * a source object to a property on a target object, optionally applying a type conversion during the mapping process.
 * 
 * @author Keith Donald
 */
public class Mapping implements AttributeMapper {

	private static final Log logger = LogFactory.getLog(Mapping.class);

	/**
	 * The source expression to evaluate against a source object to map from.
	 */
	private final Expression sourceExpression;

	/**
	 * The target expression to set on a target object to map to.
	 */
	private final SettableExpression targetExpression;

	/**
	 * A type converter to apply during the mapping process.
	 */
	private final ConversionExecutor typeConverter;

	/**
	 * Whether or not this is a required mapping; if true, the source expression must return a non-null value.
	 */
	private boolean required;

	/**
	 * Creates a new mapping.
	 * @param sourceExpression the source expression
	 * @param targetExpression the target expression
	 * @param typeConverter a type converter
	 */
	public Mapping(Expression sourceExpression, SettableExpression targetExpression, ConversionExecutor typeConverter) {
		this(sourceExpression, targetExpression, typeConverter, false);
	}

	/**
	 * Creates a new mapping.
	 * @param sourceExpression the source expression
	 * @param targetExpression the target expression
	 * @param typeConverter a type converter
	 * @param required whether or not this mapping is required
	 */
	protected Mapping(Expression sourceExpression, SettableExpression targetExpression,
			ConversionExecutor typeConverter, boolean required) {
		Assert.notNull(sourceExpression, "The source expression is required");
		Assert.notNull(targetExpression, "The target expression is required");
		this.sourceExpression = sourceExpression;
		this.targetExpression = targetExpression;
		this.typeConverter = typeConverter;
		this.required = required;
	}

	/**
	 * Map the <code>sourceAttribute</code> in to the <code>targetAttribute</code> target map, performing type
	 * conversion if necessary.
	 * @param source The source data structure
	 * @param target The target data structure
	 */
	public void map(Object source, Object target, MappingContext context) {
		// get source value
		Object sourceValue = sourceExpression.evaluate(source, null);
		if (sourceValue == null) {
			if (required) {
				throw new RequiredMappingException("This mapping is required; evaluation of expression '"
						+ sourceExpression + "' against source of type [" + source.getClass()
						+ "] must return a non-null value");
			} else {
				// source expression returned no value, simply abort mapping
				return;
			}
		}
		Object targetValue = sourceValue;
		if (typeConverter != null) {
			targetValue = typeConverter.execute(sourceValue);
		}
		// set target value
		if (logger.isDebugEnabled()) {
			logger.debug("Mapping '" + sourceExpression + "' value [" + sourceValue + "] to target property '"
					+ targetExpression + "'; setting property value to [" + targetValue + "]");
		}
		targetExpression.evaluateToSet(target, targetValue, null);
	}

	public boolean equals(Object o) {
		if (!(o instanceof Mapping)) {
			return false;
		}
		Mapping other = (Mapping) o;
		return sourceExpression.equals(other.sourceExpression) && targetExpression.equals(other.targetExpression);
	}

	public int hashCode() {
		return sourceExpression.hashCode() + targetExpression.hashCode();
	}

	public String toString() {
		return new ToStringCreator(this).append(sourceExpression + " -> " + targetExpression).toString();
	}
}