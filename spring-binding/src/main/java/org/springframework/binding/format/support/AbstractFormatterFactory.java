/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.format.support;

import java.util.Locale;

import org.springframework.binding.format.Formatter;
import org.springframework.binding.format.FormatterFactory;
import org.springframework.binding.format.Style;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.SimpleLocaleContext;

/**
 * Base class for formatter factories. Manages the locale used by the produced formatters using Spring's
 * {@link org.springframework.context.i18n.LocaleContext} system.
 * 
 * @author Keith Donald
 */
public abstract class AbstractFormatterFactory implements FormatterFactory {

	private LocaleContext localeContext = new SimpleLocaleContext(Locale.getDefault());

	private Style defaultDateStyle = Style.MEDIUM;

	private Style defaultTimeStyle = Style.MEDIUM;

	/**
	 * Sets the locale context used. Defaults to a {@link SimpleLocaleContext} holding the system default locale.
	 */
	public void setLocaleContext(LocaleContext localeContext) {
		this.localeContext = localeContext;
	}

	/**
	 * Returns the locale in use.
	 */
	protected Locale getLocale() {
		return localeContext.getLocale();
	}

	/**
	 * Returns the default date style. Defaults to {@link Style#MEDIUM}.
	 */
	protected Style getDefaultDateStyle() {
		return defaultDateStyle;
	}

	/**
	 * Set the default date style.
	 */
	public void setDefaultDateStyle(Style defaultDateStyle) {
		this.defaultDateStyle = defaultDateStyle;
	}

	/**
	 * Returns the default time style. Defaults to {@link Style#MEDIUM}.
	 */
	public Style getDefaultTimeStyle() {
		return defaultTimeStyle;
	}

	/**
	 * Set the default time style.
	 */
	public void setDefaultTimeStyle(Style defaultTimeStyle) {
		this.defaultTimeStyle = defaultTimeStyle;
	}

	public Formatter getDateFormatter() {
		return getDateFormatter(getDefaultDateStyle());
	}

	public Formatter getDateTimeFormatter() {
		return getDateTimeFormatter(getDefaultDateStyle(), getDefaultTimeStyle());
	}

	public Formatter getTimeFormatter() {
		return getTimeFormatter(getDefaultTimeStyle());
	}
}