/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.format.support;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import org.springframework.binding.format.Formatter;
import org.springframework.binding.format.Style;

/**
 * Simple FormatterFactory implementation.
 * 
 * @author Keith Donald
 */
public class SimpleFormatterFactory extends AbstractFormatterFactory {

	public Formatter getDateFormatter(Style style) {
		return new DateFormatter(SimpleDateFormat.getDateInstance(style.shortValue(), getLocale()));
	}

	public Formatter getDateTimeFormatter(Style dateStyle, Style timeStyle) {
		return new DateFormatter(SimpleDateFormat.getDateTimeInstance(dateStyle.shortValue(), timeStyle.shortValue(),
				getLocale()));
	}

	public Formatter getTimeFormatter(Style style) {
		return new DateFormatter(SimpleDateFormat.getTimeInstance(style.shortValue(), getLocale()));
	}

	public Formatter getNumberFormatter(Class numberClass) {
		return new NumberFormatter(NumberFormat.getNumberInstance(getLocale()));
	}

	public Formatter getCurrencyFormatter() {
		return new NumberFormatter(NumberFormat.getCurrencyInstance(getLocale()));
	}

	public Formatter getDateFormatter(String encodedFormat) {
		return new DateFormatter(new SimpleDateFormat(encodedFormat, getLocale()));
	}

	public Formatter getPercentFormatter() {
		return new NumberFormatter(NumberFormat.getPercentInstance(getLocale()));
	}
}