/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.convert.support;

import org.springframework.binding.convert.ConversionContext;
import org.springframework.binding.format.support.LabeledEnumFormatter;
import org.springframework.core.enums.LabeledEnum;

/**
 * Converter that converts textual representations of enum instances to a specific instance of <code>LabeledEnum</code>.
 * 
 * @author Keith Donald
 */
public class TextToLabeledEnum extends AbstractConverter {

	private LabeledEnumFormatter labeledEnumFormatter = new LabeledEnumFormatter();

	public Class[] getSourceClasses() {
		return new Class[] { String.class };
	}

	public Class[] getTargetClasses() {
		return new Class[] { LabeledEnum.class };
	}

	protected Object doConvert(Object source, Class targetClass, ConversionContext context) throws Exception {
		return labeledEnumFormatter.parseValue((String) source, targetClass);
	}
}