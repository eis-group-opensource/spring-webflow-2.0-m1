/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.format.support;

import java.math.BigDecimal;
import java.util.Locale;

import org.springframework.binding.format.Formatter;
import org.springframework.context.i18n.SimpleLocaleContext;

import junit.framework.TestCase;

/**
 * Unit tests for {@link NumberFormatterTests}.
 * 
 * @author Erwin Vervaet
 */
public class NumberFormatterTests extends TestCase {

	private Locale systemDefaultLocale;

	protected void setUp() throws Exception {
		systemDefaultLocale = Locale.getDefault();
	}

	protected void tearDown() throws Exception {
		// restore default
		Locale.setDefault(systemDefaultLocale);
	}

	public void testParseUsBigDecimalInUs() {
		Locale.setDefault(Locale.US);
		SimpleFormatterFactory formatterFactory = new SimpleFormatterFactory();
		Formatter formatter = formatterFactory.getNumberFormatter(BigDecimal.class);
		assertEquals(new BigDecimal("123.45"), formatter.parseValue("123.45", BigDecimal.class));
	}

	public void testParseUsBigDecimalInGermany() {
		Locale.setDefault(Locale.GERMANY);
		SimpleFormatterFactory formatterFactory = new SimpleFormatterFactory();
		// we're expressing our numbers in US notation!
		formatterFactory.setLocaleContext(new SimpleLocaleContext(Locale.US));
		Formatter formatter = formatterFactory.getNumberFormatter(BigDecimal.class);
		assertEquals(new BigDecimal("123.45"), formatter.parseValue("123.45", BigDecimal.class));
	}

	public void testParseGermanBigDecimalInGermany() {
		Locale.setDefault(Locale.GERMANY);
		SimpleFormatterFactory formatterFactory = new SimpleFormatterFactory();
		Formatter formatter = formatterFactory.getNumberFormatter(BigDecimal.class);
		assertEquals(new BigDecimal("123.45"), formatter.parseValue("123,45", BigDecimal.class));
	}
}
