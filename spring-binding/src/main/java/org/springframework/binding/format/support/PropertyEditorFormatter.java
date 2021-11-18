/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.format.support;

import java.beans.PropertyEditor;

import org.springframework.util.Assert;

/**
 * Adapts a property editor to the formatter interface.
 * 
 * @author Keith Donald
 */
public class PropertyEditorFormatter extends AbstractFormatter {

	private PropertyEditor propertyEditor;

	/**
	 * Wrap given property editor in a formatter.
	 */
	public PropertyEditorFormatter(PropertyEditor propertyEditor) {
		Assert.notNull(propertyEditor, "Property editor is required");
		this.propertyEditor = propertyEditor;
	}

	/**
	 * Returns the wrapped property editor.
	 */
	public PropertyEditor getPropertyEditor() {
		return propertyEditor;
	}

	protected String doFormatValue(Object value) {
		propertyEditor.setValue(value);
		return propertyEditor.getAsText();
	}

	protected Object doParseValue(String formattedValue, Class targetClass) {
		propertyEditor.setAsText(formattedValue);
		return propertyEditor.getValue();
	}
}