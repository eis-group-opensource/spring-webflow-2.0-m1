/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.convert;

import java.util.Date;

import junit.framework.TestCase;

import org.springframework.binding.convert.support.AbstractConverter;

/**
 * Test case for {@link ConversionExecutor}.
 */
public class ConversionExecutorTests extends TestCase {

	private ConversionExecutor conversionExecutor;

	protected void setUp() throws Exception {
		conversionExecutor = new ConversionExecutor(String.class, Date.class, new TestTextToDate());
	}

	public void testTypeConversion() {
		assertTrue(conversionExecutor.execute("10-10-2008").getClass().equals(Date.class));
	}

	public void testAssignmentCompatibleTypeConversion() {
		java.sql.Date date = new java.sql.Date(123L);
		assertSame(date, conversionExecutor.execute(date));
	}

	public void testConvertNull() {
		assertNull(conversionExecutor.execute(null));
	}

	public void testIllegalType() {
		try {
			conversionExecutor.execute(new StringBuffer());
			fail();
		} catch (ConversionException e) {
			// expected
		}
	}

	private class TestTextToDate extends AbstractConverter {

		public Class[] getSourceClasses() {
			return new Class[] { String.class };
		}

		public Class[] getTargetClasses() {
			return new Class[] { Date.class };
		}

		protected Object doConvert(Object source, Class targetClass, ConversionContext context) throws Exception {
			return source == null ? null : new Date();
		}
	}

}
