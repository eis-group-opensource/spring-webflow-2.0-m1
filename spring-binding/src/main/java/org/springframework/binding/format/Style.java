/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.format;

import java.text.DateFormat;

import org.springframework.core.enums.StaticLabeledEnum;

/**
 * Format styles, similar to those defined by {@link java.text.DateFormat}.
 * 
 * @author Keith Donald
 */
public class Style extends StaticLabeledEnum {

	/**
	 * See {@link java.text.DateFormat#FULL}.
	 */
	public static final Style FULL = new Style(DateFormat.FULL, "Full");

	/**
	 * See {@link java.text.DateFormat#LONG}.
	 */
	public static final Style LONG = new Style(DateFormat.LONG, "Long");

	/**
	 * See {@link java.text.DateFormat#MEDIUM}.
	 */
	public static final Style MEDIUM = new Style(DateFormat.MEDIUM, "Medium");

	/**
	 * See {@link java.text.DateFormat#SHORT}.
	 */
	public static final Style SHORT = new Style(DateFormat.SHORT, "Short");

	/**
	 * Private constructor since this is a type-safe enum.
	 */
	private Style(int code, String label) {
		super(code, label);
	}
}