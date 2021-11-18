/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.convert.support;

import org.springframework.binding.format.FormatterFactory;

/**
 * A converter that delegates to a formatter to perform the conversion. Formatters are typically not thread safe, so we
 * use a FormatterFactory that is expected to provide us with thread-safe instances as necessary.
 * 
 * @author Keith Donald
 */
public abstract class AbstractFormattingConverter extends AbstractConverter {

	/**
	 * The formatter factory.
	 */
	private FormatterFactory formatterFactory;

	/**
	 * Creates a new converter that delegates to a formatter.
	 * @param formatterFactory the factory to use
	 */
	protected AbstractFormattingConverter(FormatterFactory formatterFactory) {
		setFormatterFactory(formatterFactory);
	}

	protected FormatterFactory getFormatterFactory() {
		return formatterFactory;
	}

	public void setFormatterFactory(FormatterFactory formatterSource) {
		this.formatterFactory = formatterSource;
	}
}