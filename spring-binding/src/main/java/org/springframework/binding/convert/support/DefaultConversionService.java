/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.convert.support;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.binding.format.support.SimpleFormatterFactory;
import org.springframework.core.enums.LabeledEnum;

/**
 * Default, local implementation of a conversion service. Will automatically register <i>from string</i> converters for
 * a number of standard Java types like Class, Number, Boolean and so on.
 * 
 * @author Keith Donald
 */
public class DefaultConversionService extends GenericConversionService {

	/**
	 * Creates a new default conversion service, installing the default converters.
	 */
	public DefaultConversionService() {
		addDefaultConverters();
	}

	/**
	 * Add all default converters to the conversion service.
	 */
	protected void addDefaultConverters() {
		addConverter(new TextToClass());
		addConverter(new TextToNumber(new SimpleFormatterFactory()));
		addConverter(new TextToBoolean());
		addConverter(new TextToLabeledEnum());

		// we're not using addDefaultAlias here for efficiency reasons
		addAlias("string", String.class);
		addAlias("short", Short.class);
		addAlias("integer", Integer.class);
		addAlias("int", Integer.class);
		addAlias("byte", Byte.class);
		addAlias("long", Long.class);
		addAlias("float", Float.class);
		addAlias("double", Double.class);
		addAlias("bigInteger", BigInteger.class);
		addAlias("bigDecimal", BigDecimal.class);
		addAlias("boolean", Boolean.class);
		addAlias("class", Class.class);
		addAlias("labeledEnum", LabeledEnum.class);
	}
}