/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.format.support;

import java.beans.PropertyEditorSupport;

import org.springframework.binding.format.Formatter;

/**
 * Adapts a formatter to the property editor contract.
 * 
 * @author Keith Donald
 */
public class FormatterPropertyEditor extends PropertyEditorSupport {

	/**
	 * The wrapped formatter.
	 */
	private Formatter formatter;

	/**
	 * The target value class (may be null).
	 */
	private Class targetClass;

	/**
	 * Creates a formatter property editor.
	 * @param formatter the formatter to adapt
	 */
	public FormatterPropertyEditor(Formatter formatter) {
		this.formatter = formatter;
	}

	/**
	 * Creates a formatter property editor.
	 * @param formatter the formatter to adapt
	 * @param targetClass the target class for "setAsText" conversions
	 */
	public FormatterPropertyEditor(Formatter formatter, Class targetClass) {
		this.formatter = formatter;
		this.targetClass = targetClass;
	}

	public String getAsText() {
		return formatter.formatValue(getValue());
	}

	public void setAsText(String text) throws IllegalArgumentException {
		setValue(formatter.parseValue(text, targetClass));
	}
}