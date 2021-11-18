/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.convert.support;

import java.util.Date;

import junit.framework.TestCase;

import org.springframework.binding.convert.ConversionException;
import org.springframework.binding.convert.ConversionExecutor;
import org.springframework.binding.convert.ConversionService;

/**
 * Test case for the {@link CompositeConversionService}.
 * 
 * @author Erwin Vervaet
 */
public class CompositeConversionServiceTests extends TestCase {

	private CompositeConversionService service;

	protected void setUp() throws Exception {
		GenericConversionService first = new GenericConversionService();
		first.addConverter(new TextToClass());
		first.addConverter(new TextToBoolean("ja", "nee"));
		first.addDefaultAlias(Boolean.class);
		GenericConversionService second = new GenericConversionService();
		second.addConverter(new TextToNumber());
		second.addConverter(new TextToBoolean());
		second.addDefaultAlias(Integer.class);
		service = new CompositeConversionService(new ConversionService[] { first, second });
	}

	public void testGetConversionExecutor() {
		assertNotNull(service.getConversionExecutor(String.class, Class.class));
		assertNotNull(service.getConversionExecutor(String.class, Boolean.class));
		assertEquals(Boolean.TRUE, service.getConversionExecutor(String.class, Boolean.class).execute("ja"));
		assertNotNull(service.getConversionExecutor(String.class, Integer.class));
		try {
			service.getConversionExecutor(String.class, Date.class);
			fail();
		} catch (ConversionException e) {
			// expected
		}
	}

	public void testGetConversionExecutorByTargetAlias() {
		assertNotNull(service.getConversionExecutorByTargetAlias(String.class, "boolean"));
		assertEquals(Boolean.TRUE, service.getConversionExecutorByTargetAlias(String.class, "boolean").execute("ja"));
		assertNotNull(service.getConversionExecutorByTargetAlias(String.class, "integer"));
		assertNull(service.getConversionExecutorByTargetAlias(String.class, "class"));
	}

	public void testGetConversionExecutorsForSource() {
		assertEquals(new TextToClass().getTargetClasses().length + new TextToBoolean().getTargetClasses().length
				+ new TextToNumber().getTargetClasses().length,
				service.getConversionExecutorsForSource(String.class).length);
		assertEquals(0, service.getConversionExecutorsForSource(Date.class).length);
		ConversionExecutor[] fromStringConversionExecutors = service.getConversionExecutorsForSource(String.class);
		ConversionExecutor booleanConversionExecutor = null;
		for (int i = 0; i < fromStringConversionExecutors.length; i++) {
			if (fromStringConversionExecutors[i].getConverter() instanceof TextToBoolean) {
				booleanConversionExecutor = fromStringConversionExecutors[i];
			}
		}
		assertEquals(Boolean.TRUE, booleanConversionExecutor.execute("ja"));
	}

	public void testGetClassByAlias() {
		assertEquals(Boolean.class, service.getClassByAlias("boolean"));
		assertEquals(Integer.class, service.getClassByAlias("integer"));
		assertNull(service.getClassByAlias("class"));
	}
}
