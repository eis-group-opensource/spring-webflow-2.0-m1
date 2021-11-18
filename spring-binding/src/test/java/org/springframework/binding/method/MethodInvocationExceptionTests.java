/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.binding.method;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import junit.framework.TestCase;

/**
 * Test case for {@link MethodInvocationException}.
 * 
 * @author Erwin Vervaet
 */
public class MethodInvocationExceptionTests extends TestCase {

	public void testGetTargetException() {
		// runtime exception
		IllegalArgumentException iae = new IllegalArgumentException("test");
		MethodInvocationException ex = testException(iae);
		assertSame(iae, ex.getTargetException());

		// exception
		IOException ioe = new IOException("test");
		ex = testException(ioe);
		assertSame(ioe, ex.getTargetException());

		// nested
		InvocationTargetException ite = new InvocationTargetException(ioe);
		ex = testException(ite);
		assertSame(ioe, ex.getTargetException());

		// deep nesting
		ite = new InvocationTargetException(new InvocationTargetException(ioe));
		ex = testException(ite);
		assertSame(ioe, ex.getTargetException());
	}

	// internal helpers

	private MethodInvocationException testException(Throwable cause) {
		return new MethodInvocationException(new MethodSignature("test"), null, cause);
	}
}
