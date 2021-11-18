/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.convert.support;

import org.springframework.binding.convert.ConversionExecutor;
import org.springframework.binding.convert.ConversionService;
import org.springframework.binding.expression.Expression;

/**
 * Base class for converters that use other converters to convert things, thus they are conversion-service aware.
 * 
 * @author Keith Donald
 */
public abstract class ConversionServiceAwareConverter extends AbstractConverter implements ConversionServiceAware {

	/**
	 * The conversion service this converter is aware of.
	 */
	private ConversionService conversionService;

	/**
	 * Default constructor, expectes to conversion service to be injected using
	 * {@link #setConversionService(ConversionService)}.
	 */
	protected ConversionServiceAwareConverter() {
	}

	/**
	 * Create a converter using given conversion service.
	 */
	protected ConversionServiceAwareConverter(ConversionService conversionService) {
		setConversionService(conversionService);
	}

	/**
	 * Returns the conversion service used.
	 */
	public ConversionService getConversionService() {
		if (conversionService == null) {
			throw new IllegalStateException("Conversion service not yet set: set it first before calling this method");
		}
		return conversionService;
	}

	public void setConversionService(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	/**
	 * Returns a conversion executor capable of converting string objects to the specified target class.
	 * @param targetClass the target class
	 * @return the conversion executor, never null
	 */
	protected ConversionExecutor fromStringTo(Class targetClass) {
		return getConversionService().getConversionExecutor(String.class, targetClass);
	}

	/**
	 * Returns a conversion executor capable of converting string objects to the target class aliased by the provided
	 * alias.
	 * @param targetAlias the target class alias, e.g "long" or "float"
	 * @return the conversion executor, or <code>null</code> if no suitable converter exists for alias
	 */
	protected ConversionExecutor fromStringToAliased(String targetAlias) {
		return getConversionService().getConversionExecutorByTargetAlias(String.class, targetAlias);
	}

	/**
	 * Returns a conversion executor capable of converting objects from one class to another.
	 * @param sourceClass the source class to convert from
	 * @param targetClass the target class to convert to
	 * @return the conversion executor, never null
	 */
	protected ConversionExecutor converterFor(Class sourceClass, Class targetClass) {
		return getConversionService().getConversionExecutor(sourceClass, targetClass);
	}

	/**
	 * Helper that parsers the given expression string into an expression, using the installed String-&gt;Expression
	 * converter.
	 * @param expressionString the expression string to parse
	 * @return the parsed, evaluatable expression
	 */
	protected Expression parseExpression(String expressionString) {
		return (Expression) fromStringTo(Expression.class).execute(expressionString);
	}
}