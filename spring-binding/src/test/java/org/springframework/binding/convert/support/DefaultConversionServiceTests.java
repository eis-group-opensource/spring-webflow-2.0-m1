/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.convert.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;

import org.springframework.binding.convert.ConversionException;
import org.springframework.binding.convert.ConversionExecutor;
import org.springframework.binding.convert.Converter;
import org.springframework.core.enums.ShortCodedLabeledEnum;

/**
 * Test case for the default conversion service.
 * 
 * @author Keith Donald
 */
public class DefaultConversionServiceTests extends TestCase {

	public void testConvertCompatibleTypes() {
		DefaultConversionService service = new DefaultConversionService();
		service.addAlias("list", List.class);

		List lst = new ArrayList();
		assertSame(lst, service.getConversionExecutor(ArrayList.class, List.class).execute(lst));
		assertSame(lst, service.getConversionExecutorByTargetAlias(ArrayList.class, "list").execute(lst));

		try {
			service.getConversionExecutor(List.class, ArrayList.class);
			fail();
		} catch (ConversionException e) {
			// expected
		}
	}

	public void testOverrideConverter() {
		Converter customConverter = new TextToBoolean("ja", "nee");

		DefaultConversionService service = new DefaultConversionService();

		ConversionExecutor executor = service.getConversionExecutor(String.class, Boolean.class);
		assertNotSame(customConverter, executor.getConverter());
		try {
			executor.execute("ja");
			fail();
		} catch (ConversionException e) {
			// expected
		}

		service.addConverter(customConverter);

		executor = service.getConversionExecutor(String.class, Boolean.class);
		assertSame(customConverter, executor.getConverter());
		assertTrue(((Boolean) executor.execute("ja")).booleanValue());
	}

	public void testTargetClassNotSupported() {
		DefaultConversionService service = new DefaultConversionService();
		try {
			service.getConversionExecutor(String.class, HashMap.class);
			fail("Should have thrown an exception");
		} catch (ConversionException e) {
		}
	}

	public void testValidConversion() {
		DefaultConversionService service = new DefaultConversionService();
		ConversionExecutor executor = service.getConversionExecutor(String.class, Integer.class);
		Integer three = (Integer) executor.execute("3");
		assertEquals(3, three.intValue());
	}

	public void testLabeledEnumConversionNoSuchEnum() {
		DefaultConversionService service = new DefaultConversionService();
		ConversionExecutor executor = service.getConversionExecutor(String.class, MyEnum.class);
		try {
			executor.execute("My Invalid Label");
			fail("Should have failed");
		} catch (ConversionException e) {
		}
	}

	public void testValidLabeledEnumConversion() {
		DefaultConversionService service = new DefaultConversionService();
		ConversionExecutor executor = service.getConversionExecutor(String.class, MyEnum.class);
		MyEnum myEnum = (MyEnum) executor.execute("My Label 1");
		assertEquals(MyEnum.ONE, myEnum);
	}

	public static class MyEnum extends ShortCodedLabeledEnum {
		public static MyEnum ONE = new MyEnum(0, "My Label 1");

		public static MyEnum TWO = new MyEnum(1, "My Label 2");

		private MyEnum(int code, String label) {
			super(code, label);
		}
	}
}