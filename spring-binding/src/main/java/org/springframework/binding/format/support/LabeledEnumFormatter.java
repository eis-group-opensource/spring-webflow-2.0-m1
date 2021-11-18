/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.format.support;

import org.springframework.binding.format.InvalidFormatException;
import org.springframework.core.enums.LabeledEnum;
import org.springframework.core.enums.LabeledEnumResolver;
import org.springframework.core.enums.StaticLabeledEnumResolver;
import org.springframework.util.Assert;

/**
 * Converts from string to a <cod>LabeledEnum</code> instance and back.
 * 
 * @author Keith Donald
 */
public class LabeledEnumFormatter extends AbstractFormatter {

	private LabeledEnumResolver labeledEnumResolver = StaticLabeledEnumResolver.instance();

	/**
	 * Default constructor.
	 */
	public LabeledEnumFormatter() {
	}

	/**
	 * Create a new LabeledEnum formatter.
	 * @param allowEmpty should this formatter allow empty input arguments?
	 */
	public LabeledEnumFormatter(boolean allowEmpty) {
		super(allowEmpty);
	}

	/**
	 * Set the LabeledEnumResolver used. Defaults to {@link StaticLabeledEnumResolver}.
	 */
	public void setLabeledEnumResolver(LabeledEnumResolver labeledEnumResolver) {
		Assert.notNull(labeledEnumResolver, "The labeled enum resolver is required");
		this.labeledEnumResolver = labeledEnumResolver;
	}

	protected String doFormatValue(Object value) {
		LabeledEnum labeledEnum = (LabeledEnum) value;
		return labeledEnum.getLabel();
	}

	protected Object doParseValue(String formattedString, Class targetClass) throws IllegalArgumentException {
		LabeledEnum labeledEnum = labeledEnumResolver.getLabeledEnumByLabel(targetClass, formattedString);
		if (!isAllowEmpty()) {
			Assert.notNull(labeledEnum, "The label '" + formattedString
					+ "' did not map to a valid enum instance for type " + targetClass);
			Assert.isInstanceOf(targetClass, labeledEnum);
		}
		return labeledEnum;
	}

	/**
	 * Convenience method to parse a LabeledEnum.
	 */
	public LabeledEnum parseLabeledEnum(String formattedString, Class enumClass) throws InvalidFormatException {
		return (LabeledEnum) parseValue(formattedString, enumClass);
	}
}