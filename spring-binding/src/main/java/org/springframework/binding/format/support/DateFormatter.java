/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.format.support;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import org.springframework.binding.format.InvalidFormatException;

/**
 * Formatter that formats date objects.
 * 
 * @author Keith Donald
 */
public class DateFormatter extends AbstractFormatter {

	private DateFormat dateFormat;

	/**
	 * Constructs a date formatter that will delegate to the specified date format.
	 * @param dateFormat the date format to use
	 */
	public DateFormatter(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	/**
	 * Constructs a date formatter that will delegate to the specified date format.
	 * @param dateFormat the date format to use
	 * @param allowEmpty should this formatter allow empty input arguments?
	 */
	public DateFormatter(DateFormat dateFormat, boolean allowEmpty) {
		super(allowEmpty);
		this.dateFormat = dateFormat;
	}

	// convert from date to string
	protected String doFormatValue(Object date) {
		return dateFormat.format((Date) date);
	}

	// convert back from string to date
	protected Object doParseValue(String formattedString, Class targetClass) throws ParseException {
		return dateFormat.parse(formattedString);
	}

	/**
	 * Convenience method to parse a date.
	 */
	public Date parseDate(String formattedString) throws InvalidFormatException {
		return (Date) parseValue(formattedString, Date.class);
	}
}