/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.method;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;

import junit.framework.TestCase;

/**
 * @author Rob Harrop
 * @since 1.0
 */
public class MethodKeyTests extends TestCase {

	private static final Method LIST_NO_ARGS = safeGetMethod(File.class, "list", null);

	private static final Method LIST_FILENAME_FILTER = safeGetMethod(File.class, "list",
			new Class[] { FilenameFilter.class });

	public void testGetMethodWithNoArgs() throws Exception {
		MethodKey key = new MethodKey(File.class, "list", new Class[0]);
		Method m = key.getMethod();
		assertEquals(LIST_NO_ARGS, m);
	}

	public void testGetMoreGenericMethod() throws Exception {
		MethodKey key = new MethodKey(Object.class, "equals", new Class[] { Long.class });
		assertEquals(safeGetMethod(Object.class, "equals", new Class[] { Object.class }), key.getMethod());
	}

	public void testGetMethodWithSingleArg() throws Exception {
		MethodKey key = new MethodKey(File.class, "list", new Class[] { FilenameFilter.class });
		Method m = key.getMethod();
		assertEquals(LIST_FILENAME_FILTER, m);
	}

	public void testGetMethodWithSingleNullArgAndValidMatch() throws Exception {
		MethodKey key = new MethodKey(File.class, "list", new Class[] { null });
		Method m = key.getMethod();
		assertEquals(LIST_FILENAME_FILTER, m);
	}

	public void testGetMethodWithSingleNullAndUnclearMatch() throws Exception {
		new MethodKey(File.class, "listFiles", new Class[] { null });
	}

	private static final Method safeGetMethod(Class type, String name, Class[] argTypes) {
		try {
			return type.getMethod(name, argTypes);
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException("Unable to safely access a known method via reflection. " + e.getMessage());
		}
	}
}
