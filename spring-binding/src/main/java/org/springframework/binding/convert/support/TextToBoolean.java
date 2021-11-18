/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.convert.support;

import org.springframework.binding.convert.ConversionContext;
import org.springframework.util.StringUtils;

/**
 * Converts a textual representation of a boolean object to a <code>Boolean</code> instance.
 * 
 * @author Keith Donald
 */
public class TextToBoolean extends AbstractConverter {

	private static final String VALUE_TRUE = "true";

	private static final String VALUE_FALSE = "false";

	private static final String VALUE_ON = "on";

	private static final String VALUE_OFF = "off";

	private static final String VALUE_YES = "yes";

	private static final String VALUE_NO = "no";

	private static final String VALUE_1 = "1";

	private static final String VALUE_0 = "0";

	private String trueString;

	private String falseString;

	/**
	 * Default constructor. No special true or false strings are considered.
	 */
	public TextToBoolean() {
		this(null, null);
	}

	/**
	 * Create a text to boolean converter. Take given <i>special</i> string representations of true and false into
	 * account.
	 * @param trueString special true string to consider
	 * @param falseString special false string to consider
	 */
	public TextToBoolean(String trueString, String falseString) {
		this.trueString = trueString;
		this.falseString = falseString;
	}

	public Class[] getSourceClasses() {
		return new Class[] { String.class };
	}

	public Class[] getTargetClasses() {
		return new Class[] { Boolean.class };
	}

	protected Object doConvert(Object source, Class targetClass, ConversionContext context) throws Exception {
		String text = (String) source;
		if (!StringUtils.hasText(text)) {
			return null;
		} else if (this.trueString != null && text.equalsIgnoreCase(this.trueString)) {
			return Boolean.TRUE;
		} else if (this.falseString != null && text.equalsIgnoreCase(this.falseString)) {
			return Boolean.FALSE;
		} else if (this.trueString == null
				&& (text.equalsIgnoreCase(VALUE_TRUE) || text.equalsIgnoreCase(VALUE_ON)
						|| text.equalsIgnoreCase(VALUE_YES) || text.equals(VALUE_1))) {
			return Boolean.TRUE;
		} else if (this.falseString == null
				&& (text.equalsIgnoreCase(VALUE_FALSE) || text.equalsIgnoreCase(VALUE_OFF)
						|| text.equalsIgnoreCase(VALUE_NO) || text.equals(VALUE_0))) {
			return Boolean.FALSE;
		} else {
			throw new IllegalArgumentException("Invalid boolean value [" + text + "]");
		}
	}
}