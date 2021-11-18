/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.convert.support;

import java.beans.PropertyEditorSupport;

import org.springframework.binding.convert.ConversionExecutor;
import org.springframework.util.Assert;

/**
 * Adapts a Converter to the PropertyEditor interface.
 * <p>
 * Note: with a converter, only forward conversion from-string-to-value is supported. Value-to-string conversion is not
 * supported. If you need this capability, use a Formatter with a FormatterPropertyEditor adapter.
 * 
 * @see org.springframework.binding.format.Formatter
 * @see org.springframework.binding.format.support.FormatterPropertyEditor
 * 
 * @author Keith Donald
 */
public class ConverterPropertyEditorAdapter extends PropertyEditorSupport {

	private ConversionExecutor conversionExecutor;

	/**
	 * Adapt given conversion executor to the PropertyEditor contract.
	 */
	public ConverterPropertyEditorAdapter(ConversionExecutor conversionExecutor) {
		Assert.notNull(conversionExecutor, "A conversion executor is required");
		Assert.isTrue(conversionExecutor.getSourceClass().equals(String.class),
				"A string conversion executor is required");
		this.conversionExecutor = conversionExecutor;
	}

	/**
	 * Returns the type strings will be converted to.
	 */
	public Class getTargetClass() {
		return conversionExecutor.getTargetClass();
	}

	public void setAsText(String text) throws IllegalArgumentException {
		setValue(conversionExecutor.execute(text));
	}

	public String getAsText() {
		throw new UnsupportedOperationException();
	}
}