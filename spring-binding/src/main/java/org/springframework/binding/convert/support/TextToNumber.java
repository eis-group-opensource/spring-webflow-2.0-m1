/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.convert.support;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.binding.convert.ConversionContext;
import org.springframework.binding.format.FormatterFactory;
import org.springframework.binding.format.support.SimpleFormatterFactory;

/**
 * Converts textual representations of numbers to a <code>Number</code> specialization. Delegates to a synchronized
 * formatter to parse text strings.
 * 
 * @author Keith Donald
 */
public class TextToNumber extends AbstractFormattingConverter {

	/**
	 * Default constructor that uses a {@link SimpleFormatterFactory}.
	 */
	public TextToNumber() {
		super(new SimpleFormatterFactory());
	}

	/**
	 * Create a string to number converter using given formatter factory.
	 * @param formatterFactory the factory to use
	 */
	public TextToNumber(FormatterFactory formatterFactory) {
		super(formatterFactory);
	}

	public Class[] getSourceClasses() {
		return new Class[] { String.class };
	}

	public Class[] getTargetClasses() {
		return new Class[] { Integer.class, Short.class, Byte.class, Long.class, Float.class, Double.class,
				BigInteger.class, BigDecimal.class };
	}

	protected Object doConvert(Object source, Class targetClass, ConversionContext context) throws Exception {
		return getFormatterFactory().getNumberFormatter(targetClass).parseValue((String) source, targetClass);
	}
}